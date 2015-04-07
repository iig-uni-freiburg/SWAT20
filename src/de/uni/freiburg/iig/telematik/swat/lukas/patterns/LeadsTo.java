package de.uni.freiburg.iig.telematik.swat.lukas.patterns;

import de.uni.freiburg.iig.telematik.swat.lukas.operands.NetElementExpression;

public class LeadsTo extends ControlAndDataflowPattern {
	
	public static final String NAME = "P Leads-To Q";
	
	public static final String DESC = "P must always be followed by Q.";
	
	public LeadsTo(NetElementExpression op1, NetElementExpression op2) {
		super("G(" + op1.toString() + " => (" + "F" + op2.toString() + "))");
		mOperands.add(op1);
		mOperands.add(op2);
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
