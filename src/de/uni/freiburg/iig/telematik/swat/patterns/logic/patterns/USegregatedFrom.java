package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns;


public abstract class USegregatedFrom extends OrganizationalPattern {

	private final String mName = "T1 USegregatedFrom T1";
	
	private final String mDescription = "Different subjects must perform T1 and T2";

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
