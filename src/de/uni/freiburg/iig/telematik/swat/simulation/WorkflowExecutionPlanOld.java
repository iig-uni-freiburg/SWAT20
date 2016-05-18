package de.uni.freiburg.iig.telematik.swat.simulation;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.FireElement;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.FireSequence;
import de.uni.freiburg.iig.telematik.swat.simon.AwesomeTimeContext;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class WorkflowExecutionPlanOld extends FireSequence {
	
	int simulatedRuns;
	int successes;
	LinkedList<Double> results = new LinkedList<>();
	private static AwesomeTimeContext context;
	
	static {
		String defContext;
		try {
			defContext = SwatProperties.getInstance().getActiveTimeContext();
			context = (AwesomeTimeContext) SwatComponents.getInstance().getTimeContextContainer().getComponent(defContext);
		} catch (IOException | ProjectComponentException e) {
			Workbench.errorMessage("Could not initialize timecontext", e, true);
		}
		
	}
	
	public WorkflowExecutionPlanOld(FireSequence sequence) {
		this.entries=sequence.getSequence();
		simulatedRuns=1;
		successes=0;
	}
	
	public void addSequence(FireSequence sequence){
		//compute performance and add to performance list
		String seq1=getTransitionString();
		String seq2=sequence.getTransitionString();
		if(sequence.getTransitionString().equals(getTransitionString())){
			simulatedRuns++;
			results.add(getOverallSuccessRatio(sequence));
			//refresh performance, etc...
			
		} else
			//throw new RuntimeException("This sequence does not comply with the given execution Plan");
			System.out.println("falsche Zuordnung");
		
	}
	
	public double getOverallPerformance(){
		//TimeContext
		//Liste mit bool-Werten, berechnen wie viel % erfolgt sindf
		//ausgabe
		return computeAverage(results);
	}
	
	private double getOverallSuccessRatio(FireSequence seq){
		HashMap<String, Double> nets = new HashMap<>(); //Net-Name, time
		for(FireElement element: seq.getSequence()){
			String netName=element.getTransition().getNet().getName();
			double time = element.getEndTime();
			if(nets.containsKey(netName)&&nets.get(netName)>time)
				System.out.println("Something wrong");
			nets.put(netName,element.getEndTime());
		}
		
		int success = 0;
		for(Entry<String, Double> entry:nets.entrySet()){
			if(entry.getValue()<=context.getDeadlineFor(entry.getKey()))
				success++;
		}
		
		return success/((double)nets.keySet().size());
	}
	
	private double computeAverage(List<Double> list){
		double sum = 0.0;
		for(double d:list)
			sum+=d;
		return sum/list.size();
	}
	
	/**compares durations of the FireSequence**/
	public int compareTo(Object o) {
		return new Double(getOverallPerformance()).compareTo(((WorkflowExecutionPlanOld) o).getOverallPerformance());
	}
	
	public String toString(){
		return getOverallPerformance()+": "+getTransitionString()+" ("+results.size()+") entries";
	}

}
