package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.uni.freiburg.iig.telematik.swat.bernhard.AnalyzePanelController;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;

public class SaveAllAction extends AbstractAction {

	private static final long serialVersionUID = 7231652730616663333L;

	@Override
	public void actionPerformed(ActionEvent e) {
		MessageDialog.getInstance().addMessage("Saving all Petri nets");
		try {
			SwatComponents.getInstance().storeAllPetriNets();
			AnalyzePanelController.getInstance().allObjectsChanged();
			MessageDialog.getInstance().addMessage("Done.");
			SwatTabView.getInstance().unsetModifiedAll();
		} catch (SwatComponentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
