package de.uni.freiburg.iig.telematik.swat.bernhard;

import javax.swing.JComboBox;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;

public class PatternDataParameter extends PatternParameterPanel {

	public PatternDataParameter(String name, String values[]) {
		super(name, OperandType.TOKEN);
		jComponent=new JComboBox(values);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return (String) ((JComboBox)jComponent).getSelectedItem();
	}

}
