package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns;


public abstract class RedundantData extends DataFlowAntiPattern {

	private final String mName = "Redundant Data D";
	
	private final String mDescription = "A data item is written and in some possible continuations" +
			"of the business process it is never read before it is destroyed or the process terminates";
	
	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
