package de.uni.freiburg.iig.telematik.swat.lukas;

public class PBNI extends InformationFlowPattern {

	public static final String NAME = "PBNI+";
	public static final String DESC = "A Low-Level User can deduce that a High-Level Activity was performed.";
	
	@Override
	public boolean isAntiPattern() {
		return false;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESC;
	}

}
