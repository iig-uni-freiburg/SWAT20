package de.uni.freiburg.iig.telematik.swat.prism;

import java.awt.Window;
import java.io.File;
import java.io.IOException;

import de.invation.code.toval.graphic.dialog.ConditionalFileDialog;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.prism.searcher.PrismSearcherFactory;

public class PrismPathChooser extends ConditionalFileDialog {

	private static final String errorMessage = "Cannot find prism executables in chosen directory.\n Please choose another directory.";

	public PrismPathChooser(Window parent) {
		super(parent, ConditionalFileDialog.FileChooserType.DIRECTORY, "Choose Prism directory");

	}

	@Override
	protected String getErrorMessage() {
		return errorMessage;
	}

	@Override
	protected boolean isValid(File path){
		try {
			PrismSearcherFactory.getPrismSearcher().validatePrismPath(path.getAbsolutePath());
		} catch (ParameterException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	
	public static void main(String[] args) {
		new PrismPathChooser(null).chooseFile();
	}
	
	
}
