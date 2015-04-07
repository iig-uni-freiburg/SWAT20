package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns.parameter.ParameterTypeNames;

public class StatePredicateParamValuePanel extends ParameterValuePanel {

	private static final long serialVersionUID = -8006276257739585273L;
	private JComboBox mPlaceBox;
	private JComboBox mRelationBox;
	private JTextField mTokenCountField;
	private Parameter mParameter;
	
	public StatePredicateParamValuePanel(Parameter parameter) {
		super();
		
		String type = parameter.getValue().getType();
		mParameter = parameter;
		
		mPlaceBox = new JComboBox((String[]) parameter.getParameterRange(
				type).toArray(new String[0]));
		mRelationBox = new JComboBox(new String[] { "=", ">=", "<=", "<", ">" });
		mTokenCountField = new JTextField("1", 2);
		
		mPlaceBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				updateParameterValue();
			}
		});
		
		mRelationBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				updateParameterValue();
			}
		});

		mTokenCountField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateParameterValue();
			}
			
		});

		this.add(mPlaceBox);
		this.add(mRelationBox);
		this.add(mTokenCountField);
		updateParameterValue();

	}
	
	private void updateParameterValue() {
		String paramValue = (String) mPlaceBox.getSelectedItem() + mRelationBox.getSelectedItem()
				+ mTokenCountField.getText();
		paramValue = paramValue.replaceAll("-", "_");
		mParameter.setValue(ParameterTypeNames.STATEPREDICATE, paramValue);
		
	}

}
