package de.uni.freiburg.iig.telematik.swat.lukas;

public class CoAbsent extends CompositePattern {
	
	public static final String NAME = "Co Absent";
	
	public CoAbsent(NetElementExpression op1, NetElementExpression op2) {
		super("!(G(!" + op1.toString() + ")) | " + "(G(!" + op2.toString() + "))");
		mOperands.add(op1);
		mOperands.add(op2);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

}
