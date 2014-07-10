package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.ArrayList;

import javax.swing.JComboBox;

public class PatternActivityParameter extends PatternParameterPanel {

	public PatternActivityParameter(String name, String[] values) {
		super(name, "activity");
		jComponent=new JComboBox(values);

	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return (String) ((JComboBox)jComponent).getSelectedItem();
	}

}
