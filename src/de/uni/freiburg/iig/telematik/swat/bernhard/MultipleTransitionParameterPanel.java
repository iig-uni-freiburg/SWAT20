package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.List;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;

public class MultipleTransitionParameterPanel extends MultipleParameterPanel {

	public MultipleTransitionParameterPanel(String name, String description, LogFileReader ir) {
		super(name, description, ir);
		// TODO Auto-generated constructor stub
	}

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
