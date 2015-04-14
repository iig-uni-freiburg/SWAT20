package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns;


public abstract class DataNotDeletedOnTime extends DataFlowAntiPattern {


	private final String mName = "Data D Not Deleted On Time";
	
	private final String mDescription = "A data item is read and not destroyed and in all possible continuations of the process it is not read again";

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
