package de.uni.freiburg.iig.telematik.swat.lukas;

public class MutexChoice extends CompositePattern {

	public MutexChoice(Operand op1, Operand op2) {
		super("(F" + op1.toString() + " & G(!" + op2.toString() + ")) | "
				+ "(F" + op2.toString() + " & G(!" + op1.toString() + "))");
		mOperands.add(op1);
		mOperands.add(op2);
	}

}
