package de.uni.freiburg.iig.telematik.swat.simulation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.WorkflowTimeMachine;
import de.uni.freiburg.iig.telematik.swat.simon.AwesomeTimeContext;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class ArchitectureResults {
	
	private WorkflowTimeMachine wtm;
	private AwesomeTimeContext tc;

	public ArchitectureResults(WorkflowTimeMachine wtm) throws ProjectComponentException, IOException {
		this.wtm=wtm;
		tc=(AwesomeTimeContext) SwatComponents.getInstance().getTimeContextContainer().getComponent(SwatProperties.getInstance().getActiveTimeContext());
	}
	
	public ArchitectureResults(WorkflowTimeMachine wtm, AwesomeTimeContext tc) {
		this.wtm=wtm;
		this.tc=tc;
	}
	
	public double getArchitectureResult(){
		double weight=getSumOfNetWeights();
		double result = 0;
		for(String net:wtm.getNets().keySet()){
			result+=getSuccessRatio(net)*(getWeightFor(net)/weight);
		}
		return result;
	}
	
	public double getDeadlineFor(String netName){
		double deadline = Double.NaN;
		if(tc.containsDeadlineFor(netName))
			deadline = tc.getDeadlineFor(netName);
		return deadline;
	}
	
	public double getSuccessRatio(String netName){
		double deadline = getDeadlineFor(netName);
		ArrayList<Double> result = wtm.getResult().get(netName);
		int size = result.size();
		int success = 0;
		for (double d:result)
			if (d<=deadline)
				success++;
		return (double)success/size;
	}
	
	public double getCost(String netName) {
		ArrayList<Double> results = wtm.getResult().get(netName);
		double cost = 0;
		double deadline = getDeadlineFor(netName);
		TimedNet net = wtm.getNets().get(netName);
		for(double result: results){
			if(result>deadline){ //net has not met deadline
				cost += deadline * net.getCostPerTimeUnit(); //cost to deadline
				cost += (result-deadline)*net.getCostPerTimeUnitAfterDeadline(); //costs after deadline
			} else { //net is within deadline
				cost += result*net.getCostPerTimeUnit();
			}
		}
		return cost/(double)results.size();
	}
	
	public double getWeightFor(String netName){
		return wtm.getNets().get(netName).getNetWeight();
	}
	
	private double getSumOfNetWeights(){
		double sum = 0;
		for (TimedNet net:wtm.getNets().values()){
			sum+=net.getNetWeight();
		}
		return sum;
	}

}
