package de.uni.freiburg.iig.telematik.swat.simulation;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.FireSequence;

public class WorkflowExecutionPlan implements Comparable<WorkflowExecutionPlan>{
	
	private FireSequence seq;
	private int numberOfRuns;
	private double performance;
	static DecimalFormat df = new DecimalFormat("#.##");	
	private List<Double> endingTimes = new LinkedList<>(); //The (sorted) list of actual ending times of simulation runs with this FireSequence

	public WorkflowExecutionPlan(FireSequence seq, int numberOfRuns, double performance) {
		this.seq=seq;
		this.numberOfRuns = numberOfRuns;
		this.performance=performance;
	}

	public FireSequence getSeq() {
		return seq;
	}

	public void setSeq(FireSequence seq) {
		this.seq = seq;
	}

	public int getNumberOfRuns() {
		return numberOfRuns;
	}

	public void setNumberOfRuns(int numberOfRuns) {
		this.numberOfRuns = numberOfRuns;
	}

	public double getPerformance() {
		return performance;
	}

	public void setPerformance(double performance) {
		this.performance = performance;
	}
	
	public void setEndingTimes (List<Double> endingTimesList){
		endingTimes = endingTimesList;
		Collections.sort(endingTimes);
	}
	
	public void addEndingTime(Double d){
		endingTimes.add(d);
	}
	
	public List<Double> getEndingTimes(){
		Collections.sort(endingTimes);
		return endingTimes;
	}
	
	public String toString(){
		return df.format(performance*100)+"% ("+numberOfRuns+" entries): "+seq.getTransitionString();
	}

	@Override
	public int compareTo(WorkflowExecutionPlan o) {
		return myCompareTo(o);
		//return new Double(performance).compareTo(o.performance);
		//return new Integer(numberOfRuns).compareTo(o.getNumberOfRuns());
		//return seq.getTransitionString().compareTo(o.seq.getTransitionString());
	}
	
	public int myCompareTo(WorkflowExecutionPlan plan){
		if (Double.isNaN(performance) || Double.isNaN(plan.getPerformance())){
			System.out.println("One performance is NaN!");
			return 1;
		}
		if (this.equals(plan)){
			System.out.println("We're equal because:");
			System.out.println(this.toString());
			System.out.println("equals");
			System.out.println(plan.toString());
			//Returning 0 in compareTo means the TreeSet thinks the objects are equal
			return 0;
		}
		if (performance == plan.getPerformance()){
			//here we do a second level ordering with numberOfRuns
			if (numberOfRuns < plan.getNumberOfRuns())
				return -1;
			if (numberOfRuns >= plan.getNumberOfRuns())
				return 1;
		}		
		else if (performance > plan.getPerformance()){
			return 1;
		}
		else if(performance < plan.getPerformance()){
			return -1;
		}
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + numberOfRuns;
		long temp;
		temp = Double.doubleToLongBits(performance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((seq == null) ? 0 : seq.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkflowExecutionPlan other = (WorkflowExecutionPlan) obj;
		if (numberOfRuns != other.numberOfRuns)
			return false;
		if (Double.doubleToLongBits(performance) != Double.doubleToLongBits(other.performance))
			return false;
		if (seq == null) {
			if (other.seq != null)
				return false;
		} else if (!seq.equals(other.seq))
			return false;
		return true;
	}

}
