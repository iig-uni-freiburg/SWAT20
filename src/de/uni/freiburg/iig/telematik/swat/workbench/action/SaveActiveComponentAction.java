package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.bernhard.AnalyzePanelController;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.WorkbenchComponent;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;

public class SaveActiveComponentAction extends AbstractWorkbenchAction {

	public SaveActiveComponentAction() {
		this("");
	}

	public SaveActiveComponentAction(String name) {
		super(name);
		setTooltip("Save active component");
		try {
			setIcon(IconFactory.getIcon("save"));
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static final long serialVersionUID = 7231652730616663333L;

	private void savePN(PNEditor mainComponent) {
		try {
			SwatComponents.getInstance().storePetriNet(mainComponent.getNetContainer().getPetriNet().getName());
			MessageDialog.getInstance().addMessage("Successfully saved Petri Net");
			Workbench.consoleMessage("Successfully saved Petri Net");
		} catch (SwatComponentException e) {
			MessageDialog.getInstance().addMessage("ERROR: Could not save Petri Net: " + e.getMessage());
			Workbench.errorMessage("Could not save Petri Net: " + e.getMessage());
		}
		
	}


	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		SwatTabView tabView = SwatTabView.getInstance();
		Component selectedComponent = tabView.getSelectedComponent();
		if (selectedComponent instanceof WorkbenchComponent) {
			WorkbenchComponent component = (WorkbenchComponent) tabView.getSelectedComponent();
			if (component.getMainComponent() instanceof PNEditor) {
				savePN((PNEditor) component.getMainComponent());
				AnalyzePanelController.getInstance().objectChanged(component.getFile().getName());
			}
		} else {
			Workbench.errorMessage("Could not save: Active pane is not of type PNEditor");

		}

	}

}