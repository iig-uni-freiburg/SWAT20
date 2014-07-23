package de.uni.freiburg.iig.telematik.swat.lukas;


public class Precedes extends AtomicPattern {
	
	public static final String NAME = "P Precedes Q";
	
	public Precedes(NetElementExpression op1, NetElementExpression op2) {
		super("((G " + op1.toString() + ") | (!" + op2.toString() + " U " + op1.toString() + ")"+ ")", "Q must always be preceded by P.");
		mOperands.add(op1);
		mOperands.add(op2);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

}
