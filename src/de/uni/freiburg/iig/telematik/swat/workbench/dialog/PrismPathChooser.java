package de.uni.freiburg.iig.telematik.swat.workbench.dialog;

import java.awt.Window;
import java.io.File;
import java.io.IOException;

import de.invation.code.toval.graphic.dialog.ConditionalFileDialog;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.analysis.prism.PrismFunctionValidator;
import de.uni.freiburg.iig.telematik.swat.analysis.prism.searcher.PrismSearcherFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;

public class PrismPathChooser extends ConditionalFileDialog {

	private static final String errorMessage = "Cannot find prism executables under bin/ within chosen directory.\n Please choose another directory.";

	public PrismPathChooser(Window parent) {
		//super(parent, ConditionalFileDialog.FileChooserType.DIRECTORY, "Choose Prism directory");
		super(parent,ConditionalFileDialog.FileChooserType.FILE,"Choose Prism executable");

	}

	@Override
	protected String getErrorMessage() {
		return errorMessage;
	}

	@Override
	protected boolean isValid(File path){
		try {
			File prism = PrismSearcherFactory.getPrismSearcher().validatePrismPath(path.getAbsolutePath());

			//check prism function
			if (!PrismFunctionValidator.checkPrism(prism))
				Workbench
						.errorMessage(
								"Prism failed the execution test. this prism version might differ from prism-4.2.beta1.\br might cause trouble with SWAT",
								null, false);
		} catch (ParameterException e) {
			Workbench.errorMessage("Could not get prism executable path", e, true);
			return false;
		} catch (IOException e) {
			Workbench.errorMessage("Could not access prism executable", e, true);
			return false;
		}
		return true;
	}
	
	
	public static void main(String[] args) {
		new PrismPathChooser(null).chooseFile();
	}
	
	
}
