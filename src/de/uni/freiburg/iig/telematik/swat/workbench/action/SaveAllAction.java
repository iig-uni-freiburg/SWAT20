package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.invation.code.toval.graphic.dialog.MessageDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveAllAction extends AbstractAction {

	private static final long serialVersionUID = 7231652730616663333L;

	@Override
	public void actionPerformed(ActionEvent e) {
            try {
                MessageDialog.getInstance().addMessage("Saving all Petri nets");
                for (int i = 0; i < SwatTabView.getInstance().getComponentCount() - 1; i++) {
                    if (SwatTabView.getInstance().hasUnsavedChange(i)) {
                        save(i);
                    }
                }
                //AnalyzePanelController.getInstance().allObjectsChanged();
                
                //		try {
                //			SwatComponents.getInstance().storeAllPetriNets();
                //			AnalyzePanelController.getInstance().allObjectsChanged();
                //			MessageDialog.getInstance().addMessage("Done.");
                //			//SwatTabView.getInstance().unsetModifiedAll();
                //			for (int i = 0; i < SwatTabView.getInstance().getComponentCount() - 1; i++)
                //				if (SwatTabView.getInstance().hasUnsavedChange(i)) {
                //					Workbench.consoleMessage("Saved " + SwatTabView.getInstance().getTabComponentAt(i).getName());
                //					System.out.println("Saving " + SwatTabView.getInstance().getTabComponentAt(i).getName());
                //				try {
                //					((PNEditor) SwatTabView.getInstance().getComponentAt(i)).getUndoManager().clear();
                //				} catch (Exception e2) {
                //					e2.printStackTrace();
                //				}
                //				}
                //		} catch (SwatComponentException e1) {
                //			// TODO Auto-generated catch block
                //			e1.printStackTrace();
                //		}
            } catch (Exception ex) {
                Workbench.errorMessage("Error while saving Petri Nets...", ex, true);
            }
	}

	private void save(int i) throws Exception {
		if (SwatTabView.getInstance().getComponentAt(i) instanceof PNEditorComponent) {
			PNEditorComponent editor = (PNEditorComponent) SwatTabView.getInstance().getComponentAt(i);
			String netID = editor.getNetContainer().getPetriNet().getName();
			try {
				SwatComponents.getInstance().getContainerPetriNets().storeAnalyses(netID);
				editor.getUndoManager().clear();
				Workbench.consoleMessage("Saved " + netID);
				SwatTabView.getInstance().unsetModified(i);
			} catch (SwatComponentException e) {
				JOptionPane.showMessageDialog(Workbench.getInstance(), "Could not successfully store " + netID);
				Workbench.errorMessage("Could not successfully store " + netID, e, false);
			}
		}

	}

}
