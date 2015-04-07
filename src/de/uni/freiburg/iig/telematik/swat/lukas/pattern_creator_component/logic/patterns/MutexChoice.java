package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns;


public abstract class MutexChoice extends CandDPattern {


	private final String mName = "P MutexChoice Q";
	
	private final String mDescription = "Either P or Q exists, but not both of them";
	
	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
