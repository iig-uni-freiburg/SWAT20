package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns;


public abstract class MBounded extends OrganizationalPattern {

	private final String mName = "(T1, T2,...,Tn) MBounded S";
	
	private final String mDescription = "The same subject S must perform T1,...,Tn";

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
