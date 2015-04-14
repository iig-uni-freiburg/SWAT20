package de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.AnalysisController;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;

public class AnalyzePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	/*private JPanel mButtonPanel;
	
	private JButton mEditButton;
	private JButton mRunButton;
	private JButton mSaveButton;*/

	private AnalysisController mAnalysisController;
	
	public AnalyzePanel(AnalysisController analysisController) {	
		mAnalysisController = analysisController;
		updatePatternResults();
	
	}	
	
	private String getDateShort() {
		Date today = new Date();
		DateFormat formatter = DateFormat.getDateInstance(
				DateFormat.SHORT,
				new Locale(System.getProperty("user.language"), System
						.getProperty("user.country")));
		return formatter.format(today);
	}
	
	
	public void updatePatternResults() {
		
	this.removeAll();
	JPanel content = new JPanel(new GridBagLayout());
	JLabel analysisName = new JLabel("Analysis from " + getDateShort());
	JButton editButton = new JButton("Edit");
	JButton runButton = new JButton("Run");
	JButton saveButton = new JButton("Save");
	JPanel propertyPanel = new JPanel();
	propertyPanel.setLayout(new BoxLayout(propertyPanel, BoxLayout.Y_AXIS));
	Box box = Box.createVerticalBox();

	JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	buttonPanel.add(editButton);
	buttonPanel.add(runButton);
	buttonPanel.add(saveButton);
	box.add(jPanelLeft(analysisName));
	box.add(buttonPanel);

	box.add(jPanelLeft(new JLabel("Patterns to Check: ")));
	JPanel northPanel = new JPanel(new BorderLayout());
	northPanel.add(propertyPanel, BorderLayout.NORTH);
	JScrollPane jsp = new JScrollPane(northPanel);
	jsp.setVisible(true);
	content.setLayout(new BorderLayout());
	content.add(box, BorderLayout.NORTH);
	content.add(jsp, BorderLayout.CENTER);
	
		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mAnalysisController.openPatternDialog();
			}
		});
		
		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					mAnalysisController.runModelChecker();
				} catch (Exception e) {
					Workbench.errorMessage("Analysis Exception", e, true);
				}
			}
		});
		
		ArrayList<CompliancePattern> patterns = mAnalysisController.getPatterns();
		for (CompliancePattern pattern : patterns) {
			if (pattern.isInstantiated()) {
				propertyPanel.add(new PatternResultPanel(pattern));
			}
		}
		this.add(content);
	}
	
	private JPanel jPanelLeft(JComponent k) {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(k);
		return p;
	}

	
	
	/*
	public void updatePatternResults() {
		
		
		
		
		this.removeAll();
		Box box = Box.createVerticalBox();
		mButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		mEditButton = new JButton("Edit");
		mRunButton = new JButton("Run");
		mSaveButton = new JButton("Save");
		mButtonPanel.add(mEditButton);
		mButtonPanel.add(mRunButton);
		mButtonPanel.add(mSaveButton);
		box.add(mButtonPanel);
		
		mEditButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mAnalysisController.openPatternDialog();
			}
		});
		
		mRunButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				mAnalysisController.runModelChecker();
				
			}
		});
		ArrayList<CompliancePattern> patterns = mAnalysisController.getPatterns();
		for (CompliancePattern pattern : patterns) {
			if (pattern.isInstantiated()) {
				box.add(new PatternResultPanel(pattern));
			}
		}
		this.add(box);
	}
	*/
}
