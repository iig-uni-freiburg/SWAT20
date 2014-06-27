package de.uni.freiburg.iig.telematik.swat.lukas;

public class Absent extends AtomicPattern {

	public Absent(Operand op) {
		super("G(!" + op.toString() + ")");
		mOperands.add(op);
	}

}
