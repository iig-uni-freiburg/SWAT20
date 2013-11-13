package de.unifreiburg.iig.bpworkbench2.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.unifreiburg.iig.bpworkbench2.controller.SWAT2Controller;
import de.unifreiburg.iig.bpworkbench2.gui.SplitGui;
import de.unifreiburg.iig.bpworkbench2.logging.BPLog;

/**
 * Starts (and finds) Prism Model Checker. Use {@link #execPrism}
 **/
public class PrismRunner {
	private static Logger log = BPLog.getLogger(SplitGui.class.getName());
	private File prismPath;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		PrismSearch prismsearch = PrismSearch.getInstance();
		File prismPath = prismsearch.getPrismPath();
		System.out.println("_____Path to Prism: " + prismPath);
		SwatProperties.getInstance().setProperty("PrismPath", prismPath.toString());
		List<File> files = prismsearch.getPossiblePaths();
		for (File file : files) {
			System.out.println(file.toString());
		}

	}

	/**
	 * Class to start and to search for prism executable
	 */
	public PrismRunner() {
		// search for Prism path. Check property. If not set, search for it
		prismPath = new File(SwatProperties.getInstance().getProperty("PrismPath", PrismSearch.getInstance().getPrismPath().toString()));
	}

	/**
	 * Run Prism Model Checker with given Path to model and one or more
	 * properties
	 **/
	public String execPrism(String modelPath, String... propertyPath) {
		// TODO: make it threaded?
		// prepare property String
		String propertyString = generatePropertyString(propertyPath);
		Process p = null;

		try {
			// Start Prism
			Runtime rt = Runtime.getRuntime();
			p = rt.exec(prismPath.toString() + " " + modelPath + propertyString);
			// Read in- and outputstream
			InputStream in = p.getInputStream();
			OutputStream out = p.getOutputStream();
			InputStream err = p.getErrorStream();

			// get result
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String buffer = "";
			StringBuilder stringBuilder = new StringBuilder();
			while ((buffer = br.readLine()) != null) {
				stringBuilder.append(buffer);
			}
			String result = stringBuilder.toString();
			p.destroy();
			return result;

		} catch (Exception exc) {/* handle exception */
			log.log(Level.SEVERE, "Could not start PRISM Model Checker");
			return null;
		} finally {
			try {
				p.destroy();
			} catch (NullPointerException e) {
			}
		}
	}

	private String generatePropertyString(String[] propertyPath) {
		StringBuilder sb = new StringBuilder(100);
		for (int i = 0; i < propertyPath.length; i++) {
			sb.append(" ");
			sb.append(propertyPath[i]);
		}
		return sb.toString();
	}

	/** Returns path to prism as String **/
	public String searchForPrism() {
		return PrismSearch.getInstance().getPrismPath().toString();
	}

	public boolean validatePrismPath(String path) {
		if (path == null || path.equals("")) {
			return false;
		}
		return PrismSearch.getInstance().checkPrismPath(new File(path));
	}
}

/**
 * Searches for prism executable. Depending on OS, the corresponding PrismSearch
 * extension is chosen ({@link LinuxSearch}, {@link WindowsSearch} or
 * {@link MacOSSearch}) and returned by {@link #getInstance()}.
 **/
