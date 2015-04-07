package de.uni.freiburg.iig.telematik.swat.lukas.operands;

public abstract class StateExpression extends NetElementExpression {
	
	/**
	 * A StateExpression describes a set of states by defining requirements on the marking of a 
	 * petri net. A StateExpression specifies a quantity of tokens in a set of states. 
	 * Example: (p1_black=1 and p2_green>1) is a StateExpression which describes all states with
	 * on black token in p1 and at least 1 green token in place p2.  
	 *
	 * @author Lukas Sättler
	 * @version 1.0
	 */
	
	public abstract String toString();

}
