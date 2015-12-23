package de.uni.freiburg.iig.telematik.swat.jascha.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.filechooser.FileView;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.jascha.AwesomeResourceContext;
import de.uni.freiburg.iig.telematik.swat.jascha.fileHandling.ResourceContainer;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;

public class ResourceContextToolbar extends JToolBar {
	ResourceContextGUI gui;
	
	public ResourceContextToolbar(ResourceContextGUI gui) {
		this.gui=gui;
		add(getSaveButton());
		add(getRenameButton());
	}
	
	private JButton getSaveButton(){
		JButton save = new JButton(UIManager.getIcon("FileView.floppyDriveIcon"));
		save.setToolTipText("save this context");
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ResourceContainer container;
				try {
					container = SwatComponents.getInstance().getResourceContainer();
					if(container.containsComponent(gui.getContext().getName())){
						container.storeComponents();
					}
					else {
						container.addComponent(gui.getContext(), true, true);
					}
				} catch (ProjectComponentException e1) {
					Workbench.errorMessage("Could not save "+gui.getName(), e1, true);
				}
				
				
			}
		});
		return save;
	}
	
	private JButton getRenameButton(){
		JButton rename;

		try {
			rename = new JButton(IconFactory.getIcon("save as..."));
		} catch (ParameterException | PropertyException | IOException e) {
			rename=new JButton("rename");
		}
		rename.setToolTipText("save as...");
		rename.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String newName = JOptionPane.showInputDialog(this, "new Name?");
				if (newName.equals(gui.getContext().getName())) return;
				AwesomeResourceContext clone = gui.getContext().clone();
				clone.setName(newName);
				try {
					if(!SwatComponents.getInstance().getResourceContainer().containsComponent(newName)){
						SwatComponents.getInstance().getResourceContainer().addComponent(clone, true, true);
					}
				} catch (ProjectComponentException e1) {
					Workbench.errorMessage("Could not store "+newName, e1, true);
				}
				
				
			}
		});
		
		return rename;
	}

}
