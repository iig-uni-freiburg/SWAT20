package de.uni.freiburg.iig.telematik.swat.lukas;

public class ChainPrecedes extends AtomicPattern {
	
	public static final String NAME = "Chain Precedes";

	// op1 = S, op2 = T, op3 = P
	public ChainPrecedes(NetElementExpression op1, NetElementExpression op2, NetElementExpression op3, boolean sequencePrecedes) {
		
		String formula;
		mOperands.add(op1);
		mOperands.add(op2);
		mOperands.add(op3);
		
		if (sequencePrecedes) {
			formula = "F" + op3.toString() + " => ((!" + op3.toString() 
					+ ") U (" + op1.toString() + " & (!" + op3.toString() 
					+ ") & (X((!" + op3.toString() + ") U " + op2.toString() + "))))"; 
		} else {
			formula ="(F(" + op2.toString() + " & (X(F" + op3.toString() + ")))) => ((!" + op2.toString() + ") U " + op1.toString() + ")";
		}
		
		setPattern(formula, false);
	}
}