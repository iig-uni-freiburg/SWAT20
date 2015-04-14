package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns;


public abstract class CoExists extends CandDPattern {

	private final String mName = "P CoExists Q";
	
	private final String mDescription = "The presence of P mandates that Q is also present";
	
	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
