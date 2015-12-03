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
import de.uni.freiburg.iig.telematik.swat.misc.plots.CumulativeHistrogram;
import de.uni.freiburg.iig.telematik.swat.misc.plots.SimulationHistogram;

public class SingleTimeSimulation {
	
	static Map<String,TimedNet> nets = new HashMap<>();

	public static void main(String args[]) {
		
		WorkflowTimeMachine timeMachine = WorkflowTimeMachine.getInstance();
		
		//timeMachine.addNet(TimedNetRep.getSimpleLinearTimedNet("linear-1",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		timeMachine.addNet(TimedNetRep.getSimpleANDTimedNet("and-1",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		timeMachine.addNet(TimedNetRep.getSimpleORTimedNet("or-1",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		timeMachine.addNet(TimedNetRep.getSimpleORTimedNet("or-2",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		timeMachine.addNet(TimedNetRep.getSimpleORTimedNet("or-3",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		//timeMachine.addNet(TimedNetRep.getSimpleORTimedNet("or-4",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		
		//timeMachine.addNet(TimedNetRep.getSimpleLinearTimedNet("linear-2",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		//timeMachine.addNet(TimedNetRep.getSimpleANDTimedNet("and-2",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		//timeMachine.addNet(TimedNetRep.getSimpleORTimedNet("or-2",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		
		//timeMachine.addNet(TimedNetRep.getSimpleLinearTimedNet("linear-3",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		//timeMachine.addNet(TimedNetRep.getSimpleANDTimedNet("and-3",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		//timeMachine.addNet(TimedNetRep.getSimpleORTimedNet("or-3",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		
		//timeMachine.addNet(TimedNetRep.getSimpleANDTimedNet("and-4",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		//timeMachine.addNet(TimedNetRep.getSimpleANDTimedNet("and-5",ContextRepo.getResourceContext(), ContextRepo.getTimeContext()));
		
		HashMap<String, ArrayList<Double>> result = timeMachine.simulateAll(12345);
		
		for(Entry<String, ArrayList<Double>> entry :result.entrySet()){
			//generateDiagram(entry.getValue(), 100, entry.getKey());
			new SimulationHistogram(entry.getValue(), 100, "Simulation results", entry.getKey()).setVisible(true);
			new CumulativeHistrogram(entry.getValue(), 100, "Simulation results", entry.getKey()).setVisible(true);
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

}
