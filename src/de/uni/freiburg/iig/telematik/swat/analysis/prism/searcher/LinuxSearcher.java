package de.uni.freiburg.iig.telematik.swat.analysis.prism.searcher;

import java.io.IOException;

import de.invation.code.toval.validate.ParameterException;

/** search for Prism on Linux **/
public class LinuxSearcher extends PrismSearcher {
	
	public static final String LIN_DIR_USER_HOME_BIN = DIR_USER_HOME.concat(System.getProperty("file.separator")).concat("bin");
	public static final String LIN_DIR_USER_DIR_BIN = DIR_USER_DIR.concat(System.getProperty("file.separator")).concat("bin");
	
	public LinuxSearcher() throws IOException, ParameterException {
		super();
	}

	@Override
	protected void addSystemSpecificPaths() {
		prismPaths.addAll(getPotentialPrismSubdirectories(LIN_DIR_USER_DIR_BIN));
		prismPaths.addAll(getPotentialPrismSubdirectories(LIN_DIR_USER_HOME_BIN));
	}
	
	


}
