package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.HashMap;

public abstract class AtomicPattern extends CompliancePattern {
	
	public static final String NAME = "Atomic Pattern";
	
	private static HashMap<String, String> mPatternDescription;
	
	public AtomicPattern() {
		
	}

	public AtomicPattern(String formula) {
		super(formula, false);
	}
	
	public static HashMap<String, String> getPatternDescription() {
		
		if (mPatternDescription == null) {
			mPatternDescription = new HashMap<String, String>();
			mPatternDescription.put(Precedes.NAME, Precedes.DESC);
			mPatternDescription.put(ChainPrecedes.NAME, ChainPrecedes.DESC);
			mPatternDescription.put(PrecedesChain.NAME, PrecedesChain.DESC);
			mPatternDescription.put(ChainLeadsTo.NAME, ChainLeadsTo.DESC);
			mPatternDescription.put(LeadsToChain.NAME, LeadsToChain.DESC);
			mPatternDescription.put(XLeadsTo.NAME, XLeadsTo.DESC);
			mPatternDescription.put(Else.NAME, Else.DESC);
			mPatternDescription.put(Exists.NAME, Exists.DESC);
			mPatternDescription.put(Universal.NAME, Universal.DESC);
			mPatternDescription.put(Absent.NAME, Absent.DESC); 
		}
		
		return mPatternDescription;
	}
	
	@Override
	public boolean isAntiPattern() {
		return false;
	}
	
}
