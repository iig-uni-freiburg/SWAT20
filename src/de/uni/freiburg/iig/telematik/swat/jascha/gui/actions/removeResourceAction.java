package de.uni.freiburg.iig.telematik.swat.jascha.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceStore;

public class removeResourceAction implements ActionListener {
	
	private ResourceStore store;
	private JList<IResource> list;

	public removeResourceAction(ResourceStore store, JList<IResource> list) {
		this.store=store;
		this.list=list;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		store.removeResource(list.getSelectedValue());
	}

}
