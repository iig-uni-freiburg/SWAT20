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

import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.ParamValue;
/**
 * This Class represents a single statement of an state predicate.
 * A state predicate consinsts of at least one StatePredicateStatement
 * @author bernhard
 *
 */
public class StatePredicateStatement extends ParameterPanel {

	private final String predicates[] = {"<", ">", "=", "!=", "<=", ">=" };
	private JSpinner numberSpinner;
	private JComboBox relationsBox;
	private JComboBox placesBox;
	private JComboBox colorsBox;
	private PetriNetInformation pnReader;
	/**
	 * Create A StatePredicateStatement parameter with a given name
	 * for a given PNReader
	 * @param name the name of the parameter
	 * @param pnReader an object implementing the interface PNReader, which is
	 * used to retrieve the list of places and colors
	 */
	public StatePredicateStatement(String name, PetriNetInformation pnReader) {
		super(name);
		this.pnReader=pnReader;
		// TODO Auto-generated constructor stub
		placesBox=new JComboBox(pnReader.getPlacesArray());
		relationsBox=new JComboBox(predicates);
		colorsBox = new JComboBox(pnReader.getDataTypesWithBlackArray());
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
		String placeName=pnReader.getLabelToPlaceDictionary().get((String) placesBox.getSelectedItem());
		String result=placeName;
		result+="_"+colorsBox.getSelectedItem();
		result+=" "+(String) ((JComboBox)relationsBox).getSelectedItem();
		result+=" "+(Integer) numberSpinner.getValue();
		list.add(new ParamValue(result, OperandType.STATEPREDICATE));
		return list;
	}

	@Override
	public void setValue(List<ParamValue> val) {
		String arr[]=val.get(0).getOperandName().split(" ");
		String place=arr[0].split("_")[0];
		place=pnReader.getPlacesToLabelDictionary().get(place);
		String color=arr[0].split("_")[1];
		placesBox.setSelectedItem(place);
		colorsBox.setSelectedItem(color);
		relationsBox.setSelectedItem(arr[1]);
		numberSpinner.setValue(Integer.parseInt(arr[2]));
	}

}
