package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;

public abstract class CompliancePattern {
	
	protected ArrayList<Operand> mOperands;
	
	private String mPattern;
	
	private String mPrismRep;

	public CompliancePattern(String formula) {
		mOperands = new ArrayList<Operand>();
		mPattern = formula;
		buildPatternRep(formula);
	}
	
	public String toString() {
		return mPattern;
	}
	
	public String getPrismRep() {
		return mPrismRep;
	}
	
	public int getOperatorCount() {
		return mOperands.size();
	}
	
	private void buildPatternRep(String formula) {
		mPrismRep = "P>=1 [" + formula + "]";
	}
	

}
