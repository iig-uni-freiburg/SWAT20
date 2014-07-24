package de.uni.freiburg.iig.telematik.swat.lukas;

public class Exists extends AtomicPattern {
	
	public static final String NAME = "Exists P";
	
	public static final String DESC = "P must exist in the process.";

	public Exists(NetElementExpression op1) {
		super("F" + op1.toString());
		mOperands.add(op1);
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
