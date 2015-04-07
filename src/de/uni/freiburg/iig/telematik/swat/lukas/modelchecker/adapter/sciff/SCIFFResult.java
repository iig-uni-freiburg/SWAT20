package de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.sciff;

import java.util.ArrayList;

import org.processmining.analysis.sciffchecker.logic.reasoning.CheckerReport;

public class SCIFFResult {
		
	private ArrayList<Integer> mWrongInstances;
	
	private ArrayList<Integer> mCorrectInstances;
	
	private ArrayList<Integer> mExceptionInstances;
	
	public SCIFFResult() {
		mWrongInstances = new ArrayList<Integer>();
		mCorrectInstances = new ArrayList<Integer>();
		mExceptionInstances = new ArrayList<Integer>();
		
	}
	
	public void addReport(CheckerReport cr) {
		
		mWrongInstances.addAll(cr.wrongInstances());
		mCorrectInstances.addAll(cr.correctInstances());
		mExceptionInstances.addAll(cr.exceptionInstances());
		
	}

	public boolean isFulfilled() {
		return (mWrongInstances.size() == 0)? true : false;
	}
}
