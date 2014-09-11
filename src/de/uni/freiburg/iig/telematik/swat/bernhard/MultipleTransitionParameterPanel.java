package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.List;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
/**
 * This class represents a parameter that accepts several activities as values
 * @author bernhard
 *
 */
public class MultipleTransitionParameterPanel extends MultipleParameterPanel {
	/**
	 * Create a MultipleTransitionParameterPanel that accepts infinite values
	 * @param name the name of the parameter
	 * @param description a description of the type of the values
	 * @param ir An object implementing LogFileReader that can be used to retrieve the necessary information
	 */
	public MultipleTransitionParameterPanel(String name, String description, LogFileReader ir) {
		super(name, description, ir);
		// TODO Auto-generated constructor stub
	}
	/**
	 * Create a MultipleTransitionParameterPanel that accepts a limmited amount of values
	 * @param name the name of the parameter
	 * @param description a description of the type of the values
	 * @param ir An object implementing LogFileReader that can be used to retrieve the necessary information
	 * @param limit maximum amount of values
	 */
	public MultipleTransitionParameterPanel(String name, String description, LogFileReader ir, int limit) {
		super(name, description, ir, limit);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ParameterPanel getNewPanel() {
		// TODO Auto-generated method stub
		return new DropDownParameter(name, OperandType.TRANSITION, informationReader.getActivities());
	}

}
