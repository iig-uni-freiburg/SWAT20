package de.uni.freiburg.iig.telematik.swat.lukas;

public class ChainLeadsTo extends AtomicPattern {
	
	public ChainLeadsTo(Operand op1, Operand op2, Operand op3) {
		super("(G(" + op1.toString() + " & X(F" + op2.toString() 
				+ ") -> X(F(" + op2.toString() + " & F" 
				+ op3.toString() + "))))");
		mOperands.add(op1);
		mOperands.add(op2);
		mOperands.add(op3);
	}

}
