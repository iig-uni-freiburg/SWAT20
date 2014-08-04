package de.uni.freiburg.iig.telematik.swat.lukas;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;

public class Universal extends AtomicPattern {
	
	public static final String NAME = "Universal P";
	
	public static final String DESC = "P must occur or be valid through the process.";

	public Universal(NetElementExpression op, AbstractPlace<?,?> inputP) {
		super("G" + op.toString(), "A[G(" + op.toString() + "| (" + inputP.getName() + "_black=1))]");
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
