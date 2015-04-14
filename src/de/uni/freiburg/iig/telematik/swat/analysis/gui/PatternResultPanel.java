package de.uni.freiburg.iig.telematik.swat.analysis.gui;

import java.awt.Component;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;

public class PatternResultPanel extends JPanel {
	
	private static final long serialVersionUID = -2236168943120437184L;

	public PatternResultPanel(CompliancePattern pattern) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createTitledBorder(" "));
		this.add(new JLabel(pattern.getName()));
		for(Parameter p : pattern.getParameters()) {
			JLabel parameterName = new JLabel(p.getName() + ": " + p.getValue().getValue());
			parameterName.setAlignmentX(Component.LEFT_ALIGNMENT);
			this.add(parameterName);
		}
		if (pattern.getProbability() != -1) {
			ImageIcon satIcon;
			try {
				if (pattern.isSatisfied()) {
					satIcon = IconFactory.getIcon("result_valid");
				} else {
					satIcon = IconFactory.getIcon("result_false");
				}
				JPanel satPanel = new JPanel();
				satPanel.setLayout(new BoxLayout(satPanel, BoxLayout.X_AXIS));
				satPanel.add(new JLabel("Satisfied: "));
				satPanel.add(new JLabel(satIcon));
				satPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
				this.add(satPanel);
				JLabel probLabel = new JLabel("Prob: " + pattern.getProbability());
				probLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
				this.add(probLabel);
			} catch (ParameterException e) {
				e.printStackTrace();
			} catch (PropertyException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			this.add(new JLabel("Satisfied: ?"));
			this.add(new JLabel("Prob: ?"));
		}
		
	}

}
