package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.ParamValue;

public class PatternSingleStatePredicateParameter extends PatternParameterPanel {

	private final String predicates[] = {"<", ">", "=", "!=", "<=", ">=" };
	private JSpinner numberSpinner;
	private JComboBox relationsBox;
	private JComboBox placesBox;
	private JComboBox colorsBox;
	public PatternSingleStatePredicateParameter(String name, String places[], String colors[]) {
		super(name);
		// TODO Auto-generated constructor stub
		placesBox=new JComboBox(places);
		relationsBox=new JComboBox(predicates);
		colorsBox = new JComboBox(colors);
		SpinnerModel model =
		        new SpinnerNumberModel(0, //initial value
		                               0, //min
		                               32, //max
		                               1);
		numberSpinner=new JSpinner(model);
		content=new JPanel(new FlowLayout(FlowLayout.LEFT));
		content.add(colorsBox);
		content.add(placesBox);
		content.add(relationsBox);
		content.add(numberSpinner);
	}

	@Override
	public List<ParamValue> getValue() {
		// TODO Auto-generated method stub
		ArrayList<ParamValue> list=new ArrayList<ParamValue>();
		String result=(String) placesBox.getSelectedItem();
		result+="_"+colorsBox.getSelectedItem();
		result+=" "+(String) ((JComboBox)relationsBox).getSelectedItem();
		result+=" "+(Integer) numberSpinner.getValue();
		list.add(new ParamValue(result, OperandType.STATEPREDICATE));
		return list;
	}

	public String[] getPredicates() {
		return predicates;
	}

	@Override
	public void setValue(List<ParamValue> val) {
		String arr[]=val.get(0).getOperandName().split(" ");
		String place=arr[0].split("_")[0];
		String color=arr[0].split("_")[1];
		placesBox.setSelectedItem(place);
		colorsBox.setSelectedItem(color);
		relationsBox.setSelectedItem(arr[1]);
		numberSpinner.setValue(Integer.parseInt(arr[2]));
	}

}
