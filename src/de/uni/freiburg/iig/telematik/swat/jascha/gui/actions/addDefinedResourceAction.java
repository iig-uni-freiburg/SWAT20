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
	
	private static final long serialVersionUID = -1299110255599903016L;
	
	private ResourceType type;
	private ResourceStore store;
	
	public addDefinedResourceAction(ResourceType type, ResourceStore store) {
		super(type.toString());
		this.type=type;
		putValue(SHORT_DESCRIPTION, "add "+type.toString());
		putValue(LONG_DESCRIPTION, "add "+type.toString()+" resource");
		this.store=store;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JOptionPaneMultiInput dialog;
		switch (type) {
		case SIMPLE:
		case HUMAN:
		case COMPOUND:
			String s = (String) JOptionPane.showInputDialog(null, "Enter name of new Resource:", "Resource name",JOptionPane.PLAIN_MESSAGE);
			if(s!=null && !s.isEmpty())
				store.instantiateResource(type, s);
			break;
		case SET:
			dialog = new JOptionPaneMultiInput("name", "number");
			//If no name is specified for the ResourceSet, do nothing.
			//If no size is specified for the ResourceSet, create set with one element			
			if (!dialog.getResult(0).isEmpty() && dialog.getResult(1).isEmpty()){
				store.instantiateResource(type, dialog.getResult(0));
			}				
			else if (!dialog.getResult(0).isEmpty()){
				store.instantiateResource(type, dialog.getResult(0), Integer.parseInt(dialog.getResult(1)));
			}			
			break;
		case SHARED:
			dialog = new JOptionPaneMultiInput("name", "max. capacity");
			if (dialog.getResult(0).isEmpty()){
				//Don't create a resource without a name, maybe give feedback to the user
				System.out.println("Someone tried to create a SharedResource without name ...");
				break;				
			}
			if (dialog.hasUserInput()) {
				SharedResource res = (SharedResource) store.instantiateResource(type, dialog.getResult(0));
				if (!dialog.getResult(1).isEmpty()){
					int maxCapacity = Integer.parseInt(dialog.getResult(1));
					res.setIncrement(1f / maxCapacity);
				} else {
					System.out.println("The capacity field was left empty!");
					// do nothing since the resource has already been created and has an increment size of 0.1 by default
				}

			}
			break;

		default:
			break;
		}

	}
	



}
