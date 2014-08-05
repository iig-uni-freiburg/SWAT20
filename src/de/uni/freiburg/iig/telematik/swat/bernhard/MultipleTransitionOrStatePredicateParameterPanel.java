package de.uni.freiburg.iig.telematik.swat.bernhard;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;

public class MultipleTransitionOrStatePredicateParameterPanel extends PatternMultipleParameterPanel {


	public MultipleTransitionOrStatePredicateParameterPanel(String name, String description,PetriNetInformationReader pr) {
		super(name, description, pr);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected PatternParameterPanel getNewPanel() {
		// TODO Auto-generated method stub
		PetriNetInformationReader pnInformation=(PetriNetInformationReader) informationReader;
		return new PatternActivityOrStatePredicateParameter(name, pnInformation);
	}
}
