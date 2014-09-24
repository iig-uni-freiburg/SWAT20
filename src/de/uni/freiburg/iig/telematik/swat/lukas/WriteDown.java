package de.uni.freiburg.iig.telematik.swat.lukas;

public class WriteDown extends InformationFlowPattern {

	static final String NAME = "Write Down";
	static final String DESC = "A High-User reveals information by writing to Low-Data.";

	public WriteDown(Transition t, Token token) {
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
