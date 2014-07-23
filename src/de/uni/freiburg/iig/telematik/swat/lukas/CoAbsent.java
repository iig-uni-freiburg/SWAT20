package de.uni.freiburg.iig.telematik.swat.lukas;

public class CoAbsent extends CompositePattern {
	
	public static final String NAME = "P Co-Absent Q";
	
	public CoAbsent(NetElementExpression op1, NetElementExpression op2) {
		super("!(G(!" + op1.toString() + ")) | " + "(G(!" + op2.toString() + "))", "If P is absent, then Q must be absent.");
		mOperands.add(op1);
		mOperands.add(op2);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

}
