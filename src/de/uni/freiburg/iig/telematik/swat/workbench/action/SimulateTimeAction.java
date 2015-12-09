package de.uni.freiburg.iig.telematik.swat.workbench.action;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.InconsistencyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.event.TokenEvent;
import de.uni.freiburg.iig.telematik.sepia.event.TokenListener;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.PNTimeContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.TimeMachine;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.WorkflowTimeMachine;
import de.uni.freiburg.iig.telematik.sepia.traversal.PNTraverser;
import de.uni.freiburg.iig.telematik.sepia.traversal.RandomPNTraverser;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.misc.plots.CumulativeHistrogram;
import de.uni.freiburg.iig.telematik.swat.misc.plots.SimulationHistogram;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.RTPNEditorComponent;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SimulateTimeAction extends AbstractWorkbenchAction {

	private static final long serialVersionUID = 1729386246000057281L;

	private int numberOfRuns = 50000;
	private int numberOfBins = 100;

	private boolean drainPlaceReached = false;

	private HashMap<String, ArrayList<Double>> result;

	public SimulateTimeAction(int numberOfRuns) {
		super("");
		this.numberOfRuns = numberOfRuns;
		setTooltip("Simulate Timing");
		try {
			setIcon(IconFactory.getIcon("time"));
		} catch (ParameterException e) {
			setText("simulate Timing");
			e.printStackTrace();
		} catch (PropertyException e) {
			setText("simulate Timing");
			e.printStackTrace();
		} catch (IOException e) {
			setText("simulate Timing");
			e.printStackTrace();
		}
	}

	public SimulateTimeAction() {
		this(12345);
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		//Get all TimedNets from Workbench
		LinkedList<TimedNet> nets = new LinkedList<>();
		int length = SwatTabView.getInstance().getTabCount();
		for(int i = 0;i<length;i++){
			Object o = SwatTabView.getInstance().getTabComponentAt(i);
			if(o instanceof RTPNEditorComponent){
				RTPNEditorComponent component = (RTPNEditorComponent) o;
				nets.add(component.getNetContainer().getPetriNet());
			}
		}
		if(!nets.isEmpty()){
			WorkflowTimeMachine timeMachine = WorkflowTimeMachine.getInstance();
			timeMachine.addAllNets(nets);
			result = timeMachine.simulateAll(numberOfRuns);
			displayResults();
		}
		
	}
	
	protected void displayResults(){
		for(Entry<String, ArrayList<Double>> entry :result.entrySet()){
			new SimulationHistogram(entry.getValue(), 100, "Simulation results", entry.getKey()).setVisible(true);
			new CumulativeHistrogram(entry.getValue(), 100, "Simulation results", entry.getKey()).setVisible(true);
		}
	}

}
