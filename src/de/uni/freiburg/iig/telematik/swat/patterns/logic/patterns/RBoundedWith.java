package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns;


public abstract class RBoundedWith extends OrganizationalPattern {

	private final String mName = "(T1, T2) RBoundedWith R1";
	
	private final String mDescription = "T1 and T2 are performed by the same role R1, but different subjects"
			+ "must perform them";

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
