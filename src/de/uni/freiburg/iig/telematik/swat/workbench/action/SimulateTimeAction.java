package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.WorkflowTimeMachine;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.simon.AwesomeTimeContext;
import de.uni.freiburg.iig.telematik.swat.simulation.CumulativeHistrogram;
import de.uni.freiburg.iig.telematik.swat.simulation.SimulationHistogram;
import de.uni.freiburg.iig.telematik.swat.simulation.SimulationResult;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;
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
			timeMachine.resetAll();
			timeMachine.clearAllNets();
			timeMachine.addAllNets(nets);
			result = timeMachine.simulateAll(numberOfRuns);
			//displayResults();
			String defTimeContext = SwatProperties.getInstance().getActiveTimeContext();
			new SimulationResult(timeMachine, (AwesomeTimeContext) SwatComponents.getInstance().getTimeContextContainer().getComponent(defTimeContext)).setVisible(true);
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
			ArrayList<Double> values = entry.getValue();
			String name = entry.getKey();
			
			new SimulationHistogram(values, 100, "Simulation results "+ratioString(entry), name).setVisible(true);
			new CumulativeHistrogram(values, 100, "Simulation results "+ratioString(entry), name).setVisible(true);
		}
	}
	
	protected String ratioString(Entry<String, ArrayList<Double>> entry) {
		String s="";
		try {
			AwesomeTimeContext context = (AwesomeTimeContext) SwatComponents.getInstance().getTimeContextContainer().getComponent(SwatProperties.getInstance().getActiveTimeContext());
			if (!context.containsDeadlineFor(entry.getKey()))
				return s;
			
			s = "Success-Ratio: "+new DecimalFormat("##.##").format(getDeadlineRatio(entry));
		} catch (ProjectComponentException | IOException | NullPointerException e) {
			Workbench.errorMessage("Could not retrieve deadline for net "+entry.getKey(), e, false);
		}
		
		return s;
	}
	
	protected double getDeadlineRatio(Entry<String, ArrayList<Double>> entry) throws ProjectComponentException, IOException, NullPointerException{

			AwesomeTimeContext context = (AwesomeTimeContext) SwatComponents.getInstance().getTimeContextContainer().getComponent(SwatProperties.getInstance().getActiveTimeContext());
			
			if(!context.containsDeadlineFor(entry.getKey()))
				return 1;
			
			double deadline = context.getDeadlineFor(entry.getKey());
			int i = 0;
			for (double d:entry.getValue()){
				if (d<=deadline) i++;
			}
			
			double ratio = (double)i/entry.getValue().size();
			return ratio;
			
	}

}
