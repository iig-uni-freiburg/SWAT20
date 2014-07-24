package de.uni.freiburg.iig.telematik.swat.lukas;

public class Universal extends AtomicPattern {
	
	public static final String NAME = "Universal P";
	
	public static final String DESC = "P must occur or be valid through the process.";

	public Universal(NetElementExpression op) {
		super("G" + op.toString());
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
