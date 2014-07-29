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

public class PatternStatePredicateParameter extends PatternParameterPanel {

	private final String predicates[] = {"<", ">", "=", "<=", ">=" };
	private JSpinner numberSpinner;
	private JComboBox relationsBox;
	private JComboBox placesBox;
	public PatternStatePredicateParameter(String name, String places[]) {
		super(name);
		// TODO Auto-generated constructor stub
		placesBox=new JComboBox(places);
		relationsBox=new JComboBox(predicates);
		SpinnerModel model =
		        new SpinnerNumberModel(0, //initial value
		                               0, //min
		                               32, //max
		                               1);
		numberSpinner=new JSpinner(model);
		content=new JPanel(new FlowLayout(FlowLayout.LEFT));
		content.add(placesBox);
		content.add(relationsBox);
		content.add(numberSpinner);
	}

	@Override
	public List<ParamValue> getValue() {
		// TODO Auto-generated method stub
		ArrayList<ParamValue> list=new ArrayList<ParamValue>();
		String result=(String) ((JComboBox)placesBox).getSelectedItem();
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
		placesBox.setSelectedItem(arr[0]);
		relationsBox.setSelectedItem(arr[1]);
		numberSpinner.setValue(Integer.parseInt(arr[2]));
	}

}
