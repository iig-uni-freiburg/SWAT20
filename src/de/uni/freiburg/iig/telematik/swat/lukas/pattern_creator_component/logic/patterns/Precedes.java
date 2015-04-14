package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns;


public abstract class Precedes extends CandDPattern {

	private String mName = "P Precedes Q";
	
	private String mDescription = "Q must always be preceded by P";

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}