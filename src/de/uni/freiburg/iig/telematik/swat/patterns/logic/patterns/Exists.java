package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns;

public abstract class Exists extends CandDPattern {
	
	protected final String mPatternName = "Exists P";
	
	protected final String mDescription = "P should occur at least once during process execution";
	
	@Override
	public String getName() {
		return mPatternName;
	}
	
	@Override
	public String getDescription() {
		return mDescription;
	}

}
