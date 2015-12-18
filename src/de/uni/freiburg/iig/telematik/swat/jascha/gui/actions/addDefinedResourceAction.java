package de.uni.freiburg.iig.telematik.swat.jascha.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.uni.freiburg.iig.telematik.swat.jascha.ResourceStore;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceType;
import de.uni.freiburg.iig.telematik.swat.jascha.SharedResource;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.JOptionPaneMultiInput;

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
		JOptionPaneMultiInput dialog; 
		switch (type) {
		case SIMPLE:
			String s = (String)JOptionPane.showInputDialog(null,"Enter name of new Resource:","Resource name", JOptionPane.PLAIN_MESSAGE);
			store.instantiateResource(type, s);
			break;
		case SET:
			dialog = new JOptionPaneMultiInput("name","number");
			store.instantiateResource(type, dialog.getResult(0), Integer.parseInt(dialog.getResult(1)));
			break;
		case SHARED:
			dialog = new JOptionPaneMultiInput("name", "max. capacity");
			SharedResource res = (SharedResource) store.instantiateResource(type, dialog.getResult(0));
			int maxCapacity = Integer.parseInt(dialog.getResult(1));
			res.setIncrement(1f/maxCapacity);
			break;
		case COMPOUND:
			//TODO
			
		default:
			break;
		}
		


	}
	



}
