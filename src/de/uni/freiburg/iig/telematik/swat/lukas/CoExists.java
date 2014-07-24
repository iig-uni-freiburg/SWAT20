package de.uni.freiburg.iig.telematik.swat.lukas;

public class CoExists extends CompositePattern {
	
	public static final String NAME = "P Co-Exists  Q";
	
	public static final String DESC = "If P is present, then Q must also be present.";
	
	public CoExists(NetElementExpression op1, NetElementExpression op2) {
		super("(!(F" + op1.toString() + ")) | (F" + op2.toString()+")");
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
