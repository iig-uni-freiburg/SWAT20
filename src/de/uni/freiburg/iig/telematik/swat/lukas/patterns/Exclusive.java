package de.uni.freiburg.iig.telematik.swat.lukas.patterns;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.StateExpression;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Activity;

public class Exclusive extends ControlAndDataflowPattern {
	
	public static final String NAME = "P Exclusive Q";
	
	public static final String DESC = "If P is present then Q must be absent and vice versa.";

	public Exclusive(StateExpression op1, StateExpression op2) {
		super("(!(F" + op1.toString() + ") | (G(!" + op2.toString() 
				+ "))) & (!(F"  + op2.toString() + ") | (G(!" 
				+ op1.toString() + ")))");
		mOperands.add(op1);
		mOperands.add(op2);
	}
	
	public Exclusive(Activity t1, Activity t2, AbstractPlace<?, ?> p) {
		super("(!(F" + t1.toString() + ") | (G(!" + t2.toString() 
				+ "))) & (!(F"  + t2.toString() + ") | (G(!" 
				+ t1.toString() + ")))", "E[F((" + p.getName() + "_black=1) & "
						+ " (" + t1.getName() + "=1) & (" +t2.getName() + "=1))]");
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
