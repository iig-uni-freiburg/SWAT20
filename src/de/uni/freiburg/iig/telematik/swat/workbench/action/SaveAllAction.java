package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;

public class SaveAllAction extends AbstractAction {

	private static final long serialVersionUID = 7231652730616663333L;

	@Override
	public void actionPerformed(ActionEvent e) {
		MessageDialog.getInstance().addMessage("Saving all Petri nets");
		try {
			SwatComponents.getInstance().storeAllPetriNets();
			MessageDialog.getInstance().addMessage("Done.");
		} catch (ParameterException e1) {
			MessageDialog.getInstance().addMessage(e1.getMessage());
			e1.printStackTrace();
		}
	}

}
