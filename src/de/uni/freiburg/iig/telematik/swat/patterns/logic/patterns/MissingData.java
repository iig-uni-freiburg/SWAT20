package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns;


public abstract class MissingData extends DataFlowAntiPattern {

	private final String mName = "Missing Data D";
	
	private final String mDescription = "A data item is to be read or destroyed, but was either never written "
			+ "before or it was not written after it has been destroyed";

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
