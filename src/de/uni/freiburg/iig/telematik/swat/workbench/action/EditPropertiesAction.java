package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.SwingUtilities;

import de.uni.freiburg.iig.telematik.swat.workbench.SwatToolbar;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SWATPropertySettingDialog;
import de.uni.freiburg.iig.telematik.wolfgang.editor.WGMenuBar;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.PropertySettingDialog;

public class EditPropertiesAction extends AbstractWorkbenchAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6722296041864651654L;

	public EditPropertiesAction() {
		super("Edit Properties");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		SWATPropertySettingDialog.showDialog(null);
		
	}

	
}
