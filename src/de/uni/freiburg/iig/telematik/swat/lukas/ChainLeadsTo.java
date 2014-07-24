package de.uni.freiburg.iig.telematik.swat.lukas;

public class ChainLeadsTo extends AtomicPattern {
	
	public static final String NAME = "P, Q Chain-Leads-To R";
	
	public static final String DESC = "A sequence of P, Q must be followed by R.";
	
	public ChainLeadsTo(NetElementExpression op1, NetElementExpression op2, NetElementExpression op3) {
		super("G((" + op1.toString() + " & (X(F" + op2.toString() + "))) => "
				+ "((X(F(" + op2.toString() + " & " + op3.toString()
				+ "))) | (X(F("
				+ op3.toString()
				+ ")))))");
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

