package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns;


public abstract class ChainPrecedes extends CandDPattern {

	private final String mName = "(P, Q) ChainPrecedes R";
	
	private final String mDescription = "R must be preceded by a sequence of P and Q";
	
	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
