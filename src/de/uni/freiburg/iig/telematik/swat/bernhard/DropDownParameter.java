package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;
/**
 * This class represents a DropDown Parameter, which will be displayed
 * in the pattern wizard
 * @author bernhard
 *
 */
public class DropDownParameter extends ParameterPanel {
	protected GuiParamType type;
	protected JComboBox valueBox;
	/**
	 * Create a DropDownParameter for parameter with given name, type
	 * and possible values
	 * @param name the name of the parameter e.g. "P"
	 * @param type the type of the parameter. Have a look at the enum OperandType
	 * @param values the possible values to choose
	 */
	public DropDownParameter(String name, GuiParamType type, String[] values) {
		super(name);
		this.type=type;
		valueBox = new JComboBox(values);
		content=new JPanel(new FlowLayout(FlowLayout.LEFT));
		content.add(valueBox);
	}

	@Override
	public List<GuiParamValue> getValue() {
		// TODO Auto-generated method stub
		ArrayList<GuiParamValue> list=new ArrayList<GuiParamValue>();
		list.add(new GuiParamValue((String) valueBox.getSelectedItem(), type));
		return list;
	}

	@Override
	public void setValue(List<GuiParamValue> val) {
		// TODO Auto-generated method stub
		// System.out.println("setze "+val);
		valueBox.setSelectedItem(val.get(0).getOperandName());
	}

}
