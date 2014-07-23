package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;

/**
 * This abstract class is a superclass of all implementented compliance patterns.
 * 
 * */
public abstract class CompliancePattern {
	
	protected ArrayList<Operand> mOperands;
	
	private String mPattern;
	
	private String mPrismProperty;

	private String mTextualDescription;
	
	public CompliancePattern(String text) {
		mOperands = new ArrayList<Operand>();
		mTextualDescription = text;
	}

	public CompliancePattern(String formula, boolean antipattern, String text) {
		this(text);
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
	
	public abstract String getName();
	
	public abstract boolean isAntiPattern();
	
	public String getTextualDescription() {
		return mTextualDescription;
	}

}
