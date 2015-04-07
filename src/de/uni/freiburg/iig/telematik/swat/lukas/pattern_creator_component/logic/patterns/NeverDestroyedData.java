package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns;


public abstract class NeverDestroyedData extends DataFlowAntiPattern {

	
	private final String mName = "Never Destroyed Data D";
	
	private final String mDescription = "There exists a process execution such that, a data item is" +
			"written, but not destroyed before the process terminates.";

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
