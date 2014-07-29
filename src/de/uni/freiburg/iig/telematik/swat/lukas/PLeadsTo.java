package de.uni.freiburg.iig.telematik.swat.lukas;

public class PLeadsTo extends CompositePattern {
	
    public static final String NAME = "P PLeadsTo Q";
	
	public static final String DESC = "P must precede Q and leads necessarily to Q.";

	
	public PLeadsTo(NetElementExpression op1, NetElementExpression op2) {
		super("((G (!" + op2.toString() + ")) | (!" + op2.toString() + " U " + op1.toString() + ")"+ ") & "
				+ "(G(" + op1.toString() + " => (" + "F" + op2.toString() + ")))");
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
