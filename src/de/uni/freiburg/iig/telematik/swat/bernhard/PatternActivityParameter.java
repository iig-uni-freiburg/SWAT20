package de.uni.freiburg.iig.telematik.swat.bernhard;

import javax.swing.JComboBox;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;

public class PatternActivityParameter extends PatternParameterPanel {

	public PatternActivityParameter(String name, String[] values) {
		super(name, OperandType.TRANSITION);
		jComponent = new JComboBox(values);

	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return (String) ((JComboBox)jComponent).getSelectedItem();
	}

}
