package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns;


public abstract class LeadsTo extends CandDPattern {

	
	private final String mName = "P LeadsTo Q";
	
	private final String mDescription = "P must always be followed by Q";

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
