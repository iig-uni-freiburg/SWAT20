package de.uni.freiburg.iig.telematik.swat.prism;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.invation.code.toval.graphic.dialog.ConditionalFileDialog.FileChooserType;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.misc.OperatingSystem;
import de.uni.freiburg.iig.telematik.swat.misc.OperatingSystem.OperatingSystems;
import de.uni.freiburg.iig.telematik.swat.prism.generator.PrismRunner;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class PrismFunctionValidator {

	private static File tempModel;
	private static File tempProperty;

	private static String prismModel = "dtmc module die " + "s : [0..7] init 0; " + "d : [0..6] init 0;"
			+ "[] s=0 -> 0.5 : (s'=1) + 0.5 : (s'=2);" + "[] s=1 -> 0.5 : (s'=3) + 0.5 : (s'=4);"
			+ "[] s=2 -> 0.5 : (s'=5) + 0.5 : (s'=6);" + "[] s=3 -> 0.5 : (s'=1) + 0.5 : (s'=7) & (d'=1);"
			+ "[] s=4 -> 0.5 : (s'=7) & (d'=2) + 0.5 : (s'=7) & (d'=3);" + "[] s=5 -> 0.5 : (s'=7) & (d'=4) + 0.5 : (s'=7) & (d'=5);"
			+ "[] s=6 -> 0.5 : (s'=2) + 0.5 : (s'=7) & (d'=6);" + "[] s=7 -> (s'=7);" + "endmodule";

	private static String prismProperty = "P=? [ F s=7 & d=1 ]";

	public static void main(String[] args) {
		System.out.println(checkPrism());
		FileChooserType type;
	}

	public static boolean checkPrism() {
		if (!generatePrismTestFiles())
			return false; //could not generate files in temp dir

		PrismRunner pr = new PrismRunner();
		try {
			File fullPrismPath = getFullPrismPath(SwatProperties.getInstance().getPrismPath());
			pr.setPrismFile(fullPrismPath);
			pr.setModelFile(tempModel);
			pr.setPropertyFile(tempProperty);
			System.out.println("Verifiying Prism executable " + fullPrismPath.getAbsolutePath());
			if (pr.verifyProperties())
				return true; //all good
		} catch (ParameterException e) {
			Workbench.errorMessageWithNotification("Error loading Prism");
			e.printStackTrace();
		} catch (PropertyException e) {
			Workbench.errorMessageWithNotification("Could not retrieve Prism Path. Please set prism path");
			e.printStackTrace();
		} catch (IOException e) {
			Workbench.errorMessageWithNotification("Could not ressolve Prism Model Checker");
			e.printStackTrace();
		}
		return false;
	}

	private static boolean generatePrismTestFiles() {
		try {
			tempModel = File.createTempFile("PrismModel", ".tmp");
			FileWriter fw = new FileWriter(tempModel);
			fw.write(prismModel);
			fw.close();

			tempProperty = File.createTempFile("PrismProperty", ".tmp");
			fw = new FileWriter(tempProperty);
			fw.write(prismProperty);
			fw.close();
			return true;

		} catch (IOException e) {
			Workbench.errorMessageWithNotification("Could not access temp dir: " + tempModel.getAbsolutePath());
			e.printStackTrace();
			return false;
		}
	}

	private static File getFullPrismPath(String prismPath) {
		File fullPath;
		OperatingSystems os = OperatingSystem.getOperatingSystem();
		switch (os) {
		case win:
			fullPath = new File(prismPath, "bin/prism.bat");
			return fullPath;
		case linux:
		case mac:
			fullPath = new File(prismPath, "bin/prism");
			return fullPath;
		default:
			return new File(prismPath, "bin/prism.bat");
		}

	}
}
