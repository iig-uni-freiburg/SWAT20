package de.uni.freiburg.iig.telematik.swat.lukas;

public class LeadsTo extends AtomicPattern {
	
	public LeadsTo(Operand op1, Operand op2) {
		super("(G(" + op1.toString() + "->" + "F" + op2.toString() + "))");
		mOperands.add(op1);
		mOperands.add(op2);
	} 

}
