package de.uni.freiburg.iig.telematik.swat.analysis.prism.searcher;

import java.io.File;
import java.io.IOException;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;

/** Searches for Prism model checker on Windows OS **/
public class WindowsSearcher extends PrismSearcher {
	
	protected WindowsSearcher() throws IOException, ParameterException {
		super();
	}

	public static final String WIN_DIR_PROGRAM_FILES = System.getenv("%programfiles%");
	public static final String WIN_DIR_PROGRAM_FILES_X86 = System.getenv("%programfiles% (x86)");
	
	@Override
	protected void addSystemSpecificPaths() {
		try {
			prismPaths.addAll(getPotentialPrismSubdirectories(WIN_DIR_PROGRAM_FILES));
			prismPaths.addAll(getPotentialPrismSubdirectories(WIN_DIR_PROGRAM_FILES_X86));
		} catch (Exception e) {
			// Con't care about invalid paths.
		}
	}

	/**
	 * search possible places for prism executable.
	 * 
	 * @return The first executable that exists. Null if non exists
	 */
	public File getPrismPath() {
		File test = null;
		// test for all possible files if it exists and is executable
		for (File file : prismPaths) {
			if ((test = new File(file, "bin/prism.bat")).exists()) {
				// log.log(Level.INFO, "Found Prism Model Checker: " + file);
				if (test.canExecute())
					return file;
			}
		}
		return null;
	}

	public static File validatePrismPath(String directory) throws ParameterException {
		Validate.directory(directory);

		File testFile = new File(directory, "bin" + System.getProperty("file.separator") + "prism.bat");
		Validate.noDirectory(testFile);
		if (!testFile.exists())
			throw new ParameterException("");

		// log.log(Level.INFO, "Found possible Prism Model Checker: " +
		// directory);

		if (!testFile.canExecute())
			throw new ParameterException("Cannot execute prism");
		
		return testFile;
	}

}
