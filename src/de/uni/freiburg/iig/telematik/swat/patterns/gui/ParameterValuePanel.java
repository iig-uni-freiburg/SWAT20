package de.uni.freiburg.iig.telematik.swat.patterns.gui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public abstract class ParameterValuePanel extends JPanel {

	private static final long serialVersionUID = -3244037189023860831L;
	
	public ParameterValuePanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}

}
