package de.uni.freiburg.iig.telematik.swat.bernhard;
/**
 * This class represents a Parameter with infinite values
 * A value can have the type state predicate or transition
 * @author bernhard
 *
 */
public class MultipleTransitionOrStatePredicateParameterPanel extends MultipleParameterPanel {
	/**
	 * 
	 * @param name the name of the parameter
	 * @param description description of the type of the parameter
	 * @param pr An object implementing the interface PNReader to retrieve
	 * the needed information
	 */
	public MultipleTransitionOrStatePredicateParameterPanel(String name, String description,PNReader pr) {
		super(name, description, pr);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ParameterPanel getNewPanel() {
		// TODO Auto-generated method stub
		PNReader pnInformation=(PNReader) informationReader;
		return new ActivityOrStatePredicateParameter(name, pnInformation);
	}
}
