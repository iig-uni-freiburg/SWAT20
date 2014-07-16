package de.uni.freiburg.iig.telematik.swat.lukas;


public class Absent extends AtomicPattern {
	
	public static final String NAME = "Absent";
	
	public Absent(NetElementExpression op) {
		super("G(!" + op.toString() + ")");
		mOperands.add(op);
	}
	
	@Override
	public String getName() {
		return NAME;
	}
}
