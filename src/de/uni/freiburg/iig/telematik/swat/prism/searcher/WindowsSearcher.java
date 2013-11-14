package de.uni.freiburg.iig.telematik.swat.prism.searcher;

import java.io.IOException;

import de.invation.code.toval.validate.ParameterException;

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

}
