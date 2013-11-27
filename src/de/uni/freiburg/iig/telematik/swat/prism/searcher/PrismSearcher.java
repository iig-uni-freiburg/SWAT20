package de.uni.freiburg.iig.telematik.swat.prism.searcher;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.invation.code.toval.file.FileUtils;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

/**
 * Searches for prism executable. Depending on OS, the corresponding PrismSearch
 * extension is chosen ({@link LinuxSearch}, {@link WindowsSearch} or
 * {@link MacOSSearch}) and returned by {@link #getInstance()}.
 **/
public abstract class PrismSearcher {
//	protected static Logger log = BPLog.getLogger(SplitGui.class.getName());
	/**
	 * Possible Paths were Prism could be installed. Is additionally populated
	 * by decorators (Classes extending this class)
	 **/
	protected List<File> prismPaths = new LinkedList<File>();
	
	protected static final String separator = System.getProperty("file.separator");
	
	public static final String DIR_USER_HOME = System.getProperty("user.home");
	public static final String DIR_USER_DIR = System.getProperty("user.dir");
	
	FilenameFilter filter = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			return name.toLowerCase().contains("prism");
		}
	};

	protected PrismSearcher() throws IOException, ParameterException {
		// For all operating systems: Search in application path of SWAT
		prismPaths.addAll(FileUtils.getSubdirectories(SwatProperties.getInstance().getApplicationPath()));
		// Also, try user.home and user.dir with and without bin/ appended
		addUsersHome();
		addSystemSpecificPaths();
	}
	
	protected abstract void addSystemSpecificPaths();

	/**
	 * add the user's home dir to the list of possible prism paths (
	 * {@link #prismPaths})
	 **/
	private void addUsersHome() {
		prismPaths.addAll(getPotentialPrismSubdirectories(DIR_USER_DIR));
		prismPaths.addAll(getPotentialPrismSubdirectories(DIR_USER_HOME));
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
			if ((test = new File(file, "bin/prism")).exists()) {
//				log.log(Level.INFO, "Found Prism Model Checker: " + file);
				if (test.canExecute())
					return file;
			}
		}
		return null;
	}

	/**
	 * check if given file (path) contains the file bin/prism and is executable
	 * by the VM
	 **/
	public boolean checkPrismPath(File path) {
		File test = new File(path, "bin".concat(System.getProperty("file.separator")).concat("prism"));
		if (test.exists()) {
//			log.log(Level.INFO, "Found possible Prism Model Checker: " + path);
			if (test.canExecute())
				return true;
		}
		return false;
	}
	
	protected Set<File> getPotentialPrismSubdirectories(String path) {
		Set<File> potentialPaths = new HashSet<File>();
		try {
			Validate.directory(path);
			
			for(File potentialDir: new File(path).listFiles(filter)){
				if(potentialDir.isFile())
					potentialPaths.add(potentialDir);
			}	
		} catch (Exception e) {
			// Don't care about invalid paths.
		}
		return potentialPaths;
	}

	public static void validatePrismPath(String directory) throws ParameterException{
		Validate.directory(directory);
		
		File testFile = new File(directory, "bin"+System.getProperty("file.separator")+"prism");
		Validate.noDirectory(testFile);
		if(!testFile.exists())
			throw new ParameterException("");
			
//		log.log(Level.INFO, "Found possible Prism Model Checker: " + directory);
		
		if (!testFile.canExecute())
			throw new ParameterException("Cannot execute prism");
	}
}