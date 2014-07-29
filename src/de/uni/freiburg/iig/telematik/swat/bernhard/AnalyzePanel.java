package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.thoughtworks.xstream.XStream;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.lukas.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.lukas.IOUtils;
import de.uni.freiburg.iig.telematik.swat.lukas.Parameter;
import de.uni.freiburg.iig.telematik.swat.lukas.PatternFactory;
import de.uni.freiburg.iig.telematik.swat.lukas.PatternResult;
import de.uni.freiburg.iig.telematik.swat.lukas.PrismExecutor;
import de.uni.freiburg.iig.telematik.swat.lukas.PrismResult;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponentType;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

/**
 * this class represents a panel to be added on the right side
 * 
 * @author bernhard
 * 
 */
public class AnalyzePanel implements LoadSave {
	private JPanel panel;
	private JLabel analysisTopLabelWithDate;
	private JButton editButton, runButton, saveButton;
	private String netName;
	private PatternWindow patternWindow;
	private PNEditor pneditor;
	private PetriNetInformation netInfo;
	private PatternFactory patternFactory;

	public void netChanged() {
		netInfo.netChanged();
		// update the parameter boxes
		patternWindow.netChanged();
	}

	public PetriNetInformationReader getNetInformation() {
		return netInfo;
	}

	public AnalyzePanel(PNEditor pneditor, String net) {
		this.pneditor = pneditor;
		netName = net.split("[.]")[0];
		netInfo = new PetriNetInformation(pneditor);
		patternFactory = new PatternFactory(pneditor.getNetContainer()
				.getPetriNet());

		panel = new JPanel(new GridLayout(
				PatternAnalyzeLogic.MAX_PATTERNS + 3, 1, 10, 10));
		analysisTopLabelWithDate = new JLabel("Analysis from "
				+ getDateShort());
		editButton = new JButton("Edit");
		runButton = new JButton("Run");
		saveButton = new JButton("Save");

		patternWindow = new PatternWindow(this, patternFactory);
		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				showPatternWindow();
			}
		});
		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				analyze();
			}
		});
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		netChanged();
		update();
	}

	protected void showPatternWindow() {
		patternWindow.setVisible(true);
	}

	private void addButtons() {
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(editButton);
		buttonPanel.add(runButton);
		buttonPanel.add(saveButton);
		panel.add(buttonPanel);
	}

	private String getDateShort() {
		Date today = new Date();
		DateFormat formatter = DateFormat.getDateInstance(
				DateFormat.SHORT,
				new Locale(System.getProperty("user.language"), System
						.getProperty("user.country")));
		return formatter.format(today);
	}

	/**
	 * the function invoked when Analyze is pressed
	 */
	private void analyze() {
		PrismExecutor prismExecuter = new PrismExecutor(pneditor
				.getNetContainer().getPetriNet());
		// build list of patterns
		HashMap<PatternSetting, CompliancePattern> resultMap = new HashMap<PatternSetting, CompliancePattern>();

		List<PatternSetting> patternSettings = patternWindow
				.getPatternSettings();
		MessageDialog.getInstance().addMessage(
				"Giving " + patternSettings.size() + " Patterns to PRISM");
		ArrayList<CompliancePattern> compliancePatterns = new ArrayList<CompliancePattern>();
		for (PatternSetting setting : patternSettings) {
			CompliancePattern compliancePattern = patternFactory
					.createPattern(setting.getName(),
							(ArrayList<Parameter>) setting.getParameters());
			compliancePatterns.add(compliancePattern);
			resultMap.put(setting, compliancePattern);
		}
		System.out.println("Compliance Pattern: " + compliancePatterns.size());
		PrismResult prismResult = prismExecuter.analyze(compliancePatterns);
		for (PatternSetting setting : patternSettings) {
			PatternResult patternResult = prismResult
					.getPatternResult(resultMap.get(setting));
			setting.setResult(patternResult);
		}
		update();
	}

	public void update() {
		panel.removeAll();
		panel.add(Helpers.jPanelLeft(analysisTopLabelWithDate));
		addButtons();
		panel.add(Helpers.jPanelLeft(new JLabel("Patterns to Check: ")));
		for (PatternSetting p : patternWindow.getPatternSettings()) {
			System.out.println(p);
			JPanel newPanel = new JPanel();
			PatternResult result = p.getResult();
			// check whether a result exists
			if (result != null) {
				int rows = 2;
				if (result.isFulfilled() == false) {
					rows = 3;
				}
				newPanel = new JPanel(new GridLayout(rows, 1, 1, 1));
				newPanel.add(new JLabel(p.toString()));
				newPanel.add(new JLabel("\tProb:" + result.getProbability()));
				if (result.isFulfilled() == false) {
					JButton counterButton = new JButton("Counterexample");
					newPanel.add(counterButton);
				}
			} else {
				newPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
				newPanel.add(new JLabel(p.toString()));
			}

			panel.add(newPanel);
		}

		panel.repaint();
		panel.updateUI();
	}

	public void setAnalyseName(String text) {
		analysisTopLabelWithDate.setText(text);
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public String getNetName() {
		return netName;
	}

	public void setNetName(String netName) {
		this.netName = netName.split("[.]")[0];
	}

	public boolean load(File f) {
		MessageDialog.getInstance().addMessage(
				"Loading Analysis Settings from " + f);
		List<PatternSetting> newList = AnalysisStore.loadFromFile(f);
		// System.out.println(newList);
		patternWindow.setPatternSettings(newList);
		update();
		return true;
	}

	@Override
	public boolean save() {
		AnalysisStore.store(patternWindow.getPatternSettings(), netName);
		// update the tree
		SwatTreeView.getInstance().updateAnalysis();
		SwatTreeView.getInstance().expandAll();
		SwatTreeView.getInstance().updateUI();
		return true;
	}

}
