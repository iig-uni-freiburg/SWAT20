package de.uni.freiburg.iig.telematik.swat.lukas;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;

public class Corequisite extends CompositePattern {
	
	public static final String NAME = "P Co-Requisite Q";
	
	public static final String DESC = "Both P and Q must be present or absent.";

	public Corequisite(StateExpression t1, StateExpression t2) {
		super("((F" + t1.toString() + ") & (F" + t2.toString() + ")) | "
				+ "(!(F(" + t1.toString() + ")) & (!(F(" + t2.toString() + "))))");
		mOperands.add(t1);
		mOperands.add(t2);
	}
	
	public Corequisite(Transition t1, Transition t2, AbstractPlace<?,?> p) {
		super("((F" + t1.toString() + ") & (F" + t2.toString() + ")) | "
				+ "(!(F(" + t1.toString() + ")) & (!(F(" + t2.toString() + "))))", 
				"E[F((" + p.getName() + "_black=1) & (((" + t1.getName() + "=1) & (" + t2.getName() + "=0)) | ((" + t1.getName() + "=0) & (" + t2.getName() + "=1))))]");
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
