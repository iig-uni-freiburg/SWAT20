package de.uni.freiburg.iig.telematik.swat.simulation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Scanner;
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
import de.uni.freiburg.iig.telematik.swat.workbench.action.SimulateTimeAction;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class PlanExtractor {

	private WorkflowTimeMachine wtm;
	private StatisticListener listener;
	//private HashMap<String, ArrayList<FireSequence>> fireSequence;
	private AwesomeTimeContext context;
	private TreeSet<WorkflowExecutionPlan> plans = new TreeSet<>();
	

	
	public static void main(String args[]) throws IOException, ParserException, PNException, ProjectComponentException {
		//String net1String="Drill_Hole";
		//String net2String="Drill_Hole1";
		String net1String="invoiceIn";
		String net2String="invoiceOut";
		SwatComponents.getInstance();
		GraphicalTimedNet net1 = (GraphicalTimedNet) SwatComponents.getInstance().getContainerPetriNets().getComponent(net1String);
		GraphicalTimedNet net2 = (GraphicalTimedNet) SwatComponents.getInstance().getContainerPetriNets().getComponent(net2String);
		WorkflowTimeMachine wtm = WorkflowTimeMachine.getInstance();
		
		wtm.addNet(net1.getPetriNet());
		wtm.addNet(net2.getPetriNet());
		wtm.simulateAll(54321);

		PlanExtractor ex = new PlanExtractor(WorkflowTimeMachine.getInstance(), StatisticListener.getInstance());
		//PlanExtractor ex = new PlanExtractor();
		ArrayList<WorkflowExecutionPlan> set = ex.getExecutionPlan();
		
		ex.printResults(set);
		
		while (true){
			Scanner keyboard = new Scanner(System.in);
			System.out.println("Enter index to run further simulation. Enter 99 to start simple optimization");
			int myint = keyboard.nextInt();
			wtm.resetAll();			

			if(myint == 99) { //input code to do this simulation
				ex.getBestResultFromTop10(set, wtm, ex);
				// Another basic approach for combination of results
				//ex.simulateTopTen(set, wtm);
			}
			else {
				wtm.simulateExecutionPlan(8000, set.get(myint).getSeq());
				ex.printResults(set);
				System.out.println("Above are the initial simulation results!");
				new SimulationResult(wtm, getTimeContext()).setVisible(true);
			}			

		}
		//System.out.println("End");
		//System.exit(0);
	}
	
	private void getBestResultFromTop10 (ArrayList<WorkflowExecutionPlan> set, WorkflowTimeMachine wtm, PlanExtractor ex) throws PNException{		
		int resultSetSize = set.size()-2; //remove broken result
		int optimizationRuns = 0;
		int currentIndex;
		WorkflowExecutionPlan bestPlan = null;
		Double bestPerformance;
		if (set.get(resultSetSize).getPerformance() == 1.0 && set.get(resultSetSize).getNumberOfRuns() < 5){
			resultSetSize --; //can't get better than 100%, ignore such results if there were very few runs
		}
		currentIndex = resultSetSize;
		bestPerformance = set.get(currentIndex).getPerformance();
		Double bestSimulationPerformance = 0.0;
		while (optimizationRuns < 10){
			plans.clear();
			wtm.simulateExecutionPlan(2500, set.get(currentIndex).getSeq());
			//System.out.print(wtm.getResult().toString());
			ArrayList<WorkflowExecutionPlan> simulationSet = ex.getExecutionPlan();
			ex.printResults(simulationSet);
			Double thisRunsPerformance = simulationSet.get(0).getPerformance();
			if (thisRunsPerformance > bestSimulationPerformance){
				bestSimulationPerformance = thisRunsPerformance;
				bestPlan = simulationSet.get(0);
			}
			if (thisRunsPerformance > bestPerformance){
				bestPerformance = thisRunsPerformance;
				System.out.println("We improved the former best result in " + optimizationRuns + " optimization runs.");
				return;				
			}
			else {
				optimizationRuns ++;
				currentIndex --;
			}

		}
		System.out.println("The best performance achieved in this simulation was: " + bestSimulationPerformance);
		System.out.println("With this plan: " + bestPlan.toString());			
		
	}
	
	// Takes 5 random sequences out of the top10 sequences form the first simulation run
	private void simulateTopTen(ArrayList<WorkflowExecutionPlan> set, WorkflowTimeMachine wtm) throws PNException, IOException, ProjectComponentException {
		ArrayList<WorkflowExecutionPlan> top10 = new ArrayList<>();
		System.out.println("Set Size = " + set.size());
		if(set.size() >= 11){
			// the last entry is always broken --> ignore it
			for (int i = set.size()-2; i > set.size()-12; i--) {
				System.out.println("Loop i = " + i);
				top10.add(set.get(i));
			}
		}
		else {
			System.out.println("Need more than 11 Results to get top 10. Issue not handled yet");
			return;
		}
		int[] intArray = new int[5];
		for (int i = 0; i < 5; i++){
			intArray[i] = ThreadLocalRandom.current().nextInt(top10.size()-1);
		}
		for (int i = 0; i < 5; i++){
			System.out.println(top10.get(intArray[i]));
			wtm.simulateExecutionPlan(1250, top10.get(intArray[i]).getSeq());
			new SimulationResult(wtm, getTimeContext()).setVisible(true);
		}
	}
	
	
	private void printResults (ArrayList<WorkflowExecutionPlan> set){
		int i = 0;
		for (WorkflowExecutionPlan plan:set){
			System.out.println(i+": "+plan);
			i++;
			}
	}
	
	
	public PlanExtractor() throws IOException, ProjectComponentException{
		this(WorkflowTimeMachine.getInstance(),StatisticListener.getInstance());
	}

	private PlanExtractor(WorkflowTimeMachine wtm, StatisticListener listener) throws IOException, ProjectComponentException {
		this.wtm=WorkflowTimeMachine.getInstance();
		this.listener = listener;
		//this.fireSequence=getResultsFromNonClonedNets();
		this.context=getTimeContext();
		
		//plans = generatePlans();
	}
	
	/**gets (and if needed generates) the orderd execution plan**/
	public ArrayList<WorkflowExecutionPlan> getExecutionPlan(){
		if(plans==null||plans.isEmpty()){
			plans=generatePlans();
		}
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
	
	/**compute the score of this workflow execution between 0..1**/
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
		double result = ((double)success)/nets.keySet().size();
		if(result>1.0)
			System.out.println("This is not possible!");
		
		return result;
		//if (success==nets.keySet().size()) return 1;
		//return 0;
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
		double result = sum/(double)list.size();
		return result;
	}
}
