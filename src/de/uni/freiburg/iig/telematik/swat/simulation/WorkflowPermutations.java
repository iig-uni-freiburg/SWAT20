package de.uni.freiburg.iig.telematik.swat.simulation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalTimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.FireElement;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.WorkflowTimeMachine;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;

public class WorkflowPermutations {
	
	private Collection<TimedNet> nets = new ArrayList<>();
	private WorkflowExecutionPlan plan;
	
	public static void main (String[] args) throws ProjectComponentException, PNException, IOException {

		WorkflowTimeMachine wtm = WorkflowTimeMachine.getInstance();
		ArrayList<TimedNet> exampleNets = createExampleNets();
		wtm.addAllNets(exampleNets);
		wtm.simulateAll(100);
		PlanExtractor ex = new PlanExtractor();
		ArrayList<WorkflowExecutionPlan> plans = ex.getExecutionPlan();
		WorkflowPermutations factory = new WorkflowPermutations(exampleNets, plans.get(1));
		WorkflowExecutionPlan plan1 = plans.get(0);
		System.out.println(plan1);
		List<AbstractTimedTransition> result = factory.getPermutation(1);
		System.out.println("-------");
		for (AbstractTimedTransition t:result){
			System.out.println(t.getLabel());
		}
	}
	
	private static ArrayList<TimedNet> createExampleNets() throws ProjectComponentException{
		String net1String="invoiceIn";
		String net2String="invoiceOut";
		SwatComponents component = SwatComponents.getInstance();
		GraphicalTimedNet net1 = (GraphicalTimedNet) component.getContainerPetriNets().getComponent(net1String);
		GraphicalTimedNet net2 = (GraphicalTimedNet) component.getContainerPetriNets().getComponent(net2String);
		ArrayList<TimedNet> netList = new ArrayList<>();
		netList.add(net1.getPetriNet());
		netList.add(net2.getPetriNet());
		return netList;
	}
	
	public WorkflowPermutations(Collection<TimedNet> nets, WorkflowExecutionPlan plan){
		this.nets=nets;
		this.plan=plan;
	}
	
	public WorkflowPermutations(WorkflowExecutionPlan plan) {
		this(WorkflowTimeMachine.getInstance().getNets().values(),plan);
	}
	
	public List<AbstractTimedTransition> getPermutationFor(AbstractTimedTransition t){
		ArrayList<AbstractTimedTransition> result = new ArrayList<>();
		List<AbstractTimedFlowRelation> incoming = t.getIncomingRelations(); //outgoing flows
		for(AbstractTimedFlowRelation flow:incoming) {
			AbstractPlace place= flow.getPlace(); //connected places
			List<AbstractTimedFlowRelation> outgoing = place.getOutgoingRelations(); //outgoing flows from places
			for (AbstractTimedFlowRelation rel2:outgoing){
				result.add((AbstractTimedTransition) rel2.getTransition()); //transitions from outgoing place flows
			}
			
		}
		result.remove(t);
		return result;
	}
	
	public List<AbstractTimedTransition> getPermutation(int index){
		FireElement element = plan.getSeq().getSequence().get(index);
		AbstractTimedTransition t = element.getTransition(); //Transition at index
		return getPermutationFor(t);
		
	}
	

}