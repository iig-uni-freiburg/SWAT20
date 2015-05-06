package de.uni.freiburg.iig.telematik.swat.patterns.gui;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;

public abstract class ParameterValuePanel extends JPanel {

	private static final long serialVersionUID = -3244037189023860831L;
	
	public ParameterValuePanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}

	public void setParameterAccordingToPattern(Parameter pattern) {
		Component comp = getComponent(1);
		if (comp instanceof JComboBox) {
			System.out.println("Trying to set to: " + pattern.getValue().getValue());
			((JComboBox) comp).setSelectedItem(pattern.getValue().getValue());

		}
		//		try {
		//		for (int i =0;i<pattern.getParameters().size();i++){
		//				Component component = getComponent(i * 2 + 1);
		//			if (component instanceof JComboBox) {
		//				JComboBox box = (JComboBox) component;
		//				box.setSelectedItem(pattern.getParameters().get(i).getValue().getValue());
		//				System.out.println("Trying to set to: " + pattern.getParameters().get(i).getValue().getValue());
		//			}
		//		}
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}
	}

}
