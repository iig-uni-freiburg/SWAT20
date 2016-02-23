package de.uni.freiburg.iig.telematik.swat.patterns;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.swat.analysis.AnalysisController;
import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.logs.LogViewViewer;
import de.uni.freiburg.iig.telematik.swat.patterns.gui.PatternDialog;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.factory.AbstractPatternFactory;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.factory.CWNPatternFactory;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.factory.IFNetPatternFactory;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.factory.PTNetPatternFactory;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.factory.XESLogPatternFactory;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.CWNInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.IFNetInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.ModelInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.PTNetInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.XESLogInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.CPNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.IFNetEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PTNetEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Holds patterns **/
public class PatternController {

	private ModelInfoProvider mModelInformationProvider;
	private ArrayList<CompliancePattern> mPatterns;
	private PatternDialog mPatternDialog;
	private final AnalysisController mAnalysisController;

	public PatternController(AnalysisController anaController) throws PatternException, Exception {
		AbstractPatternFactory mPatternFactory = null;
		mAnalysisController = anaController;
		ViewComponent component = (ViewComponent) Workbench.getInstance().getTabView().getSelectedComponent();

		if (component instanceof PTNetEditorComponent) {
			mPatternFactory = new PTNetPatternFactory();
			mModelInformationProvider = new PTNetInfoProvider((PTNet) ((PTNetEditorComponent) component).netContainer.getPetriNet());
		} else if (component instanceof CPNEditorComponent) {
			mPatternFactory = new CWNPatternFactory();
			mModelInformationProvider = new CWNInfoProvider((CPN) ((CPNEditorComponent) component).netContainer.getPetriNet());
		} else if (component instanceof IFNetEditorComponent) {
			mPatternFactory = new IFNetPatternFactory();
			mModelInformationProvider = new IFNetInfoProvider((IFNet) ((IFNetEditorComponent) component).netContainer.getPetriNet());
		} else if (component instanceof LogFileViewer) {
			mPatternFactory = new XESLogPatternFactory();
			//mModelInformationProvider = new XESLogInfoProvider(((LogFileViewer) component).getFile());
			mModelInformationProvider = new XESLogInfoProvider((LogFileViewer) component);
		} else if (component instanceof LogViewViewer) {
			mPatternFactory = new XESLogPatternFactory();
			mModelInformationProvider = new XESLogInfoProvider((LogViewViewer) component);
		}

		mPatterns = mPatternFactory.loadPatterns();
		for (CompliancePattern pattern : mPatterns) {
			pattern.acceptInfoProfider(mModelInformationProvider);
		}
		generatePatternDialog();

	}

	public ArrayList<CompliancePattern> getPatterns() {
		return mPatterns;
	}

	public void openPatternDialog() {
		mPatternDialog.setLocationByPlatform(true);
		mPatternDialog.setVisible(true);
	}

	public void setPatterns(ArrayList<CompliancePattern> patterns) throws Exception {
		mPatterns = patterns;
		generatePatternDialog();//comment
		//mPatternDialog.setPattern(mPatterns); //uncomment
		mAnalysisController.updateAnalysePanel();
	}

	private void generatePatternDialog() {
		mPatternDialog = new PatternDialog(mPatterns);
		mPatternDialog.addComponentListener(new ComponentListener() {

			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentHidden(ComponentEvent e) {
                            try {
                                mAnalysisController.updateAnalysePanel();
                            } catch (Exception ex) {
                                Logger.getLogger(PatternController.class.getName()).log(Level.SEVERE, null, ex);
                            }
			}

		});
	}

}
