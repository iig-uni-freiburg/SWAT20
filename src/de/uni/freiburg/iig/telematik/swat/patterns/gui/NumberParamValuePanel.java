package de.uni.freiburg.iig.telematik.swat.patterns.gui;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;

public class NumberParamValuePanel extends ParameterValuePanel {

	private static final long serialVersionUID = 7459194052985203666L;
	private JSpinner mParaValueBox;

	public NumberParamValuePanel(Parameter parameter) {
		super();
		final String type = parameter.getValue().getType();
		final Parameter curParam = parameter;
		SpinnerModel model = new SpinnerNumberModel(1, 0, 100, 1);
		JSpinner spinner = new JSpinner(model);
		mParaValueBox = spinner;

		parameter.setValue(type, Integer.toString(0));

		mParaValueBox.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				System.out.println("Setting value of parameter to: " + mParaValueBox.getModel().getValue());
				curParam.setValue(type, Integer.toString((Integer) mParaValueBox.getModel().getValue()));

			}
		});

		this.add(mParaValueBox);
	}

}
