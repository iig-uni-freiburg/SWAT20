package de.uni.freiburg.iig.telematik.swat.lukas;

public class Corequisite extends CompositePattern {
	
	public static final String NAME = "P Co-Requisite Q";

	public Corequisite(NetElementExpression t1, NetElementExpression t2) {
		super("((F" + t1.toString() + ") & (F" + t2.toString() + ")) | "
				+ "(!(F(" + t1.toString() + ")) & (!(F(" + t2.toString() + "))))", "Both P and Q must be present or absent.");
		mOperands.add(t1);
		mOperands.add(t2);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

}
