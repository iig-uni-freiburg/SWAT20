package de.uni.freiburg.iig.telematik.swat.lukas;

public class Universal extends AtomicPattern {

	public Universal(Operand op) {
		super("G" + op.toString());
		mOperands.add(op);
	}

}
