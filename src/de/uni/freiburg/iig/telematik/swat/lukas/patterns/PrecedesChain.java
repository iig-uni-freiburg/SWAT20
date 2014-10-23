package de.uni.freiburg.iig.telematik.swat.lukas.patterns;

import de.uni.freiburg.iig.telematik.swat.lukas.operands.NetElementExpression;

public class PrecedesChain extends AtomicPattern {
	
	public static final String NAME = "P Precedes-Chain Q, R";
	
	public static final String DESC = "P must precede the sequence Q, R.";
	
	public PrecedesChain(NetElementExpression op1, NetElementExpression op2, NetElementExpression op3) {
		super("(F(" + op2.toString() + " & (X(F" + op3.toString() + ")))) => "
				+ "((!" + op2.toString() + ") U " + op1.toString() + ")");
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
