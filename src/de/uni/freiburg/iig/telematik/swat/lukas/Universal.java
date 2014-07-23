package de.uni.freiburg.iig.telematik.swat.lukas;

public class Universal extends AtomicPattern {
	
	public static final String NAME = "Universal P";

	public Universal(NetElementExpression op) {
		super("G" + op.toString(), "P must occur or be valid through the process.");
		mOperands.add(op);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

}
