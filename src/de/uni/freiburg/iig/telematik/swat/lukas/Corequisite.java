package de.uni.freiburg.iig.telematik.swat.lukas;

public class Corequisite extends CompositePattern {
	
	public static final String NAME = "Co-Requisite";

	public Corequisite(NetElementExpression t1, NetElementExpression t2) {
		super("((F" + t1.toString() + ") & (F" + t2.toString() + ")) | "
				+ "(!(F(" + t1.toString() + ")) & (!(F(" + t2.toString() + "))))");
		mOperands.add(t1);
		mOperands.add(t2);
	}

}
