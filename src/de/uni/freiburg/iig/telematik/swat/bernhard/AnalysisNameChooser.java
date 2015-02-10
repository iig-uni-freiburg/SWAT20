package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.Window;
import java.io.File;

import javax.swing.JOptionPane;

import de.invation.code.toval.graphic.dialog.FileNameDialog;

public class AnalysisNameChooser extends FileNameDialog {

	private static final String ANALYSIS_NAME_ALREADY_IN_USE = "This analysis name is already in use. Overwrite?";

	private String netPath;
	private boolean storeFile;
	public AnalysisNameChooser(Window parent, String title, String path) {
		super(parent, "Choose name for analysis:", title, false);
		// TODO Auto-generated constructor stub
		netPath = path;
		storeFile=false;
	}

	
	public boolean storeFile() {
		return storeFile;
	}


	@Override
	protected boolean isValid(String input) {
		//		if (super.isValid(input)) {
		//			String filePath = netPath + AnalysisStorage.PREFIX + input + AnalysisStorage.SUFFIX;
		//			storeFile=true;
		//			if (new File(filePath).exists()) {
		//				errorMessage = ANALYSIS_NAME_ALREADY_IN_USE;
		//				if (JOptionPane.showConfirmDialog(null,
		//						ANALYSIS_NAME_ALREADY_IN_USE, "Warning",
		//						JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
		//					storeFile=false;
		//				}
		//			}
		//			return true;
		//		}
		//		return false;
		return super.isValid(input);
	}

}
