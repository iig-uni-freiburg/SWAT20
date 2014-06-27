package de.uni.freiburg.iig.telematik.swat.lukas;

public class ChainPrecedes extends AtomicPattern {

	public ChainPrecedes(Operand op1, Operand op2, Operand op3) {
		super("(F" + op3.toString() + "->((!" + op3.toString() 
				+ ") U (" + op1.toString() + " & !" + op3.toString() 
				+ " & X((!" + op3.toString() + ") U " + op2.toString() + "))))");
		mOperands.add(op1);
		mOperands.add(op2);
		mOperands.add(op3);
	}
}
