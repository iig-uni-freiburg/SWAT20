package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns;


public abstract class PrecedesChain extends CandDPattern {

	private final String mName = "P PrecedesChain (Q, R)";
	
	private final String mDescription = "P precedes a sequence of Q and R";
	
	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
