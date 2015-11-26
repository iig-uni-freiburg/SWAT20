package de.uni.freiburg.iig.telematik.swat.timeSimulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.WorkflowTimeMachine;

public class SingleTimeSimulation {
	
	static Map<String,TimedNet> nets = new HashMap<>();

	public static void main(String args[]) {
		
		WorkflowTimeMachine timeMachine = WorkflowTimeMachine.getInstance();
		
		timeMachine.addNet(TimedNetRep.getSimpleLinearTimedNet(ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		timeMachine.addNet(TimedNetRep.getSimpleANDTimedNet(ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		timeMachine.addNet(TimedNetRep.getSimpleORTimedNet(ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		
		HashMap<String, ArrayList<Double>> result = timeMachine.simulateAll(2);
		
		for(Entry<String, ArrayList<Double>> entry :result.entrySet()){
			generateDiagram(entry.getValue(), 100, entry.getKey());
		}
		
		//nets.put("linear", TimedNetRep.getSimpleLinearTimedNet(ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		//nets.put("Parallel AND",TimedNetRep.getSimpleANDTimedNet(ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		//nets.put("OR", TimedNetRep.getSimpleORTimedNet(ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));

		//TimedNet net = TimedNetRep.getSimpleORTimedNet(getResourceContext(), getTimeContext());
		//TimedNet net = TimedNetRep.getSimpleANDTimedNet(getResourceContext(), getTimeContext());
		//TimedNet net = TimedNetRep.getSimpleLinearTimedNet(getResourceContext(), getTimeContext());
		
		// getCurrentTimeOfNet(net);
		// fireNet(net);
		// getCurrentTimeOfNet(net);
		// fireNet(net);
		// getCurrentTimeOfNet(net);
		// fireNet(net);
		// getCurrentTimeOfNet(net);
		// fireNet(net);
		// getCurrentTimeOfNet(net);

//		for(Entry<String, TimedNet> e:nets.entrySet()){
//			ArrayList<Double> results = simulateNet(e.getValue());
//			generateDiagram(results, 100 ,e.getKey());
//		}
		
		
	}
	
	public static ArrayList<Double> simulateNet(TimedNet net){
		ArrayList<Double> results = new ArrayList<>();
		for (int i = 0; i<123456; i++) {
			try {
				while (!net.isFinished()) {
					net.fire();
					//getCurrentTimeOfNet(net);
				}
			} catch (PNException e) {
				
				//System.out.println("Net finished with time " + net.getCurrentTime());
			}
			results.add(net.getCurrentTime());
			net.reset();
		}
		return results;
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
	
	private static void generateDiagram(List<Double> results_list, int bins, String title) {
		double[] buffer = new double[results_list.size()];
		for (int i = 0; i < buffer.length; i++)
			buffer[i] = results_list.get(i);
		// The histogram takes an array
		HistogramDataset histo = new HistogramDataset();
		histo.addSeries(title+" Relative Occurence of Duration", buffer, bins);
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
		//aFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		aFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		aFrame.setVisible(true);
	}

}
