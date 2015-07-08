package de.uni.freiburg.iig.telematik.swat.analysis;

import java.util.ArrayList;
import java.util.HashMap;

import de.uni.freiburg.iig.telematik.swat.analysis.gui.AnalyzePanel;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.ModelChecker;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.ModelCheckerFactory;
import de.uni.freiburg.iig.telematik.swat.patterns.PatternController;
import de.uni.freiburg.iig.telematik.swat.patterns.PatternException;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AnalysisController {

	private static AnalysisController mAnalysisController;

	private HashMap<ViewComponent, PatternController> mComponentToPatternControllerMap;

	private HashMap<ViewComponent, AnalyzePanel> mComponentToAnalyzePanelMap;

	private ViewComponent mComponent;

	private AnalyzePanel mAnalyzePanel;

	private PatternController mPatternController;

	public static AnalysisController getInstance(ViewComponent component) throws PatternException, Exception {
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

	private void setComponent(ViewComponent component) throws PatternException, Exception {
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

	public void setPatterns(ArrayList<CompliancePattern> patterns) throws Exception {
		//mPatternController=new PatternController(this);
		mPatternController.setPatterns(patterns);
            try {
                updateAnalysePanel();
                //mComponentToPatternControllerMap.put(m, value)
            } catch (Exception ex) {
                Workbench.errorMessage(null, ex, true);
                //Logger.getLogger(AnalysisController.class.getName()).log(Level.SEVERE, null, ex);
            }
	}

	public void runModelChecker() throws Exception {
		ModelChecker modelChecker = ModelCheckerFactory.createModelChecker(mComponent);
		modelChecker.run(getPatterns());
		updateAnalysePanel();
	}

	public void updateAnalysePanel() throws Exception {
		mAnalyzePanel.updatePatternResults();
		mAnalyzePanel.updateUI();
	}

}
