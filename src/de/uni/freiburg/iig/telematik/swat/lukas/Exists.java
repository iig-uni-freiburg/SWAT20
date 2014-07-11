package de.uni.freiburg.iig.telematik.swat.lukas;

public class Exists extends AtomicPattern {
	
	public static final String NAME = "Exists";

	public Exists(NetElementExpression op1) {
		super("F" + op1.toString());
		mOperands.add(op1);
	}

}
