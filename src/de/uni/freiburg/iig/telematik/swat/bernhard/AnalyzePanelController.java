package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.io.File;
import java.util.HashMap;

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
	/**
	 * The getInstace() method which can be invoked by any other object.
	 * @return
	 */
	public static AnalyzePanelController getInstance() {
		if(instance == null) {
			instance = new AnalyzePanelController();
		}
		return instance;
	}
	/**
	 * get the JPanel to add for a an open file
	 * @param name the name of the file being analyzed
	 * @param component the corresponding SwatComponent
	 * @return the AnalyzePanel associated with the component
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
	/**
	 * invoked when the user wants to load an analysis for a given object
	 * @param name the filename of the analyzed object
	 * @param f the file where the analysis is stored
	 */
	public void loadSetting(String name, File f) {
		if(panelDic.get(name) != null) {
			panelDic.get(name).load(f);
		}
	}
	/**
	 * inform the AnalyzePanel that the object being analyzed
	 * has been changed by the user
	 * @param name
	 */
	public void objectChanged(String name) {
		if(panelDic.get(name) != null) {
			panelDic.get(name).objectChanged();
		}
	}
	/**
	 * inform every AnalyzePanel that the object being analyzed was saved
	 */
	public void allObjectsChanged() {
		for(String name:panelDic.keySet()) {
			panelDic.get(name).objectChanged();
		}
	}
}
