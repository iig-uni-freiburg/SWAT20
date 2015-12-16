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
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.StatisticListener;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.WorkflowTimeMachine;
import de.uni.freiburg.iig.telematik.swat.misc.plots.CumulativeHistrogram;
import de.uni.freiburg.iig.telematik.swat.misc.plots.SimulationHistogram;

public class SingleTimeSimulation {
	
	static Map<String,TimedNet> nets = new HashMap<>();

	public static void main(String args[]) {
		
		WorkflowTimeMachine timeMachine = WorkflowTimeMachine.getInstance();
		
		timeMachine.addNet(TimedNetRep.getSimpleLinearTimedNet("linear-1",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		timeMachine.addNet(TimedNetRep.getSimpleANDTimedNet("and-1",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		timeMachine.addNet(TimedNetRep.getSimpleORTimedNet("or-1",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		timeMachine.addNet(TimedNetRep.getSimpleORTimedNet("or-2",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		timeMachine.addNet(TimedNetRep.getSimpleORTimedNet("or-3",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		timeMachine.addNet(TimedNetRep.getSimpleORTimedNet("or-4",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		timeMachine.addNet(TimedNetRep.getSimpleORTimedNet("or-5",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		timeMachine.addNet(TimedNetRep.getSimpleORTimedNet("or-6",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		
		timeMachine.addNet(TimedNetRep.getSimpleLinearTimedNet("linear-2",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		timeMachine.addNet(TimedNetRep.getSimpleANDTimedNet("and-2",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		timeMachine.addNet(TimedNetRep.getSimpleORTimedNet("or-2",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		
		timeMachine.addNet(TimedNetRep.getSimpleLinearTimedNet("linear-3",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		timeMachine.addNet(TimedNetRep.getSimpleANDTimedNet("and-3",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		timeMachine.addNet(TimedNetRep.getSimpleORTimedNet("or-3",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		
		timeMachine.addNet(TimedNetRep.getSimpleANDTimedNet("and-4",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		timeMachine.addNet(TimedNetRep.getSimpleANDTimedNet("and-5",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		
		timeMachine.addNet(TimedNetRep.getComplexNet("complex-1", ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		timeMachine.addNet(TimedNetRep.getComplexNet("complex-2", ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		timeMachine.addNet(TimedNetRep.getComplexNet("complex-3", ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		timeMachine.addNet(TimedNetRep.getComplexNet("complex-4", ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		
		HashMap<String, ArrayList<Double>> result = timeMachine.simulateAll(12345);
		StatisticListener listener=StatisticListener.getInstance();
		
		new SimulationHistogram(result.get("linear-1"), 100, "simulation results", "linear-1").setVisible(true);
		new SimulationHistogram(result.get("and-1"), 100, "simulation results", "and-1").setVisible(true);
		new SimulationHistogram(result.get("or-1"), 100, "simulation results", "or-1").setVisible(true);
		new SimulationHistogram(result.get("complex-1"), 100, "simulation results", "complex-1").setVisible(true);
		
		new CumulativeHistrogram(result.get("linear-1"), 100, "simulation results", "linear-1").setVisible(true);
		
//		for(Entry<String, ArrayList<Double>> entry :result.entrySet()){
//			//generateDiagram(entry.getValue(), 100, entry.getKey());
//			new SimulationHistogram(entry.getValue(), 100, "Simulation results", entry.getKey()).setVisible(true);
//			new CumulativeHistrogram(entry.getValue(), 100, "Simulation results", entry.getKey()).setVisible(true);
//		}
	}

}
