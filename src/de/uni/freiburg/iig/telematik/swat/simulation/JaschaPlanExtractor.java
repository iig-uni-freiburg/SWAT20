package de.uni.freiburg.iig.telematik.swat.simulation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

import com.itextpdf.text.log.SysoCounter;

import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.invation.code.toval.parser.ParserException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalTimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.StatisticListener;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.FireElement;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.FireSequence;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.WorkflowTimeMachine;
import de.uni.freiburg.iig.telematik.swat.jascha.OptimizationResult;
import de.uni.freiburg.iig.telematik.swat.simon.AwesomeTimeContext;
import de.uni.freiburg.iig.telematik.swat.simulation.gui.SimulationResult;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SimulateTimeAction;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class JaschaPlanExtractor {

	private WorkflowTimeMachine wtm;
	private StatisticListener listener;
	//private HashMap<String, ArrayList<FireSequence>> fireSequence;
	private AwesomeTimeContext context;
	private Set<WorkflowExecutionPlan> plans;
	private HashMap<FireSequence, LinkedList<Double>> endingTimesMap;
	private static ArrayList<WorkflowExecutionPlan> currentSet = null;
	private static int numberOfRuns = 2000;
	private ArchitectureResults ar;
	private List<OptimizationResult> optimizationResults = new ArrayList<OptimizationResult>();
	

	
	public static void main(String args[]) throws IOException, ParserException, PNException, ProjectComponentException {
		String net1String="Tiefbau";
		String net2String="Strassenlaterne";
		String net3String="Fundament";
		//String net4String="Sisyphos";
		//String net5String="Strassenbau";
		//String net6String="Abriss";
		//String net1String="Multinom1";
		//String net2String="Multinom2";
		//String net3String="Multinom3";
		
		//String net1String="invoiceIn";
		//String net2String="invoiceOut";
		SwatComponents.getInstance();
		GraphicalTimedNet net1 = (GraphicalTimedNet) SwatComponents.getInstance().getContainerPetriNets().getComponent(net1String);
		GraphicalTimedNet net2 = (GraphicalTimedNet) SwatComponents.getInstance().getContainerPetriNets().getComponent(net2String);
		GraphicalTimedNet net3 = (GraphicalTimedNet) SwatComponents.getInstance().getContainerPetriNets().getComponent(net3String);
		//GraphicalTimedNet net4 = (GraphicalTimedNet) SwatComponents.getInstance().getContainerPetriNets().getComponent(net4String);
		//GraphicalTimedNet net5 = (GraphicalTimedNet) SwatComponents.getInstance().getContainerPetriNets().getComponent(net5String);
		//GraphicalTimedNet net6 = (GraphicalTimedNet) SwatComponents.getInstance().getContainerPetriNets().getComponent(net6String);
		WorkflowTimeMachine wtm = WorkflowTimeMachine.getInstance();
		
		wtm.addNet(net1.getPetriNet());
		wtm.addNet(net2.getPetriNet());
		wtm.addNet(net3.getPetriNet());
		//wtm.addNet(net4.getPetriNet());
		//wtm.addNet(net5.getPetriNet());
		//wtm.addNet(net6.getPetriNet());
		wtm.simulateAll(numberOfRuns);

		JaschaPlanExtractor ex = new JaschaPlanExtractor(WorkflowTimeMachine.getInstance(), StatisticListener.getInstance());
		ArrayList<WorkflowExecutionPlan> set = ex.getExecutionPlan();
		ex.optimizationResults.add(new OptimizationResult(new FireSequence(), set));
		
		//Reverse list for easier working with it
		Collections.reverse(set);
		
		ex.printResults(set);
		ex.getOverallFitness(set);
		
		while (true){
			Scanner keyboard = new Scanner(System.in);
			System.out.println("Enter index (0 equals best result) to run further simulation. Enter 96, 97, 98 or 99 to start simple optimization");
			int myint = keyboard.nextInt();
			wtm.resetAll();			
			switch (myint) {
			case 99:				
				ex.getBestResultFromTop10(set, wtm);
				break;
				
			case 98:				
				// Another basic approach for combination of results
				ex.simulateTopPercent(set, wtm, 10, numberOfRuns); //top% to be simulated and number of runs
				break;
				
			case 97:				
				ex.simulateMultipleSequences(set, wtm);			
				break;	
				
			case 96:
				ArrayList<WorkflowExecutionPlan> reductionResult = new ArrayList<WorkflowExecutionPlan>();
				reductionResult = ex.simulateReduction(set, wtm, 5, numberOfRuns);
				System.out.println("Do you want to find the best (shortest) ending times? 0 for yes, 1 for no, 2 for OptimzationResults");
				int inputInt = keyboard.nextInt();
				if (inputInt == 0){
					// get best ending times from result
					ex.getEndingTimesFromWEP(reductionResult);
				} else 
				if (inputInt == 2){
					System.out.println("optimizationResults size="+ex.optimizationResults.size());
					for (OptimizationResult or:ex.optimizationResults){
						System.out.println(or.toString());
					}
				}
				break;
				
			case 666:
				//Idea: Print best ending time for Top15 Results				
				int maxIndex = 15;
				if (set.size()<15) 
					maxIndex=set.size();
				for (int i=0; i<maxIndex; i++){
					System.out.println("The ending times of the run with a performance of "+ set.get(i).getPerformance() + " are:\n"+set.get(i).getEndingTimes() + "\n");
				}
				break;			
				
			default:
				wtm.simulateExecutionPlan(8000, set.get(myint).getSeq());				
				ex.printResults(set);
				System.out.println("Above are the initial simulation results!");
				System.out.println("The simulated plan was:" + set.get(myint).getSeq());
				new SimulationResult(wtm, getTimeContext(),false).setVisible(true);
				break;
			}
		}
		//System.out.println("End");
		//System.exit(0);
	}
	
	// relies on ending times being unique - which they usually are
	private void getEndingTimesFromWEP(ArrayList<WorkflowExecutionPlan> set) {
		HashMap<Double, String> map = new HashMap<Double, String>();
		List<Double> resultList = new ArrayList<Double>();
		for (WorkflowExecutionPlan wep: set){
			double d = wep.getEndingTimes().get(0);
			map.put(d, wep.getSeq().toString());
			resultList.add(d);
		}
		Collections.sort(resultList);
		System.out.println("The shortest ending times were:");
		for (int i=0; i < 20; i++){
			System.out.println(i+". "+ resultList.get(i) + " for Sequence "+ map.get(resultList.get(i)).toString());			
		}		
	}

	private ArrayList<WorkflowExecutionPlan> simulateReduction(ArrayList<WorkflowExecutionPlan> set, WorkflowTimeMachine wtm, int p, int runs) {
		//clear the list with the results of former optimizations
		//optimizationResults.clear();
		//create copy of original set:
		ArrayList<WorkflowExecutionPlan> intermediateList = new ArrayList<>(set);
		ArrayList<WorkflowExecutionPlan> newList = new ArrayList<WorkflowExecutionPlan>();
		int goalSize = 5;
		int size = set.size();
		int round = 0;
		int counter = 0;
		//Idee: reduziere Ergebnismenge auf weniger als 10 oder stoppe nach 10 Runden oder stoppe wenn Ergebnis nicht weiter reduziert wird
		while (size >= goalSize && round < 20){
			round++;
			newList.clear();
			try {
				newList = simulateTopPercent(intermediateList, wtm, p, runs);
				intermediateList.clear();
				intermediateList.addAll(newList);
				
				if (newList.size() >= (double)size*0.90){ //Reduktion auf mindestens 90%, sonst counter++
					counter++;					
					//wenn das neue Ergebnis 3 Mal kaum kleiner oder sogar größer wurde, Abbruch
					if (counter >=3){
						System.out.println("Breaking because the counter reached 3");
						break;
					}
				}
				size = newList.size();
				System.out.println("Round "+round+": Result size = "+size);
				System.out.println("Counter is at "+counter);
				
			} catch (PNException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ProjectComponentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		System.out.println("simulateReduction: size = "+size+" and round = "+round);
		for (WorkflowExecutionPlan wep: intermediateList){
			
		}
		printResults(intermediateList);
		return intermediateList;
		
		
	}
	// not working!
	private void simulateMultipleSequences(ArrayList<WorkflowExecutionPlan> set, WorkflowTimeMachine wtm) throws PNException {
			ArrayList<FireSequence> sequences = new ArrayList<FireSequence>();
			int runs = 100;
			int random = 0;
			// add top 5 and also add 5 random (that are not Top5)
			// change set.size()-6 to any number to get different Tops, e.g. set.size()-11 for Top10
			for (int i = set.size()-1; i > set.size()-6; i--){
				sequences.add(set.get(i).getSeq());
				random = ThreadLocalRandom.current().nextInt(set.size()-6);
				sequences.add(set.get(random).getSeq());
			}			
			plans.clear();	
			System.out.println("Starting simulation of multiple sequences");
			// requires changes in SEPIA
			//wtm.simulateMultipleSequences(sequences, runs);
			System.out.println("Simulation ended successfully");
			ArrayList<WorkflowExecutionPlan> executionPlans = getExecutionPlan();
			getOverallFitness(executionPlans);
			printResults(executionPlans);
			currentSet = executionPlans;
		
		
		
	}

	// Simple simulation that takes the ten best results and runs each 5000 times to see which one is the best
	private void getBestResultFromTop10 (ArrayList<WorkflowExecutionPlan> set, WorkflowTimeMachine wtm) throws PNException{		
		int resultSetSize = set.size()-1; //remove broken result
		int optimizationRuns = 0;
		int currentIndex;
		WorkflowExecutionPlan bestPlan = null;
		Double bestPerformance;
		currentIndex = resultSetSize;
		bestPerformance = set.get(currentIndex).getPerformance();
		System.out.println("bestPerformance = "+bestPerformance);
		Double bestSimulationPerformance = 0.0;
		Double thisRunsPerformance;
		ArrayList<WorkflowExecutionPlan> simulationSet;
		while (optimizationRuns < 10){
			plans.clear();
			wtm.simulateExecutionPlan(500, set.get(currentIndex).getSeq());
			//System.out.print(wtm.getResult().toString());
			simulationSet = getExecutionPlan();
			//ex.printResults(simulationSet);
			printResults(simulationSet);
			thisRunsPerformance = simulationSet.get(0).getPerformance();
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
	
	// Take top 10% results and simulate them again --> greedy simulation leads to no significant reduction 
	// in the number sequences if many nets are simulated.
	private ArrayList<WorkflowExecutionPlan> simulateTopPercent(ArrayList<WorkflowExecutionPlan> set, WorkflowTimeMachine wtm, int p, int runs) 
			throws PNException, IOException, ProjectComponentException {		
		ArrayList<WorkflowExecutionPlan> originalList = new ArrayList<>();
		Set<WorkflowExecutionPlan> resultSet = new TreeSet<WorkflowExecutionPlan>();
		Set<WorkflowExecutionPlan> intermediateSet;
		//optimizationResults.clear(); //clearing the results here; it's a bad solution!
		int simulationSize = (int) (set.size() * ((double)p/100)) +1; //determine the number of sequences which should be simulated and round up
		int runEach = (int) runs/simulationSize;
		if (runEach <2){
			//set the number of runs for each sequence to at least two
			runEach = 2;
			}
		for (int i=0; i<simulationSize; i++){
			originalList.add(set.get(i));
		}
		for (WorkflowExecutionPlan wep:originalList){			
			wtm.simulateExecutionPlan(runEach, wep.getSeq());
			intermediateSet = generatePlans();
			resultSet.addAll(intermediateSet);
			//Add the result of this Sequence to the OptimizationResults list
			optimizationResults.add(new OptimizationResult(wep.getSeq(), new ArrayList<WorkflowExecutionPlan>(intermediateSet)));
			intermediateSet.clear();			
		}		
		ArrayList<WorkflowExecutionPlan> resultList = new ArrayList<WorkflowExecutionPlan>(resultSet);
		Collections.reverse(resultList);
		//printResults(resultList);
		return resultList;
	}
	
	
	private void printResults (ArrayList<WorkflowExecutionPlan> set){
		if (set.size() > 50){
			 //Prints Top50 Results
			for (int k = 0; k<50; k++){				
				System.out.println(k+": "+set.get(k));				
			}			
			System.out.println("The number of occured sequences is "+set.size());
		} else {
			int i = 0;
			for (WorkflowExecutionPlan plan:set){
				System.out.println(i+": "+plan);
				i++;
				}
			System.out.println("The number of occured sequences is "+set.size());
		}		
	}
	
	private void getOverallFitness(ArrayList<WorkflowExecutionPlan> set){
		float sum = 0;
		float overallFitness;
		int runs = 0;
		// Weighted sum of sequence performances
		for (WorkflowExecutionPlan plan:set){
			runs += plan.getNumberOfRuns();
			sum += plan.getNumberOfRuns() * plan.getPerformance();
			}
		overallFitness = sum / runs;
		System.out.println("Overall Fitness is: " + overallFitness);
	}
	
	
	public JaschaPlanExtractor() throws IOException, ProjectComponentException{
		this(WorkflowTimeMachine.getInstance(), StatisticListener.getInstance());
	}

	private JaschaPlanExtractor(WorkflowTimeMachine wtm, StatisticListener listener) throws IOException, ProjectComponentException {
		this.wtm=WorkflowTimeMachine.getInstance();
		this.listener = listener;
		//this.fireSequence=getResultsFromNonClonedNets();
		this.context=getTimeContext();
		this.ar = new ArchitectureResults(wtm);
		
		//plans = generatePlans();
	}
	
	/**gets (and if needed generates) the ordered execution plan**/
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
	
	private Set<WorkflowExecutionPlan> generatePlans(){
		HashMap<FireSequence, LinkedList<Double>> computedResults = new HashMap<>(); //store fire sequence and simulation results (performance)
		endingTimesMap = new HashMap<>(); //store fire sequence and the ending times which occurred for this sequence during the simulation
		
		//listener.getOverallLog gives an empty FireSequence as the last Object --> remove it
		List<FireSequence> fireSeqList = listener.getOverallLog();		
		fireSeqList.remove(fireSeqList.size()-1);
		
		for(FireSequence seq: fireSeqList){
			if(!computedResults.containsKey(seq)){ 
				computedResults.put(seq,new LinkedList<Double>()); //create list
				//computedResults.get(seq).add(getOverallSuccessRatio(seq));
				computedResults.get(seq).add(ar.getWeightedSuccessRatio(seq));
				endingTimesMap.put(seq, new LinkedList<Double>()); //create second list
				endingTimesMap.get(seq).add(seq.getEndingTime()); //add ending time to FireSequence
			}
			else {
				endingTimesMap.get(seq).add(seq.getEndingTime()); //list already contains the FireSequence, add another ending time
				//computedResults.get(seq).add(getOverallSuccessRatio(seq));
				computedResults.get(seq).add(ar.getWeightedSuccessRatio(seq));
			}
		}
		/*	
		 * old Version	 
		for(FireSequence seq:listener.getOverallLog()){ //add OverallPerformance
			computedResults.get(seq).add(getOverallSuccessRatio(seq));
		}
		*/		
		TreeSet<WorkflowExecutionPlan> set = new TreeSet<>();
		for(Entry<FireSequence, LinkedList<Double>> entry:computedResults.entrySet()){
			FireSequence sequence = entry.getKey();
			List<Double> deadlineResults = entry.getValue();
			double performance = computeAverage(deadlineResults);
			WorkflowExecutionPlan plan=new WorkflowExecutionPlan(sequence, deadlineResults.size(), performance);
			plan.setEndingTimes(endingTimesMap.get(sequence)); //adds the actual ending times to the WorkflowExecutionPlan
						
			if(set.contains(plan)){
				System.out.println(compareEntries(set, plan)); //this should never be the case.
			}			
			set.add(plan);
		}		
		return set;
		
	}
	
	private String compareEntries(Set<WorkflowExecutionPlan> set, WorkflowExecutionPlan plan){
		StringBuilder b = new StringBuilder();
		b.append("Plan to add: "+plan.toString()+" Hash: "+plan.hashCode()+"\r\n");
		for(Object o:set){
			b.append(o.toString()+" ("+o.hashCode()+") equals: "+o.equals(plan)+"\r\n");
		}
		return b.toString();
	}
	
	/**compute the score of this workflow execution between 0..1**/
	private double getOverallSuccessRatio(FireSequence seq){
		int success = 0;
		
		//HashMap<String, Double> nets = extractSimulatedTiming(seq); //<NetName, needed time>
		HashMap<String, Double> nets = seq.getFinishTimes(); //falls es doch nicht geht, das hier raus
		
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
		//if(result==Double.NaN || result==0)
		//	System.out.println("getOverallSuccessRatio: result = " + result);		
		return result;
		//if (success==nets.keySet().size()) return 1;
		//return 0;
	}
	
	/**extract the time of the last fired activity from each net out of a fireSequence**/
	private HashMap<String, Double> extractSimulatedTiming(FireSequence seq){
		HashMap<String, Double> nets = new HashMap<>(); //Net-Name, time
		for(FireElement element: seq.getSequence()){ //get fired transitions
			String netName=element.getTransition().getNet().getName();
			double time = element.getEndTime();
			if(nets.containsKey(netName)&&nets.get(netName)>time)
				System.out.println("Something wrong in extractSimulatedTiming");
			nets.put(netName,element.getEndTime());
		}
		return nets;
	}
	
	private double computeAverage(List<Double> list){
		if (list.isEmpty()){
			System.out.println("computerAverage: the list is empty");
			return 0.4321;
		}
		double sum = 0.0;
		for(double d:list){
			sum+=d;
		}			
		double result = sum/(double)list.size();
		if (Double.isNaN(result)){
			//Random result to find the broken entry
			result = 0.1234;
		}
		return result;
	}
}
