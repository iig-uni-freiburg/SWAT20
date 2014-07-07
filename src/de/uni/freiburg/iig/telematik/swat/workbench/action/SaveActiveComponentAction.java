package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponent;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;

public class SaveActiveComponentAction extends AbstractAction {

	private static final long serialVersionUID = 7231652730616663333L;

	/** to know the currently active tab **/
	private SwatTabView tabView = null;

	public SaveActiveComponentAction(SwatTabView tabView) {
		this.tabView = tabView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Component selectedComponent = tabView.getSelectedComponent();
		if(selectedComponent instanceof SwatComponent){
			SwatComponent component = (SwatComponent) tabView.getSelectedComponent();
			if (component.getMainComponent() instanceof PNEditor) {
				savePN((PNEditor) component.getMainComponent());
			}
		} else {
			Workbench.errorMessage("Could not save: Active pane is not of type PNEditor");
			
		}
	}
	

	private void savePN(PNEditor mainComponent) {
		try {
			SwatComponents.getInstance().storePetriNet(mainComponent.getNetContainer());
			MessageDialog.getInstance().addMessage("Successfully saved Petri Net");
			Workbench.consoleMessage("Successfully saved Petri Net");
		} catch (ParameterException e1) {
			MessageDialog.getInstance().addMessage("ERROR: Could not save Petri Net");
			Workbench.errorMessage("Could not save Petri Net");
			e1.printStackTrace();
		}
		
	}



}