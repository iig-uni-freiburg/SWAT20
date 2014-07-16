package de.uni.freiburg.iig.telematik.swat.lukas;

public class Universal extends AtomicPattern {
	
	public static final String NAME = "Universal";

	public Universal(NetElementExpression op) {
		super("G" + op.toString());
		mOperands.add(op);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

}
