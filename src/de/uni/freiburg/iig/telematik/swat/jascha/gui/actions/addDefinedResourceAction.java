package de.uni.freiburg.iig.telematik.swat.jascha.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import de.uni.freiburg.iig.telematik.swat.jascha.ResourceStore;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceType;

public class addDefinedResourceAction extends AbstractAction {
	
	private ResourceType type;
	private ResourceStore store;
	
	public addDefinedResourceAction(ResourceType type, ResourceStore store) {
		super(type.toString());
		this.type=type;
		putValue(SHORT_DESCRIPTION, type.toString());
		putValue(LONG_DESCRIPTION, type.toString());
		this.store=store;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String s = (String)JOptionPane.showInputDialog(null,"Enter name of new Resource:","Resource name", JOptionPane.PLAIN_MESSAGE);
		store.instantiateResource(type, s);


	}

}
