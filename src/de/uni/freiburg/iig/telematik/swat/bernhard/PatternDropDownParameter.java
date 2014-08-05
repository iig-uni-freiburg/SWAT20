package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.ParamValue;

public class PatternDropDownParameter extends PatternParameterPanel {

	private OperandType type;
	public PatternDropDownParameter(String name, OperandType type, String[] values) {
		super(name);
		this.type=type;
		content = new JComboBox(values);
	}

	@Override
	public List<ParamValue> getValue() {
		// TODO Auto-generated method stub
		ArrayList<ParamValue> list=new ArrayList<ParamValue>();
		list.add(new ParamValue((String) ((JComboBox)content).getSelectedItem(), type));
		return list;
	}

	@Override
	public void setValue(List<ParamValue> val) {
		// TODO Auto-generated method stub
		// System.out.println("setze "+val);
		((JComboBox)content).setSelectedItem(val.get(0).getOperandName());
	}

}
