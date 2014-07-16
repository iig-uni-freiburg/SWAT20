package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;

public class PatternResult {

	private double mProb;
	private boolean mFulfilled;
	private ArrayList<ArrayList<String>> mViolatingPaths;

	public PatternResult(double prob, boolean fulfilled, ArrayList<ArrayList<String>> violatingPaths) {
		mProb = prob;
		mFulfilled = fulfilled;
		mViolatingPaths =  violatingPaths;
	}
	
	public double getProbability() {
		return mProb;
	}
	
	public boolean isFulfilled() {
		return mFulfilled;
	}
	
	public ArrayList<ArrayList<String>> getViolatingPaths() {
		return mViolatingPaths;
	}
	
	public ArrayList<String> getViolatingPath(int i) {
		return mViolatingPaths.get(i);
	}
}
