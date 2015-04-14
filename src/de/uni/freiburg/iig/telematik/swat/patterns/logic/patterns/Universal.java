package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns;


public abstract class Universal extends CandDPattern {

	private final String mName = "P Universal";
	
	private final String mDescription = "P should always hold";

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
