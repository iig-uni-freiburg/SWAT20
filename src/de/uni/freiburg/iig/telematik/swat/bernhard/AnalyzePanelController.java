package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.HashMap;

import javax.swing.JPanel;

import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;

public class AnalyzePanelController {

	private HashMap<String, AnalyzePanel> panelDic;
	private static AnalyzePanelController instance;
	private PatternAnalyzeLogic analyzeLogic;
	public AnalyzePanelController() {
		panelDic=new HashMap<String, AnalyzePanel>();
		analyzeLogic=new PatternAnalyzeLogic();
	}
	
	public static AnalyzePanelController getInstance() {
		if(instance == null) {
			instance = new AnalyzePanelController();
		}
		return instance;
	}
	
	public AnalyzePanel getPanel(String name, PNEditor pneditor) {
		if(panelDic.get(name) == null) {
			AnalyzePanel a=new AnalyzePanel(pneditor);
			a.setNetName(name);
			panelDic.put(name, a);
		}
		return panelDic.get(name);
	}
	
}
