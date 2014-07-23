package de.uni.freiburg.iig.telematik.swat.lukas;

public class Exists extends AtomicPattern {
	
	public static final String NAME = "Exists P";

	public Exists(NetElementExpression op1) {
		super("F" + op1.toString(),  "P must exist in the process.");
		mOperands.add(op1);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

}
