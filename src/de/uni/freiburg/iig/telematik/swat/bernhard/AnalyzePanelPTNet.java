package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.lukas.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.ParamValue;
import de.uni.freiburg.iig.telematik.swat.lukas.Parameter;
import de.uni.freiburg.iig.telematik.swat.lukas.PatternResult;
import de.uni.freiburg.iig.telematik.swat.lukas.PrismExecutor;
import de.uni.freiburg.iig.telematik.swat.lukas.PrismResult;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponent;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;

public class AnalyzePanelPTNet extends AnalyzePanel {

	private PNEditor pnEditor;
	protected AnalyzeToolBar toolBar;
	public AnalyzePanelPTNet(SwatComponent component, String net) {
		super(component, net);
		pnEditor=(PNEditor) component;
		objectInformationReader=new PetriNetInformation(pnEditor);
		toolBar = new AnalyzeToolBar((PNEditor) component);
		objectChanged();
		update();
		// TODO Auto-generated constructor stub
	}
	public AnalyzeToolBar getToolBar() {
		return toolBar;
	}
	private void showCounterExample(List<String> path) {
		toolBar.setCounterExample(path);
	}

	protected void analyze() {
		toolBar.deActivate();
		toolBar.resetHighLightedCounterExample();
		PrismExecutor prismExecuter = new PrismExecutor(pnEditor
				.getNetContainer().getPetriNet());
		// build list of patterns
		HashMap<PatternSetting, CompliancePattern> resultMap = new HashMap<PatternSetting, CompliancePattern>();

		MessageDialog.getInstance().addMessage(
				"Giving " + patternSettings.size() + " Patterns to PRISM");
		ArrayList<CompliancePattern> compliancePatterns = new ArrayList<CompliancePattern>();
		for (PatternSetting setting : patternSettings) {
			CompliancePattern compliancePattern = patternFactory
					.createPattern(setting.getName(),
							(ArrayList<Parameter>) setting.getParameters());
			compliancePatterns.add(compliancePattern);
			// store the setting and the pattern in dictionary
			resultMap.put(setting, compliancePattern);
		}
		// analyze and set the results
		PrismResult prismResult = prismExecuter.analyze(compliancePatterns);
		for (PatternSetting setting : patternSettings) {
			PatternResult patternResult = prismResult
					.getPatternResult(resultMap.get(setting));
			setting.setResult(patternResult);
		}
		update();
	}
	@Override
	protected void addCounterExampleButton(PatternResult result, JPanel newPanel) {
		// TODO Auto-generated method stub
		final List<String> path = result.getViolatingPath();
		
		if (path != null) {
			JButton counterButton = new JButton("Counterexample");
			counterButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					showCounterExample(path);
				}
			});
			// counterButton.setPreferredSize(counterButton.getMaximumSize());
			// if (result.isFulfilled() == false) {
			newPanel.add(Helpers.jPanelLeft(counterButton));
		}
	}
	@Override
	protected String getCorrectValue(ParamValue val) {
		// TODO Auto-generated method stub
		if(val.getOperandType() == OperandType.TRANSITION) {
			HashMap<String, String> transitionDicReverse=((PetriNetInformation) this.objectInformationReader).getTransitionDictionaryReverse();
			return transitionDicReverse.get(val.getOperandName());
		}
		return val.getOperandName();
	}
}
