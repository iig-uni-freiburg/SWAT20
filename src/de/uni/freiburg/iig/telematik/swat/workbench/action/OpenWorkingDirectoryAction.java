package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

public class OpenWorkingDirectoryAction extends AbstractWorkingDirectoryAction {

	private static final long serialVersionUID = 3421975574956233676L;

	public OpenWorkingDirectoryAction(Window parentWindow){
		super(parentWindow, "Existing Directory");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Choose existing working directory");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fileChooser.showOpenDialog(parent);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String workingDirectory = file.getAbsolutePath()+System.getProperty("file.separator");
			addKnownWorkingDirectory(workingDirectory);
        }
	}

}
