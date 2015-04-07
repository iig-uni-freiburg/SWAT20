package de.uni.freiburg.iig.telematik.swat.lukas.operands;

public abstract class PatternParameter {

/**
 * A PatternParameter is the internal representation of a parameter which is used to in order to
 * instantiate a compliance pattern. It is created based on a gui parameter which was entered using
 * the SWAT Gui (the pattern dialog). There are different types of PatternParameters, e.g. Activity, 
 * Role, User, StateExpression.
 *
 * @author Lukas Sättler
 * @version 1.0
 */
	public abstract String toString();
	
	public abstract String getName();

}
