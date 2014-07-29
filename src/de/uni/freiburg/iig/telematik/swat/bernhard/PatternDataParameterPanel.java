package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.ParamValue;

public class PatternDataParameterPanel extends PatternParameterPanel {

	public PatternDataParameterPanel(String name, String values[]) {
		super(name);
		content=new JComboBox(values);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<ParamValue> getValue() {
		// TODO Auto-generated method stub
		ArrayList<ParamValue> list=new ArrayList<ParamValue>();
		list.add(new ParamValue((String) ((JComboBox)content).getSelectedItem(), OperandType.TOKEN));
		return list;
	}

	@Override
	public void setValue(List<ParamValue> val) {
		// TODO Auto-generated method stub
		// System.out.println("setze "+val);
		((JComboBox)content).setSelectedItem(val.get(0).getOperandName());
	}

}
