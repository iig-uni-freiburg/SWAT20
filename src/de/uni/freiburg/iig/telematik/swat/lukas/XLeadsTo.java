package de.uni.freiburg.iig.telematik.swat.lukas;

public class XLeadsTo extends AtomicPattern {
	
	public XLeadsTo(Operand op1, Operand op2) {
		super("(G(" + op1.toString() + "->" + "X" + op2.toString() + "))");
		mOperands.add(op1);
		mOperands.add(op2);
	}

}
