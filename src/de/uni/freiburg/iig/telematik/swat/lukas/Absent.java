package de.uni.freiburg.iig.telematik.swat.lukas;


public class Absent extends AtomicPattern {
	
	public static final String NAME = "Absent P";
	
	public Absent(NetElementExpression op) {
		super("G(!" + op.toString() + ")", "The process must be free of P");
		mOperands.add(op);
	}
	
	@Override
	public String getName() {
		return NAME;
	}
}
