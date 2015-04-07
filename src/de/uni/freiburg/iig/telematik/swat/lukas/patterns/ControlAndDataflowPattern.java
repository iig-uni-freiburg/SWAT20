package de.uni.freiburg.iig.telematik.swat.lukas.patterns;

import java.util.HashMap;

public abstract class ControlAndDataflowPattern extends CompliancePattern {
	
	public static final String NAME = "Composite Patterns";
	
	private static HashMap<String, String> mPatternDescription;
	
	public ControlAndDataflowPattern(String prismLTLproperty, String prismCTLproperty) {
		super(prismLTLproperty, prismCTLproperty, false);
	}
	
	public ControlAndDataflowPattern(String prismLTLproperty) {
		super(prismLTLproperty, false);
	}
	
    public ControlAndDataflowPattern() {
		super(false);
	}

	public static HashMap<String, String> getPatternDescription() {
		
		if (mPatternDescription == null) {
			mPatternDescription = new HashMap<String, String>();
			mPatternDescription.put(CoExists.NAME, CoExists.DESC);
			mPatternDescription.put(CoAbsent.NAME, CoAbsent.DESC);
			mPatternDescription.put(Exclusive.NAME, Exclusive.DESC);
			mPatternDescription.put(Corequisite.NAME, Corequisite.DESC);
			mPatternDescription.put(MutexChoice.NAME, MutexChoice.DESC); 
			mPatternDescription.put(Precedes.NAME, Precedes.DESC);
			mPatternDescription.put(ChainPrecedes.NAME, ChainPrecedes.DESC);
			mPatternDescription.put(PrecedesChain.NAME, PrecedesChain.DESC);
			mPatternDescription.put(ChainLeadsTo.NAME, ChainLeadsTo.DESC);
			mPatternDescription.put(LeadsToChain.NAME, LeadsToChain.DESC);
			mPatternDescription.put(XLeadsTo.NAME, XLeadsTo.DESC);
			mPatternDescription.put(LeadsTo.NAME, LeadsTo.DESC);
			mPatternDescription.put(Else.NAME, Else.DESC);
			mPatternDescription.put(Exists.NAME, Exists.DESC);
			mPatternDescription.put(Universal.NAME, Universal.DESC);
			mPatternDescription.put(Absent.NAME, Absent.DESC);
			mPatternDescription.put(PLeadsTo.NAME, PLeadsTo.DESC);
		}
		
		return mPatternDescription;
	}
	
	@Override
	public boolean isAntiPattern() {
		return false;
	}

}
