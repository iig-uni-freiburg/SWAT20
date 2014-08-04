package de.uni.freiburg.iig.telematik.swat.lukas;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;

public class CoExists extends CompositePattern {
	
	public static final String NAME = "P Co-Exists  Q";
	
	public static final String DESC = "If P is present, then Q must also be present.";
	
	public CoExists(Statepredicate op1, Statepredicate op2) {
		super("(!(F" + op1.toString() + ")) | (F" + op2.toString()+")");
		mOperands.add(op1);
		mOperands.add(op2);
	}
	
	public CoExists(Transition t1, Transition t2, AbstractPlace<?, ?> pOut) {
		super("(!(F" + t1.toString() + ")) | (F" + t2.toString()+")", 
				"E[F((" + pOut.getName() + "_black=1) & (" + t1.getName() + "=1) & (" + t2.getName() + "=0))]");
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
