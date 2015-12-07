package de.uni.freiburg.iig.telematik.swat.misc.plots;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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

public class SimulationHistogram {
	
	private JFrame frame;
	
	public SimulationHistogram(List<Double> values, int bins, String title, String legend){
		double[] buffer = new double[values.size()];
		for (int i = 0; i < buffer.length; i++)
			buffer[i] = values.get(i);
		// The histogram takes an array
		HistogramDataset histo = new HistogramDataset();
		histo.addSeries(legend, buffer, bins);
		histo.setType(HistogramType.RELATIVE_FREQUENCY);
		//histo.setType(HistogramType.SCALE_AREA_TO_1);

		frame = new JFrame(title);
		//ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		JFreeChart chart = ChartFactory.createHistogram("Distribution of simulated workflow duration",
				"Duration of Workflow execution in ms", "Relative occurence", histo, PlotOrientation.VERTICAL, true, true, false);

		// to save as JPG
		XYPlot plot = (XYPlot) chart.getPlot();
		XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);
		renderer.setSeriesOutlinePaint(0, Color.red);
		setFonts(plot);

		LegendTitle legendTitle = chart.getLegend();
		Font nwfont = new Font("Arial", 0, 26);
		legendTitle.setItemFont(nwfont);
		//chart.setLegend(legend);

		ChartPanel panel = new ChartPanel(chart);
		panel.setPreferredSize(new java.awt.Dimension(900, 600));
		frame.setContentPane(panel);
		frame.setPreferredSize(new java.awt.Dimension(900, 600));
		frame.setSize(new Dimension(900, 600));
		//aFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
	}
	
	public void setVisible(boolean visible){
		frame.setVisible(true);
	}

	private void setFonts(XYPlot plot) {
		//plot.getRangeAxis().setRange(0, 0.2);
		//plot.getDomainAxis().setRange(0, 30);
		plot.getRangeAxis().setTickLabelFont(new Font("Arial", 0, 30));
		plot.getDomainAxis().setTickLabelFont(new Font("Arial", 0, 30));
		plot.getRangeAxis().setLabelFont(new Font("Arial", 1, 28));
		plot.getDomainAxis().setLabelFont(new Font("Arial", 1, 28));
		//plot.getLegendItems().get(0).set(new Font("Arial", 1, 26));
		//plot.getLegendItems().get(1).setLabelFont(new Font("Arial", 1, 24));
		
	}

}
