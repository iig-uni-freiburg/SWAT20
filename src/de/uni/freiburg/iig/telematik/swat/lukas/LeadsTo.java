package de.uni.freiburg.iig.telematik.swat.lukas;

public class LeadsTo extends AtomicPattern {
	
	public static final String NAME = "P Leads-To Q";
	
	public LeadsTo(NetElementExpression op1, NetElementExpression op2) {
		super("G(" + op1.toString() + " => (" + "F" + op2.toString() + "))", "P must always be followed by Q.");
		mOperands.add(op1);
		mOperands.add(op2);
	} 
	
	@Override
	public String getName() {
		return NAME;
	}

}
