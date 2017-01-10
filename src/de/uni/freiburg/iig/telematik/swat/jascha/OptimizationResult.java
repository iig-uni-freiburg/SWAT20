package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.FireSequence;
import de.uni.freiburg.iig.telematik.swat.simulation.WorkflowExecutionPlan;

public class OptimizationResult {
	
	//This class contains an original sequence which was simulated and the resulting workflow execution plans from this simulation
	//It also contains several fitness values resulting from this simulation run
	
	FireSequence originalSequence;
	String name;
	List<WorkflowExecutionPlan> plans;
	private List<Double> finishTimes;
	private double overallFitness=0.0;
	private double averageFinishTime=0.0;
	private double medianFinishTime=0.0;
	private int numberOfRuns;

	public OptimizationResult(FireSequence originalSequence, List<WorkflowExecutionPlan> plans) {
		this.originalSequence = originalSequence;
		this.plans = plans;
		finishTimes = new ArrayList<Double>();		
		name = originalSequence.toString();
		numberOfRuns = 0;
		initialise();
	}
	
	private void initialise() {
		List<Double> performanceList = new ArrayList<Double>();
		for (WorkflowExecutionPlan wep:plans){
			if (wep == null) {
				System.out.println("this wep is null:"+wep);
				}
			else {
				finishTimes.addAll(wep.getEndingTimes());
				performanceList.add(wep.getPerformance()*wep.getNumberOfRuns());
				numberOfRuns += wep.getNumberOfRuns();				
				}			
		}
		averageFinishTime = computeAverage(finishTimes);
		medianFinishTime = computeMedian(finishTimes);
		overallFitness = computeAverage(performanceList, numberOfRuns);		
		
	}

	private double computeMedian(List<Double> list) {
		double result = list.get(list.size()/2);
		return result;
	}

	private double computeAverage(List<Double> list) {
		double sum = 0.0;
		double result = 0.0;
		for (double d: list){
			sum+=d;
		}
		result = sum / (double)list.size();
		return result;
	}

	private double computeAverage(List<Double> list, int runs) {
		double sum = 0.0;
		double result = 0.0;
		for (double d: list){
			sum+=d;
		}
		result = sum / runs;
		return result;
	}
	
	public FireSequence getOriginalSequence() {
		return originalSequence;
	}
	
	public List<WorkflowExecutionPlan> getPlans() {
		return plans;
	}
	
	public List<Double> getFinishTimes() {
		return finishTimes;
	}
	
	public int getNumberOfRuns() {
		return numberOfRuns;
	}
	
	public double getOverallFitness() {
		return overallFitness;
	}
	
	public double getAverageFinishTime() {
		
		return averageFinishTime;
	}
	
	public double getMedianFinishTime() {
		return medianFinishTime;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "OptimizationResult [originalPlan=" + originalSequence.toString() + ", overallFitness="
				+ overallFitness + ", averageFinishTime=" + averageFinishTime + ", medianFinishTime=" + medianFinishTime
				+ ", number of contained Sequences=" + plans.size() + "]";
	}
}
