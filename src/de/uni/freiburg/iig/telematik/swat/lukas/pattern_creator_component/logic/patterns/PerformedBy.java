package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns;


public abstract class PerformedBy extends OrganizationalPattern {

	private String mName = "T PerformedBy R";
	
	private String mDescription = "T has to be performed by a user with role R";

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
