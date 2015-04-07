package de.uni.freiburg.iig.telematik.swat.lukas.operands;

public abstract class NetElementExpression extends PatternParameter {
	
	/**
	 * Places and Transitions of petri nets are refered to as net elements. A NetElementExpression
	 * refers to the firing of a transition/activity or the reach of a certain set of states. 
	 * Those states are described by a marking of the petri net.    
	 *
	 * @author Lukas Sättler
	 * @version 1.0
	 */
	public abstract String getNegation();

}
