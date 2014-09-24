package de.uni.freiburg.iig.telematik.swat.lukas;

public class BottomPattern extends CompliancePattern {

	public BottomPattern() {
		super("false", false);
	}

	@Override
	public boolean isAntiPattern() {
		return false;
	}

	@Override
	public String getName() {
		return "Bottom";
	}

	@Override
	public String getDescription() {
		return "The bottom pattern is never satisfied!";
	}

}
