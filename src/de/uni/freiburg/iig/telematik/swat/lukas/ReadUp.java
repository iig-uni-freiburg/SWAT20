package de.uni.freiburg.iig.telematik.swat.lukas;

public class ReadUp extends InformationFlowPattern {

	public static final String NAME = "Read-Up";
	public static final String DESC = "A Low-User has unauthorized read Access to High-Data D.";
	
	public ReadUp(Transition t, Token token) {
		super("F(" + t.toString() + ")");
		mOperands.add(t);
		mOperands.add(token);
	}

	@Override
	public boolean isAntiPattern() {
		return true;
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
