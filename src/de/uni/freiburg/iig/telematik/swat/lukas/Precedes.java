package de.uni.freiburg.iig.telematik.swat.lukas;


public class Precedes extends AtomicPattern {
	
	public static final String NAME = "P Precedes Q";
	
	public static final String DESC = "Q must always be preceded by P.";
	
	public Precedes(StateExpression op1, StateExpression op2) {
		super("((G (!" + op2.toString() + ")) | ((!" + op2.toString() + ") U " + op1.toString() + ")"+ ")");
		mOperands.add(op1);
		mOperands.add(op2);
	}
	
	public Precedes(Transition t1, Transition t2) {
		super("((G (!" + t2.toString() + ")) | ((!" + t2.toString() + ") U " + t1.toString() + ")"+ ")",
				"A[G(" + t2.toString() + " => " + t1.toString().replace("_last", "") + ")]");
		mOperands.add(t1);
		mOperands.add(t2);
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
