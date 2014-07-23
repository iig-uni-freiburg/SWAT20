package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.io.File;
import java.util.HashMap;

import javax.swing.JPanel;

import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;

public class AnalyzePanelController {

	private HashMap<String, AnalyzePanel> panelDic;
	private static AnalyzePanelController instance;

	public AnalyzePanelController() {
		panelDic=new HashMap<String, AnalyzePanel>();
	}
	
	public static AnalyzePanelController getInstance() {
		if(instance == null) {
			instance = new AnalyzePanelController();
		}
		return instance;
	}
	/**
	 * get the JPanel to add for a given netname 
	 * @param name the name
	 * @param pneditor the pneditor that controls the net
	 * @return a JPanel that you can add on the right side
	 */
	public AnalyzePanel getPanel(String name, PNEditor pneditor) {
		if(panelDic.get(name) == null) {
			AnalyzePanel a=new AnalyzePanel(pneditor, name);
			panelDic.put(name, a);
		}
		if(panelDic.keySet().contains("analysis_val.xml")) {
			try {
				throw new Exception();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//System.out.println(panelDic);
		return panelDic.get(name);
	}
	
	public void loadSetting(String name, File f) {
		if(panelDic.get(name) != null) {
			panelDic.get(name).load(f);
		}
	}
	public void netChanged(String name) {
		if(panelDic.get(name) != null) {
			panelDic.get(name).netChanged();
		}
	}
	public void allNetsChanged() {
		for(String name:panelDic.keySet()) {
			panelDic.get(name).netChanged();
		}
	}
}
