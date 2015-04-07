package de.uni.freiburg.iig.telematik.swat.lukas.patterns;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Activity;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.StateExpression;

public class Exists extends ControlAndDataflowPattern {
	
	public static final String NAME = "Exists P";
	
	public static final String DESC = "P must exist in the process.";

	public Exists(Activity t, AbstractPlace<?,?> outp) {
		super("F" + t.toString(), "E[F (" + outp.getName() + "_black=1) & " + t.getNegation().replace("_last", "") + "]");
		mOperands.add(t);
		String activityName = t.getName();
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
