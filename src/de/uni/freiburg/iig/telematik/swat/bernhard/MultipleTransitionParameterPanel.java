package de.uni.freiburg.iig.telematik.swat.bernhard;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;

public class MultipleTransitionParameterPanel extends PatternMultipleParameterPanel {

	public MultipleTransitionParameterPanel(String name, String description,
			String[] pvalues) {
		super(name, description, pvalues);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected PatternParameterPanel addParameter() {
		// TODO Auto-generated method stub
		PatternParameterPanel p=new PatternDropDownParameterPanel(name, OperandType.TRANSITION,values);
		panelList.add(p);
		updateContent();
		return p;
	}
}
