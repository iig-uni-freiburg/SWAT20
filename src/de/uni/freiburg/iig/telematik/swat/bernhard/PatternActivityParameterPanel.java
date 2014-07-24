package de.uni.freiburg.iig.telematik.swat.bernhard;

import javax.swing.JComboBox;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;

public class PatternActivityParameterPanel extends PatternParameterPanel {

	public PatternActivityParameterPanel(String name, String[] values) {
		super(name, OperandType.TRANSITION);
		jComponent = new JComboBox(values);

	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return (String) ((JComboBox)jComponent).getSelectedItem();
	}

	@Override
	public void setValue(String val) {
		// TODO Auto-generated method stub
		// System.out.println("setze "+val);
		((JComboBox)jComponent).setSelectedItem(val);
	}

}
