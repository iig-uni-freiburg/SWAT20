package de.uni.freiburg.iig.telematik.swat.lukas;

public class ChainPrecedes extends AtomicPattern {
	
	public static final String NAME = "P, Q Chain Precedes R";

	// op1 = S, op2 = T, op3 = P
	public ChainPrecedes(NetElementExpression op1, NetElementExpression op2, NetElementExpression op3, boolean sequencePrecedes) {
		super("F" + op3.toString() + " => ((!" + op3.toString() 
				+ ") U (" + op1.toString() + " & (!" + op3.toString() 
				+ ") & (X((!" + op3.toString() + ") U " + op2.toString() + "))))", "R must be preceded by a sequence P, Q.");
		mOperands.add(op1);
		mOperands.add(op2);
		mOperands.add(op3);
	}
	
	@Override
	public String getName() {
		return NAME;
	}
}