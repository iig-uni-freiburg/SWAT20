package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.Window;
import java.io.File;

import de.invation.code.toval.graphic.dialog.FileNameDialog;

public class AnalysisNameChooser extends FileNameDialog {

	private static final String ANALYSIS_NAME_ALREADY_IN_USE = "This analysis name is already in use";

	private String netPath;
	private String prefix;
	public AnalysisNameChooser(Window parent, String title, String path, String pf) {
		super(parent, "Choose name for analysis:", title, false);
		// TODO Auto-generated constructor stub
		netPath=path;
		prefix=pf;
	}
	@Override
	protected boolean isValid(String input) {
		if(super.isValid(input)){
			String filePath=netPath+prefix+input+".xml";
			System.out.println(filePath);
			if(new File(filePath).exists()){
				errorMessage = ANALYSIS_NAME_ALREADY_IN_USE;
				return false;
			}
			return true;
		}
		return false;
	}

}
