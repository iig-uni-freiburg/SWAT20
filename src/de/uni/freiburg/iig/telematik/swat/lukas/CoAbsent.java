package de.uni.freiburg.iig.telematik.swat.lukas;

public class CoAbsent extends CompositePattern {
	
	public CoAbsent(Operand op1, Operand op2) {
		super("!G(!" + op1.toString() + ") | " + "G(!" + op2.toString() + ")");
		mOperands.add(op1);
		mOperands.add(op2);
	}

}
