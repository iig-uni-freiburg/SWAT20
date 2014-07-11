package de.uni.freiburg.iig.telematik.swat.lukas;

public class CoExists extends CompositePattern {
	
	public static final String NAME = "Co-Exists";
	
	public CoExists(NetElementExpression op1, NetElementExpression op2) {
		super("(!(F" + op1.toString() + ")) | (F" + op2.toString()+")");
		mOperands.add(op1);
		mOperands.add(op2);
	}

}
