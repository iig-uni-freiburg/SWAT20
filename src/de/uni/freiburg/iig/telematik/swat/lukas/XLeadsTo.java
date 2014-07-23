package de.uni.freiburg.iig.telematik.swat.lukas;

public class XLeadsTo extends AtomicPattern {
	
	public static final String NAME = "P X-Leads-To Q";
	
	public XLeadsTo(NetElementExpression op1, NetElementExpression op2) {
		super("G(" + op1.toString() + " => " + "(X" + op2.toString() + "))", "Q must immediately follow P");
		mOperands.add(op1);
		mOperands.add(op2);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

}
