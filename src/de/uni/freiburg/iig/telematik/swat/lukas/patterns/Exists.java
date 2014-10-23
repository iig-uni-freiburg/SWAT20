package de.uni.freiburg.iig.telematik.swat.lukas.patterns;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.StateExpression;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Transition;

public class Exists extends AtomicPattern {
	
	public static final String NAME = "Exists P";
	
	public static final String DESC = "P must exist in the process.";

	public Exists(Transition t, AbstractPlace<?,?> outp) {
		super("F" + t.toString(), "E[F (" + outp.getName() + "_black=1) & " + t.getNegation().replace("_last", "") + "]");
		mOperands.add(t);
	}
	
	public Exists(StateExpression sp) {
		super("F" + sp.toString());
		mOperands.add(sp);
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
