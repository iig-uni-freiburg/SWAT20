package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.LinkedList;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.WorkflowTimeMachine;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.simulation.JaschaPlanExtractor;
import de.uni.freiburg.iig.telematik.swat.simulation.PlanExtractor;
import de.uni.freiburg.iig.telematik.swat.simulation.gui.PlanExtractorResult;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.RTPNEditorComponent;


public class StartOptimizationAction extends AbstractWorkbenchAction {

	public StartOptimizationAction(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public StartOptimizationAction() {
		this("Optimize");
		try {
			setIcon(IconFactory.getIcon("high_connection"));
		} catch (ParameterException | PropertyException | IOException e) {
		}
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		WorkflowTimeMachine wtm = WorkflowTimeMachine.getInstance();
		wtm.resetAll();
		wtm.clearAllNets();
		wtm.addAllNets(loadNets());
		wtm.resetAll();
//		wtm.simulateAll(SwatProperties.getInstance().getNumberOfSimulationsRuns());
//		PlanExtractor pe = new PlanExtractor();
		JaschaPlanExtractor jpe = new JaschaPlanExtractor();
		wtm.simulateAll(jpe.getNumberOfRuns());
		PlanExtractorResult result = new PlanExtractorResult(jpe);

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

}
