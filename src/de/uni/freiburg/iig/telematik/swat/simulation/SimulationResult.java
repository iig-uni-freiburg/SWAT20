package de.uni.freiburg.iig.telematik.swat.simulation;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartPanel;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.WorkflowTimeMachine;
import de.uni.freiburg.iig.telematik.swat.simon.AwesomeTimeContext;


public class SimulationResult extends JFrame {
	
	private static final long serialVersionUID = -4759318829955102188L;
	WorkflowTimeMachine wtm;
	private AwesomeTimeContext tc;
	
	public SimulationResult(WorkflowTimeMachine wtm, AwesomeTimeContext tc) {
		this.wtm = wtm;
		this.tc = tc;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(600, 500);
		setPreferredSize(new Dimension(600, 500));
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		for (String name: wtm.getResult().keySet()){
			getContentPane().add(getResult(name));
		}
	}
	
	private JPanel getResult(String netName){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JLabel label = new JLabel(netName+": "+getSuccessString(netName));
		label.setSize(new Dimension(70, 30));
		label.setPreferredSize(new Dimension(120, 30));
		panel.add(label);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(getHistogramm(netName));
		panel.add(Box.createHorizontalStrut(2));
		panel.add(getCummulativeChart(netName));
		return panel;
	}
	
	private double getDeadlineFor(String netName){
		double deadline = Double.NaN;
		if(tc.containsDeadlineFor(netName))
			deadline = tc.getDeadlineFor(netName);
		return deadline;
	}
	
	private double getSuccessRatio(String netName){
		double deadline = getDeadlineFor(netName);
		ArrayList<Double> result = wtm.getResult().get(netName);
		int size = result.size();
		int success = 0;
		for (double d:result)
			if (d<=deadline)
				success++;
		return (double)success/size;
	}
	
	private String getSuccessString(String netName){
		String s = "";
		double successRatio = getSuccessRatio(netName)*100;
		if(successRatio!=Double.NaN){
			s = new DecimalFormat("##.##").format(successRatio);
		}
		return s+"%";
	}
	
	private ChartPanel getHistogramm(String netName){
		SimulationHistogram histo = new SimulationHistogram(wtm.getResult().get(netName), 100, "title", "legend");
		ChartPanel panel = histo.getChart();
		panel.setPreferredSize(new Dimension(150, 75));
		return panel;
	}
	
	private ChartPanel getCummulativeChart(String netName){
		CumulativeHistrogram histo = new CumulativeHistrogram(wtm.getResult().get(netName), 100, "title", "legend");
		ChartPanel panel = histo.getChart();
		panel.setPreferredSize(new Dimension(150, 75));
		return panel;
	}

}
