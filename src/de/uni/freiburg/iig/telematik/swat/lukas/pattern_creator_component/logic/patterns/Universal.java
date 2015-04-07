package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns;


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
