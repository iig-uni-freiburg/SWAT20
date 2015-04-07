package de.uni.freiburg.iig.telematik.swat.lukas.patterns;

import de.uni.freiburg.iig.telematik.swat.lukas.operands.NetElementExpression;

public class XLeadsTo extends ControlAndDataflowPattern {
	
	public static final String NAME = "P X-Leads-To Q";
	
	public static final String DESC = "Q must immediately follow P";
	
	public XLeadsTo(NetElementExpression op1, NetElementExpression op2) {
		super("G(" + op1.toString() + " => " + "(X" + op2.toString() + "))");
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
