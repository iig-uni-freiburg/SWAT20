package de.uni.freiburg.iig.telematik.swat.analysis.prism;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.invation.code.toval.graphic.dialog.ConditionalFileDialog.FileChooserType;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.misc.OperatingSystem;
import de.uni.freiburg.iig.telematik.swat.misc.OperatingSystem.OperatingSystems;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            try {
                File fullpath=new File(SwatProperties.getInstance().getPrismPath());
                return checkPrism(fullpath);
            } catch (PropertyException e) {
			Workbench.errorMessage("Could not retrieve Prism Path. Please set prism path", null, true);
		} catch (IOException e) {
			Workbench.errorMessage("Could not ressolve Prism Model Checker", null, true);
		}
            return false;
        }

	public static boolean checkPrism(File path) {
		if (!generatePrismTestFiles())
			return false; //could not generate files in temp dir

		PrismRunner pr = new PrismRunner();
		try {
			//File fullPrismPath = getFullPrismPath(SwatProperties.getInstance().getPrismPath());
                    File fullPrismPath=path;
			pr.setPrismFile(fullPrismPath);
			pr.setModelFile(tempModel);
			pr.setPropertyFile(tempProperty);
			System.out.println("Verifiying Prism executable " + fullPrismPath.getAbsolutePath());
			if (pr.verifyProperties())
				return true; //all good
		} catch (ParameterException e) {
			Workbench.errorMessage("Error loading Prism", null, true);
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
			Workbench.errorMessage("Could not access temp dir: " + tempModel.getAbsolutePath(), e, false);
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
