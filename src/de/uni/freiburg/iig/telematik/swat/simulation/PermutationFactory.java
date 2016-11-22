package de.uni.freiburg.iig.telematik.swat.simulation;

import java.util.ArrayList;
import java.util.List;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;

public class PermutationFactory {
	
	private ArrayList<TimedNet> nets = new ArrayList<>();
	private WorkflowExecutionPlan plan;
	
	public PermutationFactory(ArrayList<TimedNet> nets, WorkflowExecutionPlan plan){
		this.nets=nets;
		this.plan=plan;
	}
	
	public List<String> getPermutation(int index){
		return null;
	}
	

}
