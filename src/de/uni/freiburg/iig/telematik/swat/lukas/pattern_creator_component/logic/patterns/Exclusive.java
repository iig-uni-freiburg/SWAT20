package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns;


public abstract class Exclusive extends CandDPattern {

	private final String mName = "P Exclusive Q";
	
	private final String mDescription = "The presence of P mandates the absence of Q and vice versa";
	
	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
