package de.uni.freiburg.iig.telematik.swat.workbench.dialog;

import java.awt.Window;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import de.invation.code.toval.graphic.dialog.ConditionalFileDialog;
import de.invation.code.toval.validate.ParameterException;

public class LolaPathChooser extends ConditionalFileDialog {

	private static final String errorMessage = "Cannot find lola executables in chosen directory.\n Please choose another directory.";

	public static void main(String[] args) {
		LolaPathChooser lola = new LolaPathChooser(null);
		System.out.println(lola.chooseFile());
	}

	public LolaPathChooser(Window parent) {
		super(parent, ConditionalFileDialog.FileChooserType.DIRECTORY, "Choose Lola directory");

	}

	@Override
	protected String getErrorMessage() {
		return errorMessage;
	}

	@Override
	protected boolean isValid(File path){
		try {
			LolaPathValidater.validate(path);
		} catch (ParameterException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	

	
	
}

class LolaPathValidater {
	static boolean validate(File path) throws ParameterException, IOException {
		File[] possibleFiles = path.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().contains("lola");
			}
		});

		for (File possibleFile : possibleFiles) {
			if (possibleFile.canExecute())
				return true;
		}
		return false;
	}
}