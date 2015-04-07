package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns;


public abstract class LostData extends DataFlowAntiPattern {

	private final String mName = "Lost Data D";
	
	private final String mDescription = "A data item is written and in some possible continuations of the" +
			"business process it is overwritten before it has been read";

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
