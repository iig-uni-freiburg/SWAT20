package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns.parameter.Parameter;

public class PatternPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private CompliancePattern mPattern;
	
	public PatternPanel(CompliancePattern pattern) {
		mPattern = pattern;
		pattern.instantiate();
		initGui();
		
	}
	
	private void initGui() {
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel label = new JLabel(mPattern.getName());
		Font font = label.getFont();
		Font boldFont = new Font(font.getFontName(), Font.BOLD,
				font.getSize() + 3);
		label.setFont(boldFont);
		final PatternPanel pp = this;
		try {
			JButton mRemoveButton = new JButton(IconFactory.getIcon("minimize"));
			mRemoveButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					mPattern.notInstantiated();
					try {
						Container parent = pp.getParent();
						parent.remove(pp);
						parent.repaint();
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			});

			JPanel mTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			mTopPanel.add(label);
			mTopPanel.add(Box.createHorizontalStrut(10));
			mTopPanel.add(mRemoveButton);
			this.add(mTopPanel);
			
			final JPanel mBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

			int componentIndex = -1;
			for (Parameter parameter : mPattern.getParameters()) {
				componentIndex = componentIndex + 3;
				final int i = componentIndex;
				final Parameter curParameter = parameter;
				final JComboBox mParaTypeBox = new JComboBox((String[])
						parameter.getParameterDomain().toArray(new String[0]));
				final ParameterValuePanel paraValuePanel = ParaValuePanelFactory.createPanel(mParaTypeBox.getSelectedItem(), curParameter);
				mBottomPanel.add(new JLabel(parameter.getName() + ":"));
				mBottomPanel.add(mParaTypeBox);
				mBottomPanel.add(paraValuePanel);
				
				mParaTypeBox.addItemListener(new ItemListener() {

					private Component mParaValuePanel = paraValuePanel;
					
					private int index = i;

					@Override
					public void itemStateChanged(ItemEvent e) {
						mBottomPanel.remove(mParaValuePanel);
						mParaValuePanel = ParaValuePanelFactory.createPanel(
								mParaTypeBox.getSelectedItem(), curParameter);
						mBottomPanel.add(mParaValuePanel, index);
						updateUI();
					}
					
				});
				
							
			}
			
			this.add(mBottomPanel);
			mBottomPanel.validate();
			
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
				
	}


}
