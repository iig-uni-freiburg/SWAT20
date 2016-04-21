package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns;

public abstract class XLeadsTo extends CandDPattern {
	
	protected final String mPatternName = "P XLeadsTo Q";
	
	protected final String mDescription = "P must be directly followed by Q";
	
	@Override
	public String getName() {
		return mPatternName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}
}

