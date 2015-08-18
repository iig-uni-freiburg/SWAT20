package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns;


public abstract class PerformedBy extends OrganizationalPattern {

	private final String mName = "T PerformedBy R";
	
	private final String mDescription = "T has to be performed by a user with role R";

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
