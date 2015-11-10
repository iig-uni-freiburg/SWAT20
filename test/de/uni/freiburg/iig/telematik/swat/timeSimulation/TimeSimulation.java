package de.uni.freiburg.iig.telematik.swat.timeSimulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import de.invation.code.toval.misc.ListUtils;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeContext;
import de.uni.freiburg.iig.telematik.swat.jascha.AwesomeResourceContext;
import de.uni.freiburg.iig.telematik.swat.jascha.SimpleResource;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;
import de.uni.freiburg.iig.telematik.swat.simon.AwesomeTimeContext;
import de.uni.freiburg.iig.telematik.swat.simon.ITimeBehaviourFactory;

public class TimeSimulation {

	public static void main(String args[]) {

		TimedNet net = getRTPNet();
		net.setInitialMarking(net.getMarking());
		// getCurrentTimeOfNet(net);
		// fireNet(net);
		// getCurrentTimeOfNet(net);
		// fireNet(net);
		// getCurrentTimeOfNet(net);
		// fireNet(net);
		// getCurrentTimeOfNet(net);
		// fireNet(net);
		// getCurrentTimeOfNet(net);
		ArrayList<Double> results = new ArrayList<>();
		for (int i = 0; i<100000; i++) {
			try {
				while (net.moreToSimulate()) {
					net.fire();
					//getCurrentTimeOfNet(net);
				}
			} catch (PNException e) {
				results.add(net.getCurrentTime());
				//System.out.println("Net finished with time " + net.getCurrentTime());
			}
			net.reset();
		}
		
		generateDiagram(results, 100);
	}

	public static void fireNet(TimedNet net) {
		try {
			net.fire();
		} catch (PNException e) {
		}
	}

	public static void getCurrentTimeOfNet(TimedNet net) {
		System.out.println("Current net-time is: " + net.getCurrentTime());
	}

	public static TimedNet getRTPNet() {
		TimedNet net = new TimedNet();
		net.addTransition("test");
		net.addTransition("test2");
		net.addPlace("start");
		net.getPlace("start").setState(1);
		net.addPlace("place2");
		net.addPlace("end");
		net.addFlowRelationPT("start", "test");
		net.addFlowRelationTP("test", "place2");
		net.addFlowRelationPT("place2", "test2");
		net.addFlowRelationTP("test2", "end");
		net.setResourceContext(getResourceContext());
		// net.setAccessControl(getProcessContext());
		net.setTimeContext(getTimeContext());
		// net.setTimeRessourceContext(new TestTimedResourceContext());
		return net;
	}

	public static ITimeContext getTimeContext() {
		AwesomeTimeContext timeContext = new AwesomeTimeContext();
		LinkedList<Double> params1 = new LinkedList<>();
		params1.add(20.0);
		params1.add(4.0);
		LinkedList<Double> params2=new LinkedList<>();
		params2.add(15.0);
		params2.add(0.3);
		timeContext.addBehaviour("test", ITimeBehaviourFactory.getBahaviour(DistributionType.NORMAL, params1));
		timeContext.addBehaviour("test2", ITimeBehaviourFactory.getBahaviour(DistributionType.LOG_NORMAL, params2));
		return timeContext;
	}

	public static IResourceContext getResourceContext() {
		AwesomeResourceContext resourceContext = new AwesomeResourceContext();
		resourceContext.addResourceUsage("test", new SimpleResource("Schraubenzieher1"));
		resourceContext.addResourceUsage("test", new SimpleResource("Schraubenzieher2"));
		resourceContext.addResourceUsage("test2", new SimpleResource("Schraubenzieher1"));
		resourceContext.addResourceUsage("test2", new SimpleResource("Schraubenzieher2"));
		return resourceContext;
	}
	
	private static void generateDiagram(List<Double> results_list, int bins) {
		double[] buffer = new double[results_list.size()];
		for (int i = 0; i < buffer.length; i++)
			buffer[i] = results_list.get(i);
		// The histogram takes an array
		HistogramDataset histo = new HistogramDataset();
		histo.addSeries("Relative Occurence of Duration", buffer, bins);
		histo.setType(HistogramType.RELATIVE_FREQUENCY);
		//histo.setType(HistogramType.SCALE_AREA_TO_1);

		JFrame aFrame = new JFrame("Time analysis");
		//ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		JFreeChart chart = ChartFactory.createHistogram("Distribution of simulated workflow duration",
				"Duration of Workflow execution in ms", "Relative occurence", histo, PlotOrientation.VERTICAL, true, true, false);

		// to save as JPG
		XYPlot plot = (XYPlot) chart.getPlot();
		XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();

		renderer.setDrawBarOutline(false);

		renderer.setSeriesOutlinePaint(0, Color.red);

		//plot.getRangeAxis().setRange(0, 0.2);
		//plot.getDomainAxis().setRange(0, 30);
		plot.getRangeAxis().setTickLabelFont(new Font("Arial", 0, 30));
		plot.getDomainAxis().setTickLabelFont(new Font("Arial", 0, 30));
		plot.getRangeAxis().setLabelFont(new Font("Arial", 1, 28));
		plot.getDomainAxis().setLabelFont(new Font("Arial", 1, 28));
		//plot.getLegendItems().get(0).set(new Font("Arial", 1, 26));
		//plot.getLegendItems().get(1).setLabelFont(new Font("Arial", 1, 24));
		LegendTitle legend = chart.getLegend();
		Font nwfont = new Font("Arial", 0, 26);
		legend.setItemFont(nwfont);
		//chart.setLegend(legend);

		ChartPanel panel = new ChartPanel(chart);
		panel.setPreferredSize(new java.awt.Dimension(900, 600));
		aFrame.setContentPane(panel);
		aFrame.setPreferredSize(new java.awt.Dimension(900, 600));
		aFrame.setSize(new Dimension(900, 600));
		aFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		aFrame.setVisible(true);
	}

}
