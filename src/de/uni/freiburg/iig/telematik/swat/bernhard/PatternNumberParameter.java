package de.uni.freiburg.iig.telematik.swat.bernhard;

import javax.swing.JTextField;

public class PatternNumberParameter extends PatternParameterPanel {

	
	public PatternNumberParameter(String name) {
		super(name, "number");
		jComponent=new JTextField(10);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return ((JTextField)jComponent).getText();
	}

}
