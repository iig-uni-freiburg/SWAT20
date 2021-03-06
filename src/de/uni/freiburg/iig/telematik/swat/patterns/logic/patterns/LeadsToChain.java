package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns;


public abstract class LeadsToChain extends CandDPattern {

	private final String mName = "P LeadsToChain (Q, R)";
	
	private final String mDescription = "P must lead to a sequence of Q and R";

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
