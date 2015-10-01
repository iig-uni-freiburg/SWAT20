package de.uni.freiburg.iig.telematik.swat.analysis.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponentType;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

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
				Workbench.errorMessage("Wrong parameter or parameter exception while displaying pattern result", e, true);
				e.printStackTrace();
			} catch (PropertyException e) {
				//could not load icon
				Workbench.errorMessage("Could not load icon for result panel", e, false);
			} catch (IOException e) {
				//could not load icon
				Workbench.errorMessage("Could not load icon for result panel", e, false);
				e.printStackTrace();
			}
			if(pattern.getCounterExample()!=null && !pattern.getCounterExample().isEmpty())
				this.add(getCounterexampleButton(pattern));
		} else {
			this.add(new JLabel("Satisfied: ?"));
			this.add(new JLabel("Prob: ?"));
		}
		
	}

	private Component getCounterexampleButton(CompliancePattern pattern) {
		JButton btnCounterexample = new JButton("Ex");
		btnCounterexample.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (Workbench.getInstance().getTypeOfCurrentComponent() == SwatComponentType.PETRI_NET){
						PNEditorComponent editor = (PNEditorComponent) SwatTabView.getInstance().getSelectedComponent();
						editor.getGraphComponent().markPath(pattern.getCounterExample(), Color.RED);
						//((JButton)e.getSource()).setEnabled(true);
					}
				} catch (Exception e1) {
					Workbench.errorMessage("Could not present counterexample.", e1, true);
				}
				
			}
		});
		
		return btnCounterexample;
	}

}
