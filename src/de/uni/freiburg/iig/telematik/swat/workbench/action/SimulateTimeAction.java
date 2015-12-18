package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.WorkflowTimeMachine;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.misc.plots.CumulativeHistrogram;
import de.uni.freiburg.iig.telematik.swat.misc.plots.SimulationHistogram;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.RTPNEditorComponent;

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
		LinkedList<TimedNet> nets = loadNets();

		if(!nets.isEmpty()){
			WorkflowTimeMachine timeMachine = WorkflowTimeMachine.getInstance();
			timeMachine.addAllNets(nets);
			result = timeMachine.simulateAll(numberOfRuns);
			displayResults();
		}
		
	}
	
	private LinkedList<TimedNet> loadNets() throws Exception {
		LinkedList<TimedNet> nets = new LinkedList<>();
		int length = SwatTabView.getInstance().getTabCount();
		for(int i = 0;i<length;i++){
			Object o = SwatTabView.getInstance().getComponentAt(i);
			if(o instanceof RTPNEditorComponent){
				RTPNEditorComponent component = (RTPNEditorComponent) o;
				nets.add(component.getNetContainer().getPetriNet());
			}
		}
		return nets;
	}

	protected void displayResults(){
		for(Entry<String, ArrayList<Double>> entry :result.entrySet()){
			new SimulationHistogram(entry.getValue(), 100, "Simulation results", entry.getKey()).setVisible(true);
			new CumulativeHistrogram(entry.getValue(), 100, "Simulation results", entry.getKey()).setVisible(true);
		}
	}

}
