package de.uni.freiburg.iig.telematik.swat.simon.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;

public class TimeContextToolbar extends JToolBar {
	
	private TimeContextGui gui;

	public TimeContextToolbar(TimeContextGui gui){
		this.gui = gui;
		setup();
	}

	private void setup() {
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
		add(save);
		
	}

}
