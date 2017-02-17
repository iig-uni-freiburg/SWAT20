package de.uni.freiburg.iig.telematik.swat.simulation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import de.uni.freiburg.iig.telematik.swat.jascha.WEPsortByFireSequence;
import de.uni.freiburg.iig.telematik.swat.jascha.WEPsortByPerformance;
import de.uni.freiburg.iig.telematik.swat.jascha.WEPsortByFireSequence;
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
	private static int numberOfRuns = 20000;
	private static int numberOfOptiRuns = 40000;
	private static int probability = 10; //Im Moment p/10000
	private ArchitectureResults ar;
	private List<OptimizationResult> optimizationResults = new ArrayList<OptimizationResult>();
	private HashMap<String, Integer> compareMap = new HashMap<String, Integer>();;



	public static void main(String args[]) throws IOException, ParserException, PNException, ProjectComponentException {
		String net1String="Tiefbau";
		//String net2String="Strassenlaterne";
		String net3String="Fundament";
		//String net4String="Sisyphos";
		String net5String="Strassenbau";
		String net6String="Abriss";
		//String net1String="Multinom1";
		//String net2String="Multinom2";
		//String net3String="Multinom3";

		//String net1String="invoiceIn";
		//String net2String="invoiceOut";
		SwatComponents.getInstance();
		GraphicalTimedNet net1 = (GraphicalTimedNet) SwatComponents.getInstance().getContainerPetriNets().getComponent(net1String);
		//GraphicalTimedNet net2 = (GraphicalTimedNet) SwatComponents.getInstance().getContainerPetriNets().getComponent(net2String);
		GraphicalTimedNet net3 = (GraphicalTimedNet) SwatComponents.getInstance().getContainerPetriNets().getComponent(net3String);
		//GraphicalTimedNet net4 = (GraphicalTimedNet) SwatComponents.getInstance().getContainerPetriNets().getComponent(net4String);
		GraphicalTimedNet net5 = (GraphicalTimedNet) SwatComponents.getInstance().getContainerPetriNets().getComponent(net5String);
		GraphicalTimedNet net6 = (GraphicalTimedNet) SwatComponents.getInstance().getContainerPetriNets().getComponent(net6String);
		WorkflowTimeMachine wtm = WorkflowTimeMachine.getInstance();

		wtm.addNet(net1.getPetriNet());
		//wtm.addNet(net2.getPetriNet());
		wtm.addNet(net3.getPetriNet());
		//wtm.addNet(net4.getPetriNet());
		wtm.addNet(net5.getPetriNet());
		wtm.addNet(net6.getPetriNet());
		wtm.simulateAll(numberOfRuns);

		JaschaPlanExtractor ex = new JaschaPlanExtractor(WorkflowTimeMachine.getInstance(), StatisticListener.getInstance());
		ArrayList<WorkflowExecutionPlan> set = ex.getExecutionPlan();
		ex.optimizationResults.add(new OptimizationResult(new FireSequence(), set));

		//Reverse list for easier working with it
		Collections.reverse(set);

		ex.printResults(set);
		ex.printOverallFitness(set);
		
		/**
		 * F�r schnellere Experimente; f�r normale Durchf�hrung entfernen
		 */
		ArrayList<WorkflowExecutionPlan> reductionResult2 = new ArrayList<WorkflowExecutionPlan>();
		reductionResult2 = ex.simulateReduction(set, wtm, probability, numberOfOptiRuns);
		
		Collections.sort(ex.optimizationResults, OptimizationResult.OptimizationResultPerformanceComparator);
		System.out.println("optimizationResults size="+ex.optimizationResults.size());
		if (ex.optimizationResults.size()<50){
			int i = 1;
			for (OptimizationResult or:ex.optimizationResults){
				System.out.println(i +". "+ or.toString());
				i++;
			}

		} else {
			System.out.println("1. "+ex.optimizationResults.get(0).toString());
			int j = 2;
			for (int i = ex.optimizationResults.size()-50; i<ex.optimizationResults.size(); i++){
				System.out.println(j +". "+ex.optimizationResults.get(i));
				j++;
			}
			ex.printWorstAndBestFitness(ex.optimizationResults);					
		}
		int test = 1;
		while (test == 1){
			wtm.resetAll();	
			Scanner keyboard2 = new Scanner(System.in);
			int myint2 = keyboard2.nextInt();	
			wtm.simulateExecutionPlan(8000, ex.optimizationResults.get(myint2).getOriginalSequence());			
			System.out.println("The simulated pattern was:" + ex.optimizationResults.get(myint2).getOriginalSequence());
			new SimulationResult(wtm, getTimeContext(),false).setVisible(true);
		}
		/**
		 * Bis hier entfernen
		 */
		
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
				ex.simulateTopPercent(set, wtm, 10, numberOfRuns, true); //top% to be simulated and number of runs
				break;

			case 97:
				// simulate repeatedly some top percent and compare which sequences occur how often
				ex.simulateRepeatedly(set, wtm, numberOfRuns, 20, 20);
				break;	

			case 96:
				ArrayList<WorkflowExecutionPlan> reductionResult = new ArrayList<WorkflowExecutionPlan>();
				reductionResult = ex.simulateReduction(set, wtm, 10, numberOfRuns);
				System.out.println("Do you want to find the best (shortest) ending times? 0 for yes, 1 for no, 2 for OptimzationResults");
				int inputInt = keyboard.nextInt();
				if (inputInt == 0){
					// get best ending times from result
					ex.getEndingTimesFromWEP(reductionResult);
				} else 
					if (inputInt == 2){
						Collections.sort(ex.optimizationResults, OptimizationResult.OptimizationResultPerformanceComparator);
						System.out.println("optimizationResults size="+ex.optimizationResults.size());
						if (ex.optimizationResults.size()<50){
							int i = 1;
							for (OptimizationResult or:ex.optimizationResults){
								System.out.println(i +". "+ or.toString());
								i++;
							}

						} else {
							System.out.println("1. "+ex.optimizationResults.get(0).toString());
							int j = 2;
							for (int i = ex.optimizationResults.size()-50; i<ex.optimizationResults.size(); i++){
								System.out.println(j +". "+ex.optimizationResults.get(i));
								j++;
							}
							ex.printWorstAndBestFitness(ex.optimizationResults);					
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

	private void simulateRepeatedly(ArrayList<WorkflowExecutionPlan> set, WorkflowTimeMachine wtm, int runs, int repetitions, int topNumber) {		
		int overallCount = set.size();
		// initialize the compare map with the top p percent of the original result
		compareMap.clear();
		for (int i = 0; i < set.size(); i++){			
			compareMap.put(set.get(i).getSeq().getTransitionString(), 1);
		}		
		ArrayList<WorkflowExecutionPlan> intermediateList = new ArrayList<WorkflowExecutionPlan>();
		for (int j = 0; j < repetitions; j++){
			intermediateList.clear();
			//intermediateList = simulateTopPercent(set, wtm, p, runs, false);
			intermediateList = simulateTopNumber(set, wtm, topNumber, runs);
			countFireSequenceOccurences(intermediateList);
			overallCount+=intermediateList.size();				
		}
		int compareCount = printCompareMap();
		System.out.println("Overall count = "+overallCount+" and compare count = "+compareCount);
	}

	private ArrayList<WorkflowExecutionPlan> simulateTopNumber(ArrayList<WorkflowExecutionPlan> list,
			WorkflowTimeMachine wtm, int topValues, int runs) {
		ArrayList<WorkflowExecutionPlan> topList = new ArrayList<>();		
		ArrayList<WorkflowExecutionPlan> resultList = new ArrayList<>();
		Set<WorkflowExecutionPlan> intermediateSet;
		int runEach = (int)runs/topValues;
		for (int i=0; i<topValues; i++){
			topList.add(list.get(i));
		}
		try {
			for (WorkflowExecutionPlan wep:topList){
				wtm.simulateExecutionPlan(runEach, wep.getSeq());
				intermediateSet = generatePlans();
				// TODO: The WEP contains numberOfRuns --> it should be added to the resultList, which Sequence occurred how often 
				// because thats the whole idea behind this method! Else it is wrong!
				resultList.addAll(intermediateSet);
			}
		} catch (PNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return resultList;
	}

	private int printCompareMap() {
		int result = 0;
		List<Entry<String, Integer>> entryList = new ArrayList<Entry<String, Integer>>();
		entryList.addAll(compareMap.entrySet());
		entryList.sort(Entry.comparingByValue());
		for (Entry<String, Integer> e:entryList){
			if (e.getValue() > 10){
				System.out.println("Occurences: "+e.getValue()+", FireSequence: "+e.getKey());
				result+=e.getValue();
			} else {
				result+=e.getValue();
			}			
		}
		//for (Entry<String, Integer> e:compareMap.entrySet()){			
		//	System.out.println("Occurences: "+e.getValue()+" FireSequence: "+e.getKey());
		//	result+=e.getValue();
		//}
		System.out.println("Number of sequences in CompareMap: "+compareMap.size());
		return result;
	}

	private void countFireSequenceOccurences(ArrayList<WorkflowExecutionPlan> list) {
		String fireSeqName;
		for (WorkflowExecutionPlan wep: list){
			fireSeqName = wep.getSeq().getTransitionString();
			if (compareMap.containsKey(fireSeqName)){
				int newValue = compareMap.get(fireSeqName) +1;
				compareMap.put(fireSeqName, newValue);
			} else {
				compareMap.put(fireSeqName, 1);
			}
		}
	}

	private void printWorstAndBestFitness(List<OptimizationResult> list) {
		List<Double> extremes = new LinkedList<Double>();
		for (OptimizationResult or:list){
			extremes.add(or.getOverallFitness());
		}
		Collections.sort(extremes);
		System.out.println("Best Fitness Results:");
		for (int l = extremes.size()-1; l >=extremes.size()-11; l--){
			System.out.println(l+". "+ extremes.get(l));
		}
		System.out.println("Worst Fitness Results:");
		for (int k = 0; k <=10; k++){
			System.out.println((k+10)+". "+ extremes.get(k));
		}

	}


	// relies on ending times being unique - which they usually are
	private void getEndingTimesFromWEP(ArrayList<WorkflowExecutionPlan> wepList) {
		HashMap<Double, String> map = new HashMap<Double, String>();
		List<Double> resultList = new ArrayList<Double>();
		for (WorkflowExecutionPlan wep: wepList){
			double d = wep.getEndingTimes().get(0);
			map.put(d, wep.getSeq().getTransitionString());
			resultList.add(d);
		}
		Collections.sort(resultList);
		System.out.println("The shortest ending times were:");
		for (int i=0; i < 20; i++){
			System.out.println(i+". "+ resultList.get(i) + " for Sequence "+ map.get(resultList.get(i)));			
		}		
	}

	private ArrayList<WorkflowExecutionPlan> simulateReduction(ArrayList<WorkflowExecutionPlan> set, WorkflowTimeMachine wtm, int p, int runs) {
		//clear the list with the results of former optimizations
		//optimizationResults.clear();
		//create copy of original set:
		ArrayList<WorkflowExecutionPlan> intermediateList = new ArrayList<>(set);
		List<WorkflowExecutionPlan> newList = new ArrayList<WorkflowExecutionPlan>();
		boolean generateOptimizationResults = false;
		int size = set.size();
		int round = 0;
		int counter = 0;
		//Idee: reduziere Ergebnismenge auf weniger als 10 oder stoppe nach 10 Runden oder stoppe wenn Ergebnis nicht weiter reduziert wird
		while (round < 20){			
			round++;
			System.out.println("Starting round "+round+"...");
			newList.clear();
			try {
				if (counter >=3 || round == 20){
					generateOptimizationResults = true;
				}
				newList = simulateTopPercent(intermediateList, wtm, p, runs, generateOptimizationResults);
				intermediateList.clear();
				intermediateList.addAll(newList);				 
				if (newList.size() > (double)size*0.90){ //Reduktion auf mindestens 90%, sonst counter++
					counter++;					
					//wenn das neue Ergebnis 3 Mal kaum kleiner oder sogar gr��er wurde, Abbruch
					if (counter >=4){
						System.out.println("Breaking because the counter reached "+counter);
						size = newList.size();
						break;
					}
				}				
				size = newList.size();
				System.out.println("Result size = "+size);
				System.out.println("The counter is at "+counter);
				System.out.println("-----------------------------------------------");
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
		intermediateList.sort(new WEPsortByPerformance());
		Collections.reverse(intermediateList);
		printResults(intermediateList);
		//Debug:
		//countFireSequenceOccurences(intermediateList);
		//printCompareMap();
		return intermediateList;
	}
	
	// checks for WorkflowExecutionPlans with the same FireSequence and merges them (deletes them and creates one new)
	private List<WorkflowExecutionPlan> mergeEqualWorkflowExecutionPlans(List<WorkflowExecutionPlan> list) {
		HashMap<FireSequence, ArrayList<WorkflowExecutionPlan>> map = new HashMap<FireSequence, ArrayList<WorkflowExecutionPlan>>();
		for (WorkflowExecutionPlan wep:list){
			if (!map.containsKey(wep.getSeq())){
				map.put(wep.getSeq(), new ArrayList<WorkflowExecutionPlan>());
				map.get(wep.getSeq()).add(wep);
			} else {
				map.get(wep.getSeq()).add(wep);
			}
		}
		//for each FireSequence that has more than one WEP a new WEP with the merged values is created
		for (Entry<FireSequence, ArrayList<WorkflowExecutionPlan>> entry:map.entrySet()){			
			if (entry.getValue().size()>1){
				double newPerformance;
				double nr = 0.0;
				int newNumberOfRuns = 0;
				List<Double> newEndingTimes = new ArrayList<Double>();
				for (WorkflowExecutionPlan wep2:entry.getValue()){
					nr += (wep2.getPerformance() * wep2.getNumberOfRuns());
					newNumberOfRuns += wep2.getNumberOfRuns();
					newEndingTimes.addAll(wep2.getEndingTimes());
					list.remove(wep2);
				}
				newPerformance = nr / newNumberOfRuns;
				WorkflowExecutionPlan newWep = new WorkflowExecutionPlan(entry.getKey(), newNumberOfRuns, newPerformance);
				newWep.setEndingTimes(newEndingTimes);
				list.add(newWep);				
			}
		}
		return list;
	}
	
	private void remap(ArrayList<WorkflowExecutionPlan> oldWeps, ArrayList<WorkflowExecutionPlan> newWep){
		
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
		printOverallFitness(executionPlans);
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

	// Take top % results and simulate them again
	private List<WorkflowExecutionPlan> simulateTopPercent(ArrayList<WorkflowExecutionPlan> set, WorkflowTimeMachine wtm,
			int p, int runs, boolean generateOptimizationResults) 
					throws PNException, IOException, ProjectComponentException {		
		ArrayList<WorkflowExecutionPlan> originalList = new ArrayList<>();		
		//initializes the TreeSet with the custom comparator WEPsortByPerformance
		//Set<WorkflowExecutionPlan> resultSet = new TreeSet<WorkflowExecutionPlan>(new WEPsortByPerformance());
		ArrayList<WorkflowExecutionPlan> intermediateList = new ArrayList<>();
		Set<WorkflowExecutionPlan> intermediateSet;		
		//p/1000 for percentages below 1%
		int simulationSize = (int) (set.size() * ((double)p/10000)) +1; //determine the number of sequences which should be simulated and round up
		System.out.println("simulationSize ="+simulationSize);
		int runEach = (int) runs/simulationSize;
		if (runEach <2){
			//set the number of runs for each sequence to at least two
			runEach = 2;
		}
		for (int i=0; i<simulationSize; i++){
			originalList.add(set.get(i));
		}
		if (generateOptimizationResults){
			for (WorkflowExecutionPlan wep:originalList){			
				wtm.simulateExecutionPlan(runEach, wep.getSeq());
				intermediateSet = generatePlans();
				//resultSet.addAll(intermediateSet);
				intermediateList.addAll(intermediateSet);
				//Add the result of this Sequence to the OptimizationResults list
				optimizationResults.add(new OptimizationResult(wep.getSeq(), new ArrayList<WorkflowExecutionPlan>(intermediateSet)));
				intermediateSet.clear();
			}
		} else {
			for (WorkflowExecutionPlan wep:originalList){			
				wtm.simulateExecutionPlan(runEach, wep.getSeq());
				intermediateSet = generatePlans();
				//resultSet.addAll(intermediateSet);
				intermediateList.addAll(intermediateSet);
				intermediateSet.clear();
			}
		}
		
		List<WorkflowExecutionPlan> resultList = new ArrayList<WorkflowExecutionPlan>();		
		//Merge Duplicate FireSequences for the intermediate result. Takes a long time to compute --> remove worst 66% before merging		
		if (generateOptimizationResults){
			if(intermediateList.size() >1){
				System.out.println("start merging "+intermediateList.size()+" entries ...");
				resultList = mergeEqualWorkflowExecutionPlans(intermediateList);
				resultList.sort(new WEPsortByPerformance());
			} else {
				resultList.addAll(intermediateList);
				resultList.sort(new WEPsortByPerformance());
			}

		} else {
			intermediateList.sort(new WEPsortByPerformance());
			//take top third of sequences and merge them for further optimization			
			int startIndex = (int)(intermediateList.size() / 1.5);
			//System.out.println("startIndex="+startIndex+", intermediateList.size="+intermediateList.size());
			System.out.println("start merging "+(intermediateList.size()-startIndex)+" entries ...");
			resultList = mergeEqualWorkflowExecutionPlans(intermediateList.subList(startIndex, intermediateList.size()));
			//take bottom 66% of sequences and remove duplicates to retain the correct reduction factor.
			TreeSet<WorkflowExecutionPlan> ts = new TreeSet<WorkflowExecutionPlan>(new WEPsortByFireSequence());
			ts.addAll(intermediateList.subList(0, (startIndex)-1));
			//System.out.println("intermediateList.size="+intermediateList.size()+", startIndex="+startIndex+
			//		", resultList.size="+resultList.size()+", ts.size()="+ts.size());
			resultList.addAll(ts);
			resultList.sort(new WEPsortByPerformance());
		}
		Collections.reverse(resultList);
		System.out.println("returning "+resultList.size()+" entries");
		return resultList;
	}


	public void printResults (ArrayList<WorkflowExecutionPlan> set){
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

	private void printOverallFitness(ArrayList<WorkflowExecutionPlan> set){
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
		//System.out.println("Starting generatePlans() in PlanExtractor");
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
		TreeSet<WorkflowExecutionPlan> set = new TreeSet<WorkflowExecutionPlan>(new WEPsortByPerformance());
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
