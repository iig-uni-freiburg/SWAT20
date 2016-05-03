package de.uni.freiburg.iig.telematik.swat.simulation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.invation.code.toval.parser.ParserException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalTimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.StatisticListener;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.FireElement;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.FireSequence;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.WorkflowTimeMachine;
import de.uni.freiburg.iig.telematik.swat.simon.AwesomeTimeContext;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class PlanExtractor {

	private WorkflowTimeMachine wtm;
	private StatisticListener listener;
	private HashMap<String, ArrayList<FireSequence>> fireSequence;
	private AwesomeTimeContext context;
	

	
	public static void main(String args[]) throws IOException, ParserException, PNException, ProjectComponentException {
		String net1String="invoiceIn";
		String net2String="invoiceOut";
		SwatComponents.getInstance();
		GraphicalTimedNet net1 = (GraphicalTimedNet) SwatComponents.getInstance().getContainerPetriNets().getComponent(net1String);
		GraphicalTimedNet net2 = (GraphicalTimedNet) SwatComponents.getInstance().getContainerPetriNets().getComponent(net2String);
		WorkflowTimeMachine wtm = WorkflowTimeMachine.getInstance();
		
		wtm.addNet(net1.getPetriNet());
		wtm.addNet( net2.getPetriNet());
		wtm.simulateAll(12345);

		PlanExtractor ex = new PlanExtractor(WorkflowTimeMachine.getInstance(), StatisticListener.getInstance());
		System.out.println("End");
		System.exit(0);
	}

	public PlanExtractor(WorkflowTimeMachine wtm, StatisticListener listener) throws IOException, ProjectComponentException {
		this.wtm=wtm;
		this.listener = listener;
		this.fireSequence=getUniqueNets();
		String context = SwatProperties.getInstance().getActiveTimeContext();
		this.context=(AwesomeTimeContext) SwatComponents.getInstance().getTimeContextContainer().getComponent(context);
		//this.fireSequence=listener.getFireSequences();
		for(Entry<String, ArrayList<FireSequence>> bla:fireSequence.entrySet()){
			System.out.print("Net: "+bla.getKey());
			System.out.println(" size "+bla.getValue().size()+" elements.");
		}
		
		HashSet<String> singlePlans = new HashSet<>();
		for(FireSequence seq: listener.getOverallLog())
			singlePlans.add(seq.getTransitionString());
		
		System.out.println("number of plans: "+singlePlans.size());
		
		generatePlans();
		

	}
	/**remove cloned nets from recurring instances**/
	private HashMap<String, ArrayList<FireSequence>> getUniqueNets(){
		Set<String> keep = wtm.getResult().keySet();
		HashMap<String, ArrayList<FireSequence>> result = new HashMap<>();
		for(String s:keep){
			result.put(s,listener.getFireSequences().get(s));
		}
		return result;
		
	}
	
	private void generatePlans(){
		HashSet<String> differentPlanSet = new HashSet<>();
		HashMap<FireSequence, LinkedList<Double>> performance = new HashMap<>();
		for(FireSequence seq: listener.getOverallLog()){
			differentPlanSet.add(seq.getTransitionString());
			if(!performance.containsKey(seq)){ //create list
				performance.put(seq,new LinkedList<Double>());
			}
			
			//add OverallPerformance
			performance.get(seq).add(getOverallSuccessRatio(seq));
		}
		
		for(Entry<FireSequence, LinkedList<Double>> entry:performance.entrySet()){
			System.out.println(entry.getKey().getTransitionString()+": "+computeAverage(entry.getValue())); //sort this
		}
		
		System.out.println("Number of different runnings: "+differentPlanSet.size());
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
}
