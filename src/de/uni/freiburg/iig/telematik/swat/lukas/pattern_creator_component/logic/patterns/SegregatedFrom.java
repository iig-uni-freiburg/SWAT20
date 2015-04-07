package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns;


public abstract class SegregatedFrom extends OrganizationalPattern {

	private final String mName = "T1 SegregatedFrom T2";
	
	private final String mDescription = "T1 and T2 must be performed by different roles "
			+ "and different subjects";

	@Override
	public String getName() {
		return mName ;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
