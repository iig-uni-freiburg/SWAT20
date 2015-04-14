package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns;


public abstract class TwiceDestroyedData extends DataFlowAntiPattern {

	
	private final String mName = "Twice Destroyed Data D";
	
	private final String mDescription = "A data item is twice destroyed without having been created in between";

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
