package de.uni.freiburg.iig.telematik.swat.lukas;

public class MutexChoice extends CompositePattern {
	
	public static final String NAME = "P Mutex-Choice Q";

	public MutexChoice(NetElementExpression op1, NetElementExpression op2) {
		super("((F" + op1.toString() + ") & (G(!" + op2.toString() + "))) | "
				+ "((F" + op2.toString() + ") & (G(!" + op1.toString() + ")))", "Either P or Q must be present.");
		mOperands.add(op1);
		mOperands.add(op2);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

}
