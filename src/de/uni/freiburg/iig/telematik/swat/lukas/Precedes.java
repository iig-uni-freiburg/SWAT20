package de.uni.freiburg.iig.telematik.swat.lukas;

public class Precedes extends AtomicPattern {
	
	
	public Precedes(Operand op1, Operand op2) {
		super("((G " + op1.toString() + ") | (!" + op2.toString() + " U " + op1.toString() + ")"+ ")");
		mOperands.add(op1);
		mOperands.add(op2);
	}

}
