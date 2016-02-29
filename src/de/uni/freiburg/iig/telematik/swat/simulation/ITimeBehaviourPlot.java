package de.uni.freiburg.iig.telematik.swat.simulation;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.swat.simon.AbstractTimeBehaviour;
import de.uni.freiburg.iig.telematik.swat.simon.MeasuredTimeBehaviour;

public class ITimeBehaviourPlot {

	private ITimeBehaviour behaviour;
	private int bins = 100;
	boolean isMeasuredBehaviour = false;
	
	private JFrame frame = new JFrame();

	public ITimeBehaviourPlot(ITimeBehaviour behaviour) {
		this.behaviour = behaviour;
		if (behaviour instanceof MeasuredTimeBehaviour) {
			MeasuredTimeBehaviour MeasBehave = (MeasuredTimeBehaviour) this.behaviour;
			bins = MeasBehave.getMap().keySet().size();
			isMeasuredBehaviour = true;
		}
		frame.setContentPane(createPlotFrame());
	}

	public ITimeBehaviourPlot(ITimeBehaviour behaviour, int bins) {
		this(behaviour);
		this.bins = bins;

	}

	private ChartPanel createPlotFrame() {
		ApplicationFrame aFrame = new ApplicationFrame("Cumulative Histogram");
		XYSeries series = new XYSeries("Distribution");
		if (isMeasuredBehaviour) {
			MeasuredTimeBehaviour measBehave = (MeasuredTimeBehaviour) this.behaviour;
			for (Entry<Long, Double> entry : measBehave.getMap().entrySet()) {
				series.add(entry.getKey(), entry.getValue());
			}
		} else if (behaviour instanceof AbstractTimeBehaviour){
			AbstractTimeBehaviour abstractBehaviour = (AbstractTimeBehaviour) behaviour;
			double[] binValues = fillBins(abstractBehaviour);
			for (int i = 0; i < bins; i++) {
				series.add(binValues[i],abstractBehaviour.getCummulativeValueAt(binValues[i]));
			}
		}

		XYSeriesCollection data = new XYSeriesCollection(series);
		JFreeChart chart = ChartFactory.createXYLineChart(behaviour.toString(), "X", "Y", data, PlotOrientation.VERTICAL,
				true, true, false);

		ChartPanel chartPanel = new ChartPanel(chart);

		chartPanel.getChart().getXYPlot().getRenderer().setSeriesStroke(0, new BasicStroke(2.0f));

		return chartPanel;
	}
	
	private double[] fillBins(AbstractTimeBehaviour behaviour){
		int min = 0;
		int max = 1;
		double[] bins = new double[this.bins];
		bins[0]=0;
		while (true){
			System.out.println(behaviour.getCummulativeValueAt(max));
			if(behaviour.getCummulativeValueAt(max)>0.99)
				break;
			max+=5;
		}
		double step = (double)(max-min)/bins.length;
		System.out.println("Step: "+step);
		for (int i = 0;i<bins.length-1;i++)
			bins[i+1]=bins[i]+step;
		
		return bins;
	}
	
	public void setVisible(){
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setPreferredSize(new java.awt.Dimension(900, 600));
		frame.setSize(new Dimension(900, 600));
		frame.setVisible(true);
	}

}
