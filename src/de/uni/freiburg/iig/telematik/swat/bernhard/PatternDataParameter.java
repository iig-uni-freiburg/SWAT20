package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.List;
import java.util.ArrayList;

import javax.swing.JComboBox;

public class PatternDataParameter extends PatternParameterPanel {

	public PatternDataParameter(String name, String values[]) {
		super(name, "data");
		jComponent=new JComboBox(values);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return (String) ((JComboBox)jComponent).getSelectedItem();
	}

}
