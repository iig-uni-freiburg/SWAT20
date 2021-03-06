package de.uni.freiburg.iig.telematik.swat.patterns.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;


public class StandardParamValuePanel extends ParameterValuePanel {

	private static final long serialVersionUID = 5642382836552346292L;
	
	private JComboBox mParaValueBox;
	
	public StandardParamValuePanel(Parameter parameter) {
		super();
		//final String typeCopy = type;
		final String type = parameter.getValue().getType();
		final Parameter curParam = parameter;
		String[] timePoints = {"start","end","complete"};
		JComboBox<String> timePoint = new JComboBox<>(parameter.getTimePoints());
		timePoint.setSelectedItem(parameter.getSelectedTimePoint());
		mParaValueBox = new JComboBox((String[]) parameter.getParameterRange(
				type).toArray(new String[0]));
		mParaValueBox.setSelectedItem(parameter.getValue().getValue());
		
		mParaValueBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				
				curParam.setValue(type, (String) e.getItem());
			}
			
		});
		
		timePoint.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				curParam.setSelectedTimePoint((String) e.getItem());
				
			}
		});
		
		curParam.setValue(type, parameter.getValue().getValue());//what to do with rules that do carry empty params?
		this.add(timePoint);
		this.add(mParaValueBox);

	}

	

}
