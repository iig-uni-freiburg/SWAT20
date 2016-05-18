package de.uni.freiburg.iig.telematik.swat.simulation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
	private TreeSet<WorkflowExecutionPlan> plans = new TreeSet<>();
	

	
	public static void main(String args[]) throws IOException, ParserException, PNException, ProjectComponentException {
		String net1String="invoiceIn";
		String net2String="invoiceOut";
		SwatComponents.getInstance();
		GraphicalTimedNet net1 = (GraphicalTimedNet) SwatComponents.getInstance().getContainerPetriNets().getComponent(net1String);
		GraphicalTimedNet net2 = (GraphicalTimedNet) SwatComponents.getInstance().getContainerPetriNets().getComponent(net2String);
		WorkflowTimeMachine wtm = WorkflowTimeMachine.getInstance();
		
		wtm.addNet(net1.getPetriNet());
		wtm.addNet( net2.getPetriNet());
		wtm.simulateAll(4321);

		//PlanExtractor ex = new PlanExtractor(WorkflowTimeMachine.getInstance(), StatisticListener.getInstance());
		PlanExtractor ex = new PlanExtractor();
		ArrayList<WorkflowExecutionPlan> set = ex.getExecutionPlan();
		wtm.simulateExecutionPlan(4000, set.get(set.size()-15).getSeq());
		new SimulationResult(wtm, getTimeContext()).setVisible(true);
		
		System.out.println("End");
		//System.exit(0);
	}
	
	
	public PlanExtractor() throws IOException, ProjectComponentException{
		this(WorkflowTimeMachine.getInstance(),StatisticListener.getInstance());
	}

	private PlanExtractor(WorkflowTimeMachine wtm, StatisticListener listener) throws IOException, ProjectComponentException {
		this.wtm=WorkflowTimeMachine.getInstance();
		this.listener = listener;
		this.fireSequence=getResultsFromNonClonedNets();
		this.context=getTimeContext();

		
//		for(Entry<String, ArrayList<FireSequence>> bla:fireSequence.entrySet()){
//			System.out.print("Net: "+bla.getKey());
//			System.out.println(" size "+bla.getValue().size()+" elements.");
//		}
		
		//HashSet<String> singlePlans = getUniqueWorkflowExecutions();
		
		
		
		plans = generatePlans();
		
		for(WorkflowExecutionPlan plan:plans)
			System.out.println(plan);
		
//		for(Entry<Double, FireSequence> entry:plan.entrySet()){
//			System.out.println(entry.getKey()+": "+entry.getValue().getTransitionString());
//		}
		
	}
	
	public ArrayList<WorkflowExecutionPlan> getExecutionPlan(){
		return new ArrayList<>(plans);
	}
	
	private HashSet<String> getUniqueWorkflowExecutions(){
		HashSet<String> singlePlans = new HashSet<>();
		for(FireSequence seq: listener.getOverallLog())
			singlePlans.add(seq.getTransitionString());
		
		System.out.println("number of plans: "+singlePlans.size());
		
		return singlePlans;
	}
	
	
	private static AwesomeTimeContext getTimeContext() throws IOException, ProjectComponentException {
		String context = SwatProperties.getInstance().getActiveTimeContext();
		return (AwesomeTimeContext) SwatComponents.getInstance().getTimeContextContainer().getComponent(context);
	}


	/**remove cloned nets from recurring instances**/
	private HashMap<String, ArrayList<FireSequence>> getResultsFromNonClonedNets(){
		Set<String> keep = wtm.getResult().keySet();
		HashMap<String, ArrayList<FireSequence>> result = new HashMap<>();
		for(String s:keep){
			result.put(s,listener.getFireSequences().get(s));
		}
		return result;
		
	}
	
	private TreeSet<WorkflowExecutionPlan> generatePlans(){
		
		//TreeMap<WorkflowExecutionPlan,WorkflowExecutionPlan> differentPlanSet = new TreeMap<>();
		
		HashMap<FireSequence, LinkedList<Double>> computedResults = new HashMap<>(); //store fire sequence and simulation results (performance)
		for(FireSequence seq: listener.getOverallLog()){
			//WorkflowExecutionPlan plan = new WorkflowExecutionPlan(seq);
			//differentPlanSet.put(plan,plan);
			if(!computedResults.containsKey(seq)){ 
				computedResults.put(seq,new LinkedList<Double>()); //create list
			}
		}
		
		for(FireSequence seq:listener.getOverallLog()) //add OverallPerformance
			computedResults.get(seq).add(getOverallSuccessRatio(seq));
		
//		for(FireSequence seq:listener.getOverallLog()){
//			WorkflowExecutionPlan plan = differentPlanSet.get(seq);
//			if(plan!=null)
//				plan.addSequence(seq);
//		}
//		
//		for(WorkflowExecutionPlan seq:differentPlanSet.keySet()){
//			System.out.println(seq);
//		}
		
//		TreeMap<Double, FireSequence> result = new TreeMap<>();
		TreeSet<WorkflowExecutionPlan> set = new TreeSet<>();
		
		for(Entry<FireSequence, LinkedList<Double>> entry:computedResults.entrySet()){
			//System.out.println(entry.getValue().size()+" entries for: "+entry.getKey().getTransitionString());
//			result.put(computeAverage(entry.getValue()),entry.getKey());
			set.add(new WorkflowExecutionPlan(entry.getKey(), entry.getValue().size(), computeAverage(entry.getValue())));
		}
		
//		for(WorkflowExecutionPlan plan:set){
//			System.out.println(plan.toString());
//		}
		
		//System.out.println("Number of different runnings: "+differentPlanSet.size()); //size is wrong
		
		return set;
		
	}
	
	/**compute the score of this workflow execution**/
	private double getOverallSuccessRatio(FireSequence seq){
		int success = 0;
		
		HashMap<String, Double> nets = extractSimulatedTiming(seq); //<NetName, needed time>
		
		//count successes
		for(Entry<String, Double> entry:nets.entrySet()){
			String netName=entry.getKey();
			double neededTime=entry.getValue();
			if(neededTime<=context.getDeadlineFor(netName))
				success++;
		}
		
		return ((double)success)/nets.keySet().size();
	}
	
	/**extract the time of the last fired activitiy from each net out of a fireSequence**/
	private HashMap<String, Double> extractSimulatedTiming(FireSequence seq){
		HashMap<String, Double> nets = new HashMap<>(); //Net-Name, time
		for(FireElement element: seq.getSequence()){ //get fired transitions
			String netName=element.getTransition().getNet().getName();
			double time = element.getEndTime();
			if(nets.containsKey(netName)&&nets.get(netName)>time)
				System.out.println("Something wrong");
			nets.put(netName,element.getEndTime());
		}
		return nets;
	}
	
	private double computeAverage(List<Double> list){
		double sum = 0.0;
		for(double d:list)
			sum+=d;
		return sum/list.size();
	}
}
