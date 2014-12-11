package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.PatternResult;
import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.prism.PrismExecutor;
import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.prism.PrismResult;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.ParamValue;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.Parameter;
import de.uni.freiburg.iig.telematik.swat.workbench.WorkbenchComponent;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
/**
 * This class implements the functionality for analyzing a PTNet.
 * @author bernhard
 *
 */
public class AnalyzePanelPN extends AnalyzePanel {

	private PNEditor pnEditor;
	protected AnalyzeToolBar toolBar;
	public AnalyzePanelPN(WorkbenchComponent component, String net) {
		super(component, net);
		pnEditor=(PNEditor) component;
		objectInformationReader=new PetriNetInformation(pnEditor);
		toolBar = new AnalyzeToolBar((PNEditor) component);
		objectChanged();
		update();
		// TODO Auto-generated constructor stub
	}
	/**
	 * return the AnalyzeToolBar
	 * @return the ToolBar
	 */
	public AnalyzeToolBar getToolBar() {
		return toolBar;
	}
	/**
	 * load the given Path in the AnalyzeToolBar
	 * @param path the Counterexample to be visualized
	 */
	private void showCounterExample(List<String> path) {
		toolBar.setCounterExample(path);
	}
	/**
	 * Start the analysis. Reset the toolbar, do some checks
	 * and then make the analysis logic start PRISM.
	 */
	protected void analyze() {
		
		toolBar.reset();
		if(!pnEditor.getNetContainer().getPetriNet().isCapacityBounded()) {
			JOptionPane.showMessageDialog(null,
					"Petri Net is not bounded!", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		PrismExecutor prismExecuter = new PrismExecutor(pnEditor
				.getNetContainer().getPetriNet());
		// build list of patterns
		HashMap<PatternSetting, ArrayList<CompliancePattern>> resultMap = 
				new HashMap<PatternSetting, ArrayList<CompliancePattern>>();

		MessageDialog.getInstance().addMessage(
				"Giving " + patternSettings.size() + " Patterns to PRISM");
		ArrayList<CompliancePattern> compliancePatterns = new ArrayList<CompliancePattern>();
		
		for (PatternSetting setting : patternSettings) {
			setting.reset();
			ArrayList<CompliancePattern> cPs = patternFactory
					.createPattern(setting.getName(),
							(ArrayList<Parameter>) setting.getParameters());
			compliancePatterns.addAll(cPs);
			// store the setting and the pattern in dictionary
			resultMap.put(setting, cPs);
		}
		
		// analyze and set the results
		PrismResult prismResult = prismExecuter.analyze(compliancePatterns);
		for (PatternSetting setting : patternSettings) {
			
			ArrayList<CompliancePattern> patterns = resultMap.get(setting);
			
			for (CompliancePattern pattern : patterns) {
				PatternResult patternResult = prismResult.getPatternResult(pattern);
				setting.setResult(patternResult);
			}
			
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
	protected String adjustValue(ParamValue val) {
		// TODO Auto-generated method stub
		if(val.getOperandType() == OperandType.TRANSITION) {
			HashMap<String, String> transitionDicReverse=((PetriNetInformation) this.objectInformationReader).getTransitionToLabelDictionary();
			return transitionDicReverse.get(val.getOperandName());
		}
		return val.getOperandName();
	}
}
