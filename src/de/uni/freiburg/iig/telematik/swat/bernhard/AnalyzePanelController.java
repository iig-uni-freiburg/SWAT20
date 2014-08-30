package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.io.File;
import java.util.HashMap;

import javax.swing.JPanel;

import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponent;
/**
 * this class is used to select the AnalyzePanel for a given
 * SwatComponent which has a file open that is being analyzed
 * The class is implemented as a singleton.
 * @author bernhard
 *
 */
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
	public AnalyzePanel getAnalyzePanel(String name, SwatComponent component) {
		if(panelDic.get(name) == null) {
			AnalyzePanel a=null;
			if(component instanceof PNEditor) {
				a=new AnalyzePanelPN(component, name);
			
			} else {
				a=new AnalyzePanelLogfile(component, name);
			}
			panelDic.put(name, a);
		}
		//System.out.println(panelDic);
		return panelDic.get(name);
	}
	
	public void loadSetting(String name, File f) {
		if(panelDic.get(name) != null) {
			panelDic.get(name).load(f);
		}
	}
	public void objectChanged(String name) {
		if(panelDic.get(name) != null) {
			panelDic.get(name).objectChanged();
		}
	}
	public void allObjectsChanged() {
		for(String name:panelDic.keySet()) {
			panelDic.get(name).objectChanged();
		}
	}
}
