package de.uni.freiburg.iig.telematik.swat.simulation.gui;

import java.awt.BasicStroke;
import java.awt.Container;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class CumulativeHistrogram {
	
	JFrame frame;
	ChartPanel chartPanel;
	
	public CumulativeHistrogram(List <Double> values, int bins, String title, String legend) {
		
		initFrame(title);

		
		double[] buffer = new double[values.size()];
		for (int i = 0; i < buffer.length; i++)
			buffer[i] = values.get(i);
		Arrays.sort(buffer);
		
		double[] bin = initializeBins(buffer, bins);
		
		double[] frequency = fillBins(buffer, bin);
		
		normalize(frequency);
		
		double[] cumulate = cumulate(frequency);
		
		frame.setContentPane(createPlotFrame(bin, cumulate, legend));
	}
	
	private void initFrame(String title){
		frame=new JFrame(title);
		frame.setPreferredSize(new java.awt.Dimension(900, 600));
		frame.setSize(new Dimension(900, 600));
		//aFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	public void setVisible(boolean visible){
		frame.setVisible(visible);
	}
	
	public ChartPanel getChart(){
		chartPanel.getChart().removeLegend();
		return chartPanel;
	}
	
	

	public double[] initializeBins(double[] occurences, int numberOfBins) {
		double[] bins = new double[numberOfBins];
		double min = occurences[0]; //value for first bin
		double max = occurences[occurences.length - 1]; //value for last bin
		double step = (max - min) / (((double) numberOfBins - 1)); //width of each bin
		bins[0] = (int) min;
		for (int i = 1; i < numberOfBins; i++) {
			bins[i] = (bins[i - 1] + step); //value for each bin
		}
		
		return bins;
	}
	
	/** make cummulative histogram. last entry will be one **/
	private double[] cumulate(double[] frequency) {
		double[] cummulated = new double[frequency.length];
		double sum = 0;
		for (int i = 0; i < frequency.length; i++) {
			sum += frequency[i];
			cummulated[i] = sum;
		}
		return cummulated;
	}
	
	/**
	 * create histogram out of occurences with given bins. occurences must be
	 * sorted
	 **/
	private double[] fillBins(double[] occurences, double[] bin) {
		double[] frequency = new double[bin.length];
		int startFrom = 0;
		//sweep through bins
		for (int i = 0; i < bin.length; i++) {
			int count = 0;
			while (startFrom < occurences.length) { //sweep through measeaured occurences
				if (occurences[startFrom] < bin[i]) {
					count++; //add occurence into current bin
					startFrom++; //go on with next occurence
				} else
					break; //bin full. Go to next bin
			}
			frequency[i] = count; //put into bin: number of matching occurences
		}
		return frequency;

	}
	
	/** normalize: divide frequency so they sum up to 1 **/
	private void normalize(double[] frequency) {
		double sum = 0.0;
		for (double d : frequency) {
			sum += d;
		}
		for (int i = 0; i < frequency.length; i++) {
			frequency[i] = frequency[i] / sum;
		}
	}
	
	private ChartPanel createPlotFrame(double[] bin, double[] cumulate, String legend){
		ApplicationFrame aFrame = new ApplicationFrame("Cumulative Results");
		XYSeries series = new XYSeries(legend);
		for(int i = 0;i<bin.length;i++){
			series.add(bin[i], cumulate[i]);
		}
		
         XYSeriesCollection data = new XYSeriesCollection(series);
         JFreeChart chart = ChartFactory.createXYLineChart(
            "Cumulative Results",
            "Duration", 
            "Cumulated Probability", 
            data,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
         
        chartPanel = new ChartPanel(chart);
        
        chartPanel.getChart().getXYPlot().getRenderer().setSeriesStroke(0, new BasicStroke(2.0f));
		
		return chartPanel;
	}

}
