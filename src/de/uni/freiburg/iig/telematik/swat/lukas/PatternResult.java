package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;

public class PatternResult {

	private double mProb;
	private boolean mFulfilled;
	private ArrayList<String> mViolatingPath;

	public PatternResult(double prob, boolean fulfilled, ArrayList<String> violatingPaths) {
		this(prob, fulfilled);
		mViolatingPath =  violatingPaths;
	}
	
	public PatternResult(double prob, boolean fulfilled) {
		mProb = prob;
		mFulfilled = fulfilled;
	}
	
	public double getProbability() {
		return mProb;
	}
	
	public boolean isFulfilled() {
		return mFulfilled;
	}
	
	public ArrayList<String> getViolatingPath() {
		return mViolatingPath;
	}
	
	public void setViolatingPath(ArrayList<String> path) {
		mViolatingPath = path;
	}
	
}
