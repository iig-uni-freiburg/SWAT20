package de.uni.freiburg.iig.telematik.swat.lukas;

public class LeadsTo extends AtomicPattern {
	
	public static final String NAME = "Leads To";
	
	public LeadsTo(NetElementExpression op1, NetElementExpression op2) {
		super("G(" + op1.toString() + " => (" + "F" + op2.toString() + "))");
		mOperands.add(op1);
		mOperands.add(op2);
	} 
	
	@Override
	public String getName() {
		return NAME;
	}

}
