package de.uni.freiburg.iig.telematik.swat.workbench.action;


import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class NewWorkingDirectoryAction extends AbstractWorkingDirectoryAction {

	private static final long serialVersionUID = 3421975574956233676L;
	
	public NewWorkingDirectoryAction(Window parentWindow){
		super(parentWindow, "New Working Directory");
	}

	@Override
	public void doFancyStuff(ActionEvent e) throws Exception {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Choose location for new working directory");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fileChooser.showOpenDialog(parent);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	File file = fileChooser.getSelectedFile();
            String simulationDirectoryLocation = file.getAbsolutePath();
            File dir = new File(simulationDirectoryLocation + "/" + SwatProperties.defaultWorkingDirectoryName);
            if(dir.exists()){
            	int count = 1;
            	while((dir = new File(simulationDirectoryLocation + "/" + SwatProperties.defaultWorkingDirectoryName + count)).exists()){
            		count++;
            	}
            } 
            dir.mkdir();
            String simulationDirectory = dir.getAbsolutePath() + "/";
 
            addKnownWorkingDirectory(simulationDirectory, true);
        }
	}

}
