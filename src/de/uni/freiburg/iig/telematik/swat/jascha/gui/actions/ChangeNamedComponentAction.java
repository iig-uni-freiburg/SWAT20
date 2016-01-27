package de.uni.freiburg.iig.telematik.swat.jascha.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import de.invation.code.toval.misc.NamedComponent;
import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.sepia.util.PNUtils;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceStore;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.ResourceStoreGUI;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;

public class ChangeNamedComponentAction extends AbstractAction {
	
	NamedComponent store;
	
	public ChangeNamedComponentAction(NamedComponent store) {
		super("change name");
		this.store=store;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String result = JOptionPane.showInputDialog("name");
		if(result!=null && !result.isEmpty()) {
			result = PNUtils.sanitizeElementName(result, "r");
			//test if ResourceStore is in SwatComponents.
			try {
				if(SwatComponents.getInstance().getResourceStoreContainer().containsComponent(store.getName())){
					SwatComponents.getInstance().getResourceStoreContainer().renameComponent(store.getName(), result, true);
				} else {
					//just change name
					store.setName(result);
				}
			} catch (ProjectComponentException e2) {
				Workbench.errorMessage("Could not rename ResourceStore", e2, true);
			}

		}
		else {
			JOptionPane.showMessageDialog(null, "Could not change name: User Input invalid");
		}

	}

}
