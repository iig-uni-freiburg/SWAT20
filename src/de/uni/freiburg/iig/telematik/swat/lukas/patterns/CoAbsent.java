package de.uni.freiburg.iig.telematik.swat.lukas.patterns;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.StateExpression;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Activity;

public class CoAbsent extends ControlAndDataflowPattern {
	
	public static final String NAME = "P Co-Absent Q";
	
	public static final String DESC = "If P is absent, then Q must be absent.";
	
	public CoAbsent(StateExpression op1, StateExpression op2) {
		super("!(G(!" + op1.toString() + ")) | " + "(G(!" + op2.toString() + "))");
		mOperands.add(op1);
		mOperands.add(op2);
	}
	
	public CoAbsent(Activity t1, Activity t2, AbstractPlace<?,?> outPlace) {
		super("!(G(!" + t1.toString() + ")) | " + "(G(!" + t2.toString() + "))", 
				"E[F((" + outPlace.getName() + "_black=1) & (" + t1.getName() + "=0) & (" +t2.getName() + "=1))]");
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
