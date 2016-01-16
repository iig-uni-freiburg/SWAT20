package de.uni.freiburg.iig.telematik.swat.patterns.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import com.sun.prism.paint.Color;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;


public class PatternDialog extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel mDialogPanel;
	
	private JButton mAddPatternButton, mOkButton, mRemoveAllButton;

	private ArrayList<CompliancePattern> mPatterns;
	
	public PatternDialog(ArrayList<CompliancePattern> patterns) {
		mDialogPanel = new JPanel();
		mPatterns = patterns;
		initGui();
	}
	
	private void initGui() {
		
		BorderLayout bl = new BorderLayout();
		Container c = getContentPane();
		this.setLayout(bl);
		mDialogPanel.setLayout(new BoxLayout(mDialogPanel, BoxLayout.Y_AXIS));

		// GUI and functionality of OK-Button
		mOkButton = new JButton(" OK ");
		mOkButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		
		// GUI and functionality of Remove All-Button
		mRemoveAllButton=new JButton("Remove All");
		mRemoveAllButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (CompliancePattern pt : mPatterns) {
					pt.notInstantiated();
				}
				mDialogPanel.removeAll();
				mDialogPanel.repaint();
				mDialogPanel.updateUI();
			}
		});
		
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southPanel.add(mOkButton);
		
		JPanel northPanel=new JPanel(new BorderLayout());
		northPanel.add(mDialogPanel, BorderLayout.NORTH);
		JScrollPane jsp = new JScrollPane(northPanel);
		jsp.setVisible(true);
		
		mAddPatternButton = new JButton("Add Pattern");
		
		mAddPatternButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				JPopupMenu popup = new JPopupMenu();
				for (CompliancePattern pattern : mPatterns) {
					final CompliancePattern patternForPanel = pattern;
					String patternName = pattern.getName();

					JMenuItem patternItem = new JMenuItem(patternName);

					patternItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							
							try {
								addPatternPanel(patternForPanel);
							} catch (ParameterException e) {
								e.printStackTrace();
							}
						}

						private void addPatternPanel(CompliancePattern pattern) {
							
							if (pattern.isInstantiated()) {
								pattern = pattern.duplicate();
								pattern.unmakeMenuItem();
								mPatterns.add(pattern);
							}
							mDialogPanel.add(new PatternPanel(pattern));
							mDialogPanel.updateUI();
						}

						
					});
					if (pattern.isMenuItem()) {
						popup.add(patternItem);
					}
				}
				
				popup.show(mAddPatternButton, mAddPatternButton.getWidth() * 4 / 5,
						mAddPatternButton.getHeight() * 4 / 5);
				
			}
		});
		
		JPanel buttonPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(mAddPatternButton);
		buttonPanel.add(mRemoveAllButton);
		c.add(buttonPanel, BorderLayout.NORTH);
		c.add(jsp, BorderLayout.CENTER);
		c.add(southPanel, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(900, 600);
		
		for (CompliancePattern p : mPatterns) {
			addPatternToEditPane(p);
		}

	}

	public static void main(String args[]) {
		PatternDialog dialog = new PatternDialog(null);
		dialog.setVisible(true);
	}

	public void setPattern(ArrayList<CompliancePattern> patterns) {
		//mPatterns.clear();
		//mPatterns.addAll(patterns);
		mPatterns = patterns;
		mDialogPanel.removeAll();
		mDialogPanel.repaint();
		mDialogPanel.updateUI();
		for (CompliancePattern pattern : patterns) {
			addPatternToEditPane(pattern);
		}
		mDialogPanel.repaint();
		mDialogPanel.updateUI();
	}

	private void addPatternToEditPane(CompliancePattern pattern) {

		if (pattern.isInstantiated() && pattern.isLoadedFromDisk()) {
			System.out.println("Adding stored pattern: " + pattern.getName());
			mDialogPanel.add(new PatternPanel(pattern));
			//mPatterns.add(pattern);
		}
	}

}
