package de.uni.freiburg.iig.telematik.swat.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

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

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.AbstractDistributionView;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionViewFactory;

public class TimingTest {
	
	public static void main(String args[]) {
		AbstractDistributionView view = (AbstractDistributionView) DistributionViewFactory.getDistributionView(DistributionType.UNIFORM);
		view.setParams(100, 200);
		int size = 100000;
		long[] results = new long[size];
		for (int i = 0; i < size; i++) {
			results[i] = (long) view.getNeededTime();
		}
		generateDiagram(results, 50);

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
		panel.setPreferredSize(new java.awt.Dimension(800, 600));
		aFrame.setContentPane(panel);
		aFrame.setPreferredSize(new java.awt.Dimension(800, 600));
		aFrame.setSize(new Dimension(800, 600));
		aFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		aFrame.setVisible(true);
	}

}
