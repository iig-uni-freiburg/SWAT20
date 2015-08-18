package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns;


public abstract class PLeadsToQ extends CandDPattern {

	
	private final String mName = "P PLeadsTo Q";
	
	private final String mDescription = "P must be followed by Q and P must lead to Q";

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
