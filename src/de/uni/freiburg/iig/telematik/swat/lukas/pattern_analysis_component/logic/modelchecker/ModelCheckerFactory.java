package de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.swat.editor.CPNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.IFNetEditor;
import de.uni.freiburg.iig.telematik.swat.editor.PTNetEditor;
import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.prism.PRISM;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.sciff.SCIFF;
import de.uni.freiburg.iig.telematik.swat.workbench.WorkbenchComponent;

public class ModelCheckerFactory {

	
	public static ModelChecker createModelChecker(WorkbenchComponent comp) {
		
		ModelChecker mc = null;
		if (comp instanceof PTNetEditor) {
			 mc = new PRISM((PTNet)((PTNetEditor) comp).netContainer.getPetriNet());
		} else if (comp instanceof CPNEditor) {
			mc = new PRISM((CPN)((CPNEditor) comp).netContainer.getPetriNet());
		} else if (comp instanceof IFNetEditor) {
			mc = new PRISM((IFNet)((IFNetEditor) comp).netContainer.getPetriNet());
		} else if (comp instanceof LogFileViewer) {
			mc = new SCIFF(((LogFileViewer) comp).getFile());
		}
		
		return mc;
	}
	

}
