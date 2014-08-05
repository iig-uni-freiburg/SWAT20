package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.List;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;

public class MultipleTransitionParameterPanel extends PatternMultipleParameterPanel {

	public MultipleTransitionParameterPanel(String name, String description, InformationReader ir) {
		super(name, description, ir);
		// TODO Auto-generated constructor stub
	}

	public MultipleTransitionParameterPanel(String name, String description, InformationReader ir, int limit) {
		super(name, description, ir, limit);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected PatternParameterPanel getNewPanel() {
		// TODO Auto-generated method stub
		List<String> transitionList=informationReader.getActivities();
		return new PatternDropDownParameter(name, OperandType.TRANSITION, transitionList.toArray(new String[transitionList.size()]));
	}

}
