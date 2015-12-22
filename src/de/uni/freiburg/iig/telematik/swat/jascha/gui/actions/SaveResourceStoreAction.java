package de.uni.freiburg.iig.telematik.swat.jascha.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceStore;
import de.uni.freiburg.iig.telematik.swat.jascha.fileHandling.ResourceStoreContainer;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;

public class SaveResourceStoreAction extends AbstractAction {
	ResourceStore resourceStore;
	 public SaveResourceStoreAction(ResourceStore store) {
		super("save...");
		this.resourceStore=store;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String name = JOptionPane.showInputDialog("rename?",resourceStore.getName());
		if(name==null && name.isEmpty()){
			JOptionPane.showMessageDialog((Component) e.getSource(), "User input invalid, cannot save...");
			return;
		}
		try {
			 ResourceStoreContainer comp = SwatComponents.getInstance().getResourceStoreContainer();
			if(name.equals(resourceStore.getName())){
				if(comp.containsComponent(name))
					comp.storeComponents();
				else
					comp.addComponent(resourceStore, true, true);
			} else {
				//clone and rename resource store
				ResourceStore clone = resourceStore.clone();
				clone.setName(name);
				comp.addComponent(clone, true, true);
			}
			
		} catch (ProjectComponentException e2) {
			Workbench.errorMessage("Could not store ResourceStore", e2, true);
		}

	}

}
