package de.uni.freiburg.iig.telematik.swat.workbench.action;


import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatProperties;

public class NewWorkingDirectoryAction extends AbstractWorkingDirectoryAction {

	private static final long serialVersionUID = 3421975574956233676L;
	
	public NewWorkingDirectoryAction(Window parentWindow){
		super(parentWindow, "New Working Directory");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Choose location for new working directory");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fileChooser.showOpenDialog(parent);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String workingDirectoryLocation = file.getAbsolutePath();
            File dir = new File(workingDirectoryLocation + System.getProperty("file.separator") + SwatProperties.defaultWorkingDirectoryName);
            if(dir.exists()){
            	int count = 1;
            	while((dir = new File(workingDirectoryLocation +  System.getProperty("file.separator") + SwatProperties.defaultWorkingDirectoryName + count)).exists()){
            		count++;
            	}
            } 
            dir.mkdir();
            String workingDirectory = dir.getAbsolutePath() + System.getProperty("file.separator");
 
            try {
				addKnownWorkingDirectory(workingDirectory);
			} catch (PropertyException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Property Exception", JOptionPane.ERROR_MESSAGE);
			}
        }
	}

}
