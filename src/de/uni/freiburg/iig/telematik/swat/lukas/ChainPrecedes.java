package de.uni.freiburg.iig.telematik.swat.lukas;

public class ChainPrecedes extends AtomicPattern {
	
	public static final String NAME = "P, Q Chain-Precedes R";
	
	public static final String DESC = "R must be preceded by a sequence P, Q.";

	// op1 = S, op2 = T, op3 = P
	public ChainPrecedes(NetElementExpression op1, NetElementExpression op2, NetElementExpression op3) {
		super("(F" + op3.toString() + ") => ((!" + op3.toString() 
				+ ") U (" + op1.toString() + " & (!" + op3.toString() 
				+ ") & (X((!" + op3.toString() + ") U " + op2.toString() + "))))");
		mOperands.add(op1);
		mOperands.add(op2);
		mOperands.add(op3);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESC;
	}
	
}