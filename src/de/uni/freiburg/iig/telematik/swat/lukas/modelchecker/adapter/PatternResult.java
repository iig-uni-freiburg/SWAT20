package de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter;

import java.util.ArrayList;

import de.uni.freiburg.iig.telematik.swat.lukas.operands.Operand;

public class PatternResult {

	private double mProb;
	private boolean mFulfilled;
	private ArrayList<String> mViolatingPath;
	private ArrayList<Operand> mPatternOperands;

	public PatternResult(double prob, boolean fulfilled, 
			ArrayList<String> violatingPaths, ArrayList<Operand> patternOperands) {
		this(prob, fulfilled, patternOperands);
		mViolatingPath =  violatingPaths;
	}
	
	public PatternResult(double prob, boolean fulfilled, ArrayList<Operand> patternOperands) {
		mProb = prob;
		mFulfilled = fulfilled;
		mPatternOperands = patternOperands;
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
	
	public void setProbability(double prob) {
		mProb = prob;
	}
	
	public void setFulfilled(boolean fulfilled) {
		mFulfilled = fulfilled;
	}
	
	public ArrayList<Operand> getPatternOperands() {
		return mPatternOperands;
	}
	
	public PatternResult clone() {		
		PatternResult clone = new PatternResult(mProb, mFulfilled, mPatternOperands); 
		return clone;
	}
	
}