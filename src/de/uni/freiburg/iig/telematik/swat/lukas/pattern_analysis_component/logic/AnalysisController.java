package de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic;

import java.util.ArrayList;
import java.util.HashMap;

import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.gui.AnalyzePanel;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.ModelChecker;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.ModelCheckerFactory;
import de.uni.freiburg.iig.telematik.swat.patterns.PatternController;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;

public class AnalysisController {
	
	private static AnalysisController mAnalysisController;
	
	private HashMap<ViewComponent, PatternController> mComponentToPatternControllerMap;
	
	private HashMap<ViewComponent, AnalyzePanel> mComponentToAnalyzePanelMap;

	private ViewComponent mComponent;

	private AnalyzePanel mAnalyzePanel;
	
	private PatternController mPatternController;
	
	public static AnalysisController getInstance(ViewComponent component) {
		
		if (mAnalysisController == null) {
			mAnalysisController = new AnalysisController();
		}
		
		mAnalysisController.setComponent(component);
		
		return mAnalysisController;
		
	}
	
	private AnalysisController() {
		mComponentToPatternControllerMap = new HashMap<ViewComponent, PatternController>();
		mComponentToAnalyzePanelMap = new HashMap<ViewComponent, AnalyzePanel>();
	}

	private void setComponent(ViewComponent component) {

		mComponent = component;
		
		PatternController controller = mComponentToPatternControllerMap.get(mComponent);
		if (controller == null) {
			controller = new PatternController(this);
			mComponentToPatternControllerMap.put(mComponent, controller);
		}
		
		mPatternController = controller;
		
		AnalyzePanel anaPanel = mComponentToAnalyzePanelMap.get(mComponent);
		if (anaPanel == null) {
			anaPanel = new AnalyzePanel(mAnalysisController);
			mComponentToAnalyzePanelMap.put(mComponent, anaPanel);
		}
 
		mAnalyzePanel = anaPanel;

	}
	
	public AnalyzePanel getAnalyzePanel() {
		return mAnalyzePanel;
	}

	public void openPatternDialog() {
		mComponentToPatternControllerMap.get(mComponent).openPatternDialog();
		
	}
	
	public ArrayList<CompliancePattern> getPatterns() {
		return mPatternController.getPatterns();
	}
	
	public void runModelChecker() throws Exception {
		ModelChecker modelChecker = ModelCheckerFactory.createModelChecker(mComponent);
		modelChecker.run(getPatterns());
		updateAnalysePanel();
	}

	public void updateAnalysePanel() {
		mAnalyzePanel.updatePatternResults();
		mAnalyzePanel.updateUI();
	}
	

}
