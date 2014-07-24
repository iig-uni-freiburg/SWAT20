package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;

public class PatternStatePredicateParameter extends PatternParameterPanel {

	private final String predicates[] = {"<", ">", "=", "<=", ">=" };
	private JTextField numberField;
	private JComboBox relationsBox;
	private JComboBox placesBox;
	public PatternStatePredicateParameter(String name, String places[]) {
		super(name, OperandType.STATEPREDICATE);
		// TODO Auto-generated constructor stub
		placesBox=new JComboBox(places);
		relationsBox=new JComboBox(predicates);
		numberField=new JTextField(10);
		jComponent=new JPanel(new FlowLayout(FlowLayout.LEFT));
		jComponent.add(placesBox);
		jComponent.add(relationsBox);
		jComponent.add(numberField);
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(String val) {
		// TODO Auto-generated method stub
		
	}

}
