package de.uni.freiburg.iig.telematik.swat.lukas;


public class Absent extends AtomicPattern {
	
	public static final String NAME = "Absent P";
	
	public static final String DESC = "The process must be free of P";
	
	public Absent(NetElementExpression op) {
		super("G(!" + op.toString() + ")");
		mOperands.add(op);
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
