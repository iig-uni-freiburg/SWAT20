package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.HashMap;

public abstract class CompositePattern extends CompliancePattern {
	
	public static final String NAME = "Composite Patterns";
	
	private static HashMap<String, String> mPatternDescription;
	
	public CompositePattern(String prismLTLproperty, String prismCTLproperty) {
		super(prismLTLproperty, prismCTLproperty, false);
	}
	
	public CompositePattern(String prismLTLproperty) {
		super(prismLTLproperty, false);
	}
	
    public static HashMap<String, String> getPatternDescription() {
		
		if (mPatternDescription == null) {
			mPatternDescription = new HashMap<String, String>();
			mPatternDescription.put(CoExists.NAME, CoExists.DESC);
			mPatternDescription.put(CoAbsent.NAME, CoAbsent.DESC);
			mPatternDescription.put(Exclusive.NAME, Exclusive.DESC);
			mPatternDescription.put(Corequisite.NAME, Corequisite.DESC);
			mPatternDescription.put(MutexChoice.NAME, MutexChoice.DESC); 
		}
		
		return mPatternDescription;
	}
	
	@Override
	public boolean isAntiPattern() {
		return false;
	}

}
