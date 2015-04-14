package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns;

public abstract class Corequisite extends CandDPattern {

	private final String mName = "P Corequisite Q";
	
	private final String mDescription = "P and Q only occur together";
	
	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
