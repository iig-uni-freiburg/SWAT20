package de.uni.freiburg.iig.telematik.swat.jascha.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.filechooser.FileView;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.swat.jascha.AwesomeResourceContext;
import de.uni.freiburg.iig.telematik.swat.jascha.fileHandling.ResourceContainer;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;

public class ResourceContextToolbar extends JToolBar {
	AwesomeResourceContext context;
	
	public ResourceContextToolbar(AwesomeResourceContext context) {
		this.context=context;
		add(getSaveButton());
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
					if(container.containsComponent(context.getName())){
						container.storeComponents();
					}
					else {
						container.addComponent(context, true, true);
					}
				} catch (ProjectComponentException e1) {
					Workbench.errorMessage("Could not save "+context.getName(), e1, true);
				}
				
				
			}
		});
		return save;
	}

}
