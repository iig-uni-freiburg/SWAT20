package de.uni.freiburg.iig.telematik.swat.lukas;


public class Precedes extends AtomicPattern {
	
	public static final String NAME = "P Precedes Q";
	
	public static final String DESC = "Q must always be preceded by P.";
	
	public Precedes(NetElementExpression op1, NetElementExpression op2) {
		super("((G (!" + op2.toString() + ")) | (!" + op2.toString() + " U " + op1.toString() + ")"+ ")");
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
