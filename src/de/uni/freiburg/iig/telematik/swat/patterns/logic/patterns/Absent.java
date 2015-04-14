package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns;

public abstract class Absent extends CandDPattern {
	
	protected final String mPatternName = "Absent P";
	
	protected final String mDescription = "P should never occur during process execution";
	
	@Override
	public String getName() {
		return mPatternName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}


}
