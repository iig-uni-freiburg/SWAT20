package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns;


public abstract class CoAbsent extends CandDPattern {

	private final String mName = "P CoAbsent Q";
	
	private final String mDescription = "The absence of P mandates that Q is also absent";
	
	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
