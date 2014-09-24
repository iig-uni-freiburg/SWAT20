package de.uni.freiburg.iig.telematik.swat.lukas;


public class Absent extends AtomicPattern {
	
	public static final String NAME = "Absent P";
	
	public static final String DESC = "The process must be free of P";
	
	public Absent(StateExpression op) {
		super("G(!" + op.toString() + ")");
		mOperands.add(op);
	}
	
	public Absent(Transition t) {
		super("G(!" + t.toString() + ")", "A[G " + t.getNegation().replace("_last", "") + "]");
		mOperands.add(t);
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
