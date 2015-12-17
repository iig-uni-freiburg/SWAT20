package de.uni.freiburg.iig.telematik.swat.jascha.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPopupMenu;

import de.uni.freiburg.iig.telematik.swat.jascha.ResourceStore;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceType;
import de.uni.freiburg.iig.telematik.swat.workbench.action.AFSqlLogImportAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.AFtemplateImportAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.BpmnImportAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.LogImportAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.PTImportAction;

/** shows menu pop-up with all available resource types**/
public class addResourceAction implements ActionListener {
	
	private ResourceStore store;

	public addResourceAction(ResourceStore store) {
		this.store=store;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		final JPopupMenu menu = new JPopupMenu("Menu");
		for(ResourceType item:ResourceType.values()){
			menu.add(new addDefinedResourceAction(item, store));
		}
		menu.setInvoker((Component) e.getSource());
		menu.setLocation((int) source.getLocationOnScreen().getX(), (int) source.getLocationOnScreen().getY());
		menu.setVisible(true);

	}

}
