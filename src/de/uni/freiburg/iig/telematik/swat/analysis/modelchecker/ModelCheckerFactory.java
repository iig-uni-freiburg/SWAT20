package de.uni.freiburg.iig.telematik.swat.analysis.modelchecker;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.prism.PRISM;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.sciff.execution.SCIFF;
import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.CPNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.IFNetEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PTNetEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;

public class ModelCheckerFactory {

	
	public static ModelChecker createModelChecker(ViewComponent comp) {
		
		ModelChecker mc = null;
		if (comp instanceof PTNetEditorComponent) {
			mc = new PRISM((PTNet) ((PTNetEditorComponent) comp).netContainer.getPetriNet());
		} else if (comp instanceof CPNEditorComponent) {
			mc = new PRISM((CPN) ((CPNEditorComponent) comp).netContainer.getPetriNet());
		} else if (comp instanceof IFNetEditorComponent) {
			mc = new PRISM((IFNet) ((IFNetEditorComponent) comp).netContainer.getPetriNet());
		} else if (comp instanceof LogFileViewer) {
			mc = new SCIFF(((LogFileViewer) comp).getFile());
		}
		
		return mc;
	}
	

}