class PrismSearch {
	protected static Logger log = BPLog.getLogger(SplitGui.class.getName());
	private static String OS = System.getProperty("os.name").toLowerCase();
	/**
	 * Possible Paths were Prism could be installed. Is additionally populated
	 * by decorators (Classes extending this class)
	 **/
	protected List<File> prismPaths = new LinkedList<File>();
	FilenameFilter filter = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			return name.toLowerCase().contains("prism");
		}
	};

	protected PrismSearch() {
		// For all operating systems:
		// Search in runtimepath of SWAT
		addExecPath();
		// Also, try user.home and user.dir with and without bin/ appended
		addUsersHome();
	}

	/**
	 * add the user's home dir to the list of possible prism paths (
	 * {@link #prismPaths})
	 **/
	private void addUsersHome() {
		// get Os specific Path seperator
		String sep = System.getProperty("file.separator");
		// search prism in user home (and $home/bin)
		String userHome = System.getProperty("user.home");
		String userDir = System.getProperty("user.dir");

		// get possible files as list (user.home/bin/*prism*)
		prismPaths.addAll(Arrays.asList(new File(userHome + sep + "bin" + sep).listFiles(filter)));
		// and directly the user home (user.home/*prism*)
		prismPaths.addAll(Arrays.asList(new File(userHome).listFiles(filter)));
		// get possible files as list (user.dir/bin/*prism*)
		prismPaths.addAll(Arrays.asList(new File(userDir + sep + "bin" + sep).listFiles(filter)));
		// Also use working directory (user.dir/*prism*)
		prismPaths.addAll(Arrays.asList(new File(userDir).listFiles(filter)));
	}

	private void addExecPath() {
		// Search in execution path, underlying path and underlying underlying
		// path of SWAT2
		File runtimePath = new File(SWAT2Controller.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		prismPaths.addAll(Arrays.asList(runtimePath.listFiles(filter)));
		prismPaths.addAll(Arrays.asList(runtimePath.getParentFile().listFiles(filter)));
		prismPaths.addAll(Arrays.asList(runtimePath.getParentFile().getParentFile().listFiles(filter)));
	}

	/** Depending on the users OS, get the correct PrismSearch Object **/
	public static PrismSearch getInstance() {

		if (isWindows())
			return (PrismSearch) new WindowsSearch();
		else if (isMac())
			return (PrismSearch) new MacOSSearch();
		else if (isUnix() || isSolaris())
			return (PrismSearch) new LinuxSearch();
		else
			return null; // or better pretend to be windows?
	}

	public List<File> getPossiblePaths() {
		return prismPaths;
	}

	private static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	private static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	private static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}

	private static boolean isSolaris() {
		return (OS.indexOf("sunos") >= 0);
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
				log.log(Level.INFO, "Found Prism Model Checker: " + file);
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
		File test;
		if ((test = new File(path, "bin/prism")).exists()) {
			if (test.canExecute()) {
				log.log(Level.INFO, "Found possible Prism Model Checker: " + path);
				return true;
			}
		}
		return false;
	}

}

/** Search for Prism model checker on Linux **/
class LinuxSearch extends PrismSearch {

	/** search for Prism on Linux **/
	public LinuxSearch() {
		// Some pre defined Paths
		File[] pathsArray = { new File("prism/bin/prism"), new File("bin/prism"), new File("../bin/prism") };
		List<File> paths = new LinkedList<File>(Arrays.asList(pathsArray));

		// Merge, append to prismPaths!
		prismPaths.addAll(paths);
	}
}

/** Search for Prism model checker on Windows OS **/
class WindowsSearch extends PrismSearch {

	/** suggest additional folders to search for prism on Windows OS **/
	public WindowsSearch() {
		// Search on C:\program files, C:\program files(x86)...
		prismPaths.addAll(Arrays.asList(new File(System.getenv("ProgramFiles")).listFiles(filter)));
		prismPaths.addAll(Arrays.asList(new File(System.getenv("%programfiles%" + " " + "(x86)")).listFiles(filter)));
		prismPaths.addAll(Arrays.asList(new File("c:\\Program Files\\").listFiles(filter)));
	}

	public boolean checkPrismPath(File path) {
		File test;
		if ((test = new File(path, "bin\\prism")).exists()) {
			log.log(Level.INFO, "Found possible Prism Model Checker: " + path);
			if (test.canExecute())
				return true;
		}
		return false;
	}
}

class MacOSSearch extends PrismSearch {

	public MacOSSearch() {
		// Some pre defined Paths
		File[] pathsArray = { new File("prism/bin/prism"), new File("bin/prism"), new File("../bin/prism") };
		List<File> paths = new LinkedList<File>(Arrays.asList(pathsArray));

		// Merge, append to prismPaths!
		prismPaths.addAll(paths);
	}

}