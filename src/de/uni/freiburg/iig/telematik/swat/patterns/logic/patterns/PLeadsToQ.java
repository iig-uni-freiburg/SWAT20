package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns;


public abstract class PLeadsToQ extends CandDPattern {

	
	private String mName = "P PLeadsTo Q";
	
	private String mDescription = "P must be followed by Q and P must lead to Q";

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
