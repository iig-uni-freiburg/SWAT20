package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.ParamValue;

public class PatternActivityParameterPanel extends PatternParameterPanel {

	public PatternActivityParameterPanel(String name, String[] values) {
		super(name);
		
		content = new JComboBox(values);

	}

	@Override
	public List<ParamValue> getValue() {
		// TODO Auto-generated method stub
		ArrayList<ParamValue> list=new ArrayList<ParamValue>();
		list.add(new ParamValue((String) ((JComboBox)content).getSelectedItem(), OperandType.TRANSITION));
		return list;
	}

	@Override
	public void setValue(List<ParamValue> val) {
		// TODO Auto-generated method stub
		// System.out.println("setze "+val);
		((JComboBox)content).setSelectedItem(val.get(0).getOperandName());
	}

}
