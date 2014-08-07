package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.HashSet;
import java.util.Set;

import org.processmining.analysis.sciffchecker.logic.reasoning.CheckerReport;

public class SCIFFResult {
		
	private Set<Integer> mWrongInstances;
	
	private Set<Integer> mCorrectInstances;
	
	private Set<Integer> mExceptionInstances;
	
	public SCIFFResult() {
		
		mWrongInstances = new HashSet<Integer>();
		mCorrectInstances = new HashSet<Integer>();
		mExceptionInstances = new HashSet<Integer>();
		
	}
	
	public void addReport(CheckerReport cr) {
		
		mWrongInstances.addAll(cr.wrongInstances());
		mCorrectInstances.addAll(cr.wrongInstances());
		mExceptionInstances.addAll(cr.wrongInstances());
		
	}

	public boolean isSatisfied() {
		return (mWrongInstances.size() == 0)? true : false;
	}
}
