package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
import org.jfree.ui.ApplicationFrame;

import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.TimeMachine;
import de.uni.freiburg.iig.telematik.sepia.traversal.PNTraverser;
import de.uni.freiburg.iig.telematik.sepia.traversal.RandomPNTraverser;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;

public class TimeActionListener implements ActionListener {

	long[] results;
	private int numberOfRuns = 50000;
	private int numberOfBins = 50;

	public TimeActionListener(int numberOfRuns) {
		this.numberOfRuns = numberOfRuns;
	}

	public TimeActionListener() {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		results = new long[numberOfRuns];
		for (int i = 0; i < results.length; i++) {
			results[i] = getOneSimulationRun();
		}

		System.out.println("Needed times:");
		for (long l : results)
			System.out.print(" " + l);
		generateDiagram(results, numberOfBins);
	}

	public long[] getSimulationResults() {
		return results;
	}

	private AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> getNet() {
		return ((PNEditor) Workbench.getInstance().getTabView().getSelectedComponent()).getNetContainer();
	}

	private TimeMachine<?, ?, ?, ?, ?, ?, ?> getTimeMachine(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> net) {
		return new TimeMachine(net.getPetriNet(), SwatComponents.getInstance().getTimeAnalysisForNet(net));
	}

	private PNTraverser<AbstractTransition<?, Object>> getTraverser(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> net){
		return new RandomPNTraverser((AbstractPetriNet<?, ?, ?, ?, ?, ?, ?>) net.getPetriNet());
	}

	private AbstractTransition<?, Object> chooseTransition(PNTraverser<AbstractTransition<?, Object>> traverser,
			AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> net) {
		return traverser.chooseNextTransition((List<AbstractTransition<?, Object>>) net.getPetriNet().getEnabledTransitions());
	}

	private long getOneSimulationRun() {
		long time = 0;
		AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> net = getNet();
		TimeMachine<?, ?, ?, ?, ?, ?, ?> timeMachine = getTimeMachine(net);
		PNTraverser<AbstractTransition<?, Object>> traverser = getTraverser(net);

		//do at least the first Run
		//do while enabled transitions available and timeMachine increases time
		boolean firstRun = true;
		while (firstRun || (timeMachine.incTime() && net.getPetriNet().hasEnabledTransitions())) {
			firstRun = false;
			AbstractTransition<?, Object> transition = chooseTransition(traverser, net);
			try {
				timeMachine.fire(transition.getName());
				time = timeMachine.getTime();
			} catch (PNException e1) {
				e1.printStackTrace();
			}
		}
		timeMachine.reset();
		return time;

	}

	private static void generateDiagram(long[] results2, int bins) {
		double[] buffer = new double[results2.length];
		for (int i = 0; i < results2.length; i++)
			buffer[i] = results2[i];
		// The histogram takes an array
		HistogramDataset histo = new HistogramDataset();
		histo.addSeries("Relative Occurence of Duration", buffer, bins);
		//histo.setType(HistogramType.RELATIVE_FREQUENCY);
		histo.setType(HistogramType.SCALE_AREA_TO_1);

		ApplicationFrame aFrame = new ApplicationFrame("Time analysis");
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
		panel.setPreferredSize(new java.awt.Dimension(800, 600));
		aFrame.setContentPane(panel);
		aFrame.setPreferredSize(new java.awt.Dimension(800, 600));
		aFrame.setSize(new Dimension(800, 600));
		aFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		aFrame.setVisible(true);
	}

}
