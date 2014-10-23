package de.uni.freiburg.iig.telematik.swat.lukas.patterns;

import java.util.ArrayList;

import de.uni.freiburg.iig.telematik.swat.lukas.operands.Operand;

/**
 * This abstract class is a superclass of all implemented compliance patterns.
 * */
public abstract class CompliancePattern {
	
	protected ArrayList<Operand> mOperands;
	
	private String mPrismLTLProperty;
	
	private String mPrismCTLProperty;

	private boolean mIsAntipattern;
	
	public CompliancePattern(boolean isAntipatten) {
		mOperands = new ArrayList<Operand>();
		mIsAntipattern = isAntipatten;
	}
	
	public CompliancePattern(String prismLTLProp, boolean isAntipattern) {
		this(isAntipattern);
		mPrismLTLProperty = prismLTLProp;
		
	}
	
	public CompliancePattern(String prismLTLProp, String prismCTLProp, boolean isAntipattern) {
		this(prismLTLProp, isAntipattern);
		mPrismCTLProperty = prismCTLProp;
	}
	
	public String getPrismCTLProperty() {
		return mPrismCTLProperty;
	}
	
	public String getPrismLTLProperty() {
		return mPrismLTLProperty;
	}
	
	public ArrayList<Operand> getOperands() {
		return mOperands;
	}
	
	public String getPrismProp(boolean isBoundedNet) {
		
		if (isBoundedNet && mPrismCTLProperty != null) {
			
			return mPrismCTLProperty;
			
		} else {
			return "P=? [" + mPrismLTLProperty + "]";
		}
	
	}
	
	public String getPrismProperty(boolean isBoundedNet) {
		
		if (isBoundedNet && mPrismCTLProperty != null) {
			
			return mPrismCTLProperty;
			
		} else {
			
			if (mIsAntipattern) {
				return "P<=0 [" + mPrismLTLProperty + "]";
			} else {
				return "P>=1 [" + mPrismLTLProperty + "]";
			}
		}
	}

	public int getOperatorCount() {
		return mOperands.size();
	}
	
	protected void setPrismProperty(String ltlProperty, String ctlProperty, boolean antipattern) {
		
		mIsAntipattern = antipattern;
		mPrismCTLProperty = ctlProperty;
		mPrismLTLProperty = ltlProperty;
		
	}
	
    protected void setPrismProperty(String ltlProperty, boolean antipattern) {
		
		mIsAntipattern = antipattern;
		mPrismLTLProperty = ltlProperty;
		
	}
	
	public abstract boolean isAntiPattern();
	
	public abstract String getName();
	
	public abstract String getDescription();

}
