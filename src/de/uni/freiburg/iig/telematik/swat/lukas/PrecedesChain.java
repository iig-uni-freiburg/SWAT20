package de.uni.freiburg.iig.telematik.swat.lukas;

public class PrecedesChain extends AtomicPattern {
	
	public static final String NAME = "P Precedes-Chain Q, R";
	
	public PrecedesChain(NetElementExpression op1, NetElementExpression op2, NetElementExpression op3) {
		super("(F(" + op2.toString() + " & (X(F" + op3.toString() + ")))) => ((!" + op2.toString() + ") U " + op1.toString() + ")",
				"P must precede the sequence Q, R.");
		mOperands.add(op1);
		mOperands.add(op2);
		mOperands.add(op3);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

}
