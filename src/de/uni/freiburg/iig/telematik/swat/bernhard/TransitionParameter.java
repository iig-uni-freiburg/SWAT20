package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.ParamValue;

/**
 * This Class represents a parameter panel where a Transition of an
 * PN can be chosen.
 * @author bernhard
 *
 */
public abstract class TransitionParameter extends DropDownParameter {

	/**
	 * Create an TransitionParameter with a given name for a given PNReader.
	 * @param name the name of the parameter
	 * @param pnReader an object implementing the interface PNReader which is
	 * used to retrieve the list of transitions
	 */
	public TransitionParameter(String name, String[] activities) {
		super(name, OperandType.TRANSITION, activities);
	}
	
}
