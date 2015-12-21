package de.uni.freiburg.iig.telematik.swat.jascha.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import de.uni.freiburg.iig.telematik.swat.jascha.ResourceStore;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.ResourceStoreGUI;

public class ChangeResourceStoreNameAction extends AbstractAction {
	
	ResourceStore store;
	
	public ChangeResourceStoreNameAction(ResourceStore store) {
		super("change name");
		this.store=store;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String result = JOptionPane.showInputDialog("name");
		if(result!=null && !result.isEmpty()) {
			store.setName(result);
		}
		else {
			JOptionPane.showMessageDialog(null, "Could not change name");
		}

	}

}
