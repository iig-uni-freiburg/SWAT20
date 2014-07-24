package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;

/**
 * This abstract class is a superclass of all implemented compliance patterns.
 * 
 * */
public abstract class CompliancePattern {
	
	protected ArrayList<Operand> mOperands;
	
	private String mPattern;
	
	private String mPrismProperty;
	
	public CompliancePattern() {
		mOperands = new ArrayList<Operand>();
	}

	public CompliancePattern(String formula, boolean antipattern) {
		this();
		mPattern = formula;
		if (antipattern) {
			buildAntiPatternRep(formula);
		} else {
			buildPatternRep(formula);
		}
	}
	
	public String toString() {
		return mPattern;
	}
	
	public String getPrismRep() {
		return mPrismProperty;
	}
	
	public int getOperatorCount() {
		return mOperands.size();
	}
	
	private void buildPatternRep(String formula) {
		mPrismProperty = "P>=1 [" + formula + "]";
	}
	
	private void buildAntiPatternRep(String formula) {
		mPrismProperty = "P>0 [" + formula + "]";
	}
	
	protected void setPattern(String formula, boolean antipattern) {
		
		if (antipattern) {
			mPattern = formula;
			buildAntiPatternRep(formula);
		} else {
			mPattern = formula;
			buildPatternRep(formula);
		}
	}
	
	public abstract boolean isAntiPattern();
	
	public abstract String getName();
	
	public abstract String getDescription();

}
