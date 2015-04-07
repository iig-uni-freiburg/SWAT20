package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns;


public abstract class ChainLeadsTo extends CandDPattern {

	private final String mName = "(P, Q) ChainLeadsTo R";
	
	private final String mDescription = "A sequence of P, Q must be followed by R";

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
