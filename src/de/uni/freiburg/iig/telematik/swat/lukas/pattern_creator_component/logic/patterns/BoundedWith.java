package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns;


public abstract class BoundedWith extends OrganizationalPattern {

	private final String mName = "T1 BoundedWith T2";
	
	private final String mDescription = "T1 and T2 are performed by the same subject and role";

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

}
