package de.uni.freiburg.iig.telematik.swat.lukas;

public class LeadsToChain extends AtomicPattern {
	
	public static final String NAME = "P Leads-To-Chain Q, R"; 
	
	public static final String DESC = "P must be followed by a sequence of Q, R.";
	
	public LeadsToChain(NetElementExpression op1, NetElementExpression op2, NetElementExpression op3) {
		super("G(" + op1.toString() + " => (F(" + op2.toString() + " & "
				+ "(X(F" + op3.toString() + ")))))");
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
