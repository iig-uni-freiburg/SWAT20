package de.uni.freiburg.iig.telematik.swat.simon.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.swat.simon.AwesomeTimeContext;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;

public class TimeContextToolbar extends JToolBar {
	
	private static final long serialVersionUID = -8384088368631021354L;
	private TimeContextGui gui;

	public TimeContextToolbar(TimeContextGui gui){
		this.gui = gui;
		setup();
	}

	private void setup() {

		add(getSaveButton());
		add(getSaveAsButton());
		
	}
	
	private JButton getSaveButton(){
		JButton save = new JButton("save");
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					gui.save();
				} catch (ProjectComponentException e1) {
					Workbench.errorMessage("Could not save time context", e1, true);
				}
				
			}
		});
		return save;
	}
	
	private JButton getSaveAsButton(){
		JButton saveAs = new JButton("Save As...");
		
		saveAs.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String newName = JOptionPane.showInputDialog(saveAs, "New name for context?");
				if(newName==null||newName.isEmpty())
					return;
				
				AwesomeTimeContext original = (AwesomeTimeContext) gui.getContext();
				AwesomeTimeContext newContext = original.clone();
				newContext.setName(newName);
				try {
					SwatComponents.getInstance().getTimeContextContainer().addComponent(newContext, true);
				} catch (ProjectComponentException e1) {
					Workbench.errorMessage("Could not store timecontext "+newName, e1, true);
				}
				
				
			}
		});
		
		return saveAs;
	}

}