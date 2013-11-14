package de.uni.freiburg.iig.telematik.swat.prism;

import java.awt.Window;
import java.io.File;

import de.invation.code.toval.graphic.ConditionalFileChooser;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.prism.searcher.PrismSearcher;

public class PrismPathChooser extends ConditionalFileChooser {

	private static final String errorMessage = "Cannot find prism executables in chosen directory.\n Please choose another directory.";

	public PrismPathChooser(Window parent) {
		super(parent, ConditionalFileChooser.FileChooserType.DIRECTORY, "Choose Prism directory");

	}

	@Override
	protected String getErrorMessage() {
		return errorMessage;
	}

	@Override
	protected boolean isValid(File path){
		try {
			PrismSearcher.validatePrismPath(path.getAbsolutePath());
		} catch (ParameterException e) {
			return false;
		}
		return true;
	}
	public static void main(String[] args) {
		new PrismPathChooser(null).chooseFile();
	}
	
	
}
