package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.AnalysisController;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.gui.PatternDialog;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.factory.AbstractPatternFactory;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.factory.CWNPatternFactory;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.factory.IFNetPatternFactory;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.factory.PTNetPatternFactory;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.factory.XESLogPatternFactory;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.model_info_provider.CWNInfoProvider;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.model_info_provider.IFNetInfoProvider;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.model_info_provider.ModelInfoProvider;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.model_info_provider.PTNetInfoProvider;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.model_info_provider.XESLogInfoProvider;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.CPNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.IFNetEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PTNetEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;


public class PatternController {
	
	private ModelInfoProvider mModelInformationProvider;
	private ArrayList<CompliancePattern> mPatterns;
	private PatternDialog mPatternDialog;
	private AnalysisController mAnalysisController;

	public PatternController(AnalysisController anaController) {
		AbstractPatternFactory mPatternFactory = null;
		mAnalysisController = anaController;
		ViewComponent component = (ViewComponent)
				Workbench.getInstance().getTabView().getSelectedComponent();
		
		if (component instanceof PTNetEditorComponent) {
			mPatternFactory = new PTNetPatternFactory();
			mModelInformationProvider = new PTNetInfoProvider((PTNet) 
 ((PTNetEditorComponent) component).netContainer.getPetriNet());
		} else if (component instanceof CPNEditorComponent) {
			mPatternFactory = new CWNPatternFactory();
			mModelInformationProvider = new CWNInfoProvider((CPN) 
 ((CPNEditorComponent) component).netContainer.getPetriNet());
		} else if (component instanceof IFNetEditorComponent) {
			mPatternFactory = new IFNetPatternFactory();
			mModelInformationProvider = new IFNetInfoProvider((IFNet) 
 ((IFNetEditorComponent) component).netContainer.getPetriNet());
		} else if (component instanceof LogFileViewer) {
			mPatternFactory = new XESLogPatternFactory();
			mModelInformationProvider = new XESLogInfoProvider(((LogFileViewer) component).getFile());
		}
		
		mPatterns = mPatternFactory.createPatterns();
		for (CompliancePattern pattern : mPatterns) {
			pattern.acceptInfoProfider(mModelInformationProvider);
		}
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
				mAnalysisController.updateAnalysePanel();
			}
			
		});
				
	}
	
	public ArrayList<CompliancePattern> getPatterns() {
		return mPatterns;
	}
	
	public void openPatternDialog() {
		mPatternDialog.setVisible(true);
	}

}
