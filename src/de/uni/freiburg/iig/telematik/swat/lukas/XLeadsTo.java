package de.uni.freiburg.iig.telematik.swat.lukas;

public class XLeadsTo extends AtomicPattern {
	
	public static final String NAME = "X-Leads To";
	
	public XLeadsTo(NetElementExpression op1, NetElementExpression op2) {
		super("G(" + op1.toString() + " => " + "(X" + op2.toString() + "))");
		mOperands.add(op1);
		mOperands.add(op2);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

}
