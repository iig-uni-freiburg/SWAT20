package de.unifreiburg.iig.bpworkbench2.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.unifreiburg.iig.bpworkbench2.gui.SplitGui;
import de.unifreiburg.iig.bpworkbench2.logging.BPLog;

public class PrismRunner {
	private static Logger log = BPLog.getLogger(SplitGui.class.getName());
	private File prismPath;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		PrismSearch prismsearch = PrismSearch.getInstance();
		File prismPath = prismsearch.getPrismPath();
		System.out.println("_____Path to Prism: " + prismPath);
		File[] files = prismsearch.getPossiblePaths();
		for (File file : files) {
			System.out.println(file.toString());
		}

	}

	public void execPrism(String... param) {

	}

	public PrismRunner() {
		prismPath = PrismSearch.getInstance().getPrismPath();
	}

	public String execPrism(String modelPath, String... propertyPath) {
		// prepare property String
		String propertyString = propertyString(propertyPath);

		try {
			// Start Prism
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec(prismPath.toString() + " " + modelPath + propertyString);
			// Read in- and outpustream
			InputStream in = p.getInputStream();
			OutputStream out = p.getOutputStream();
			InputStream err = p.getErrorStream();

			// get result
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			p.destroy();
			String buffer = "";
			StringBuilder stringBuilder = new StringBuilder();
			while ((buffer = br.readLine()) != null) {
				stringBuilder.append(buffer);
			}
			String result = stringBuilder.toString();
			return result;

		} catch (Exception exc) {/* handle exception */
			log.log(Level.SEVERE, "Could not start PRISM Model Checker");
			return null;
		}

	}

	private String propertyString(String[] propertyPath) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < propertyPath.length; i++) {
			sb.append(" ");
			sb.append(propertyPath[i]);
		}
		return sb.toString();
	}
}

class PrismSearch {
	private static Logger log = BPLog.getLogger(SplitGui.class.getName());
	private static String OS = System.getProperty("os.name").toLowerCase();
	protected File prismPaths[];

	public static PrismSearch getInstance() {

		if (isWindows())
			return (PrismSearch) new WindowsSearch();
		else if (isMac())
			return (PrismSearch) new MacOSSearch();
		else if (isUnix() || isSolaris())
			return (PrismSearch) new LinuxSearch();
		else
			return null;
	}

	public File[] getPossiblePaths() {
		return prismPaths;
	}

	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);

	}

	public static boolean isSolaris() {

		return (OS.indexOf("sunos") >= 0);

	}

	public File getPrismPath() {
		for (int i = 0; i < prismPaths.length; i++) {
			if (new File(prismPaths[i], "bin/prism").exists()) {
				log.log(Level.INFO, "Found Prism Model Checker: " + prismPaths[i]);
				return new File(prismPaths[i], "bin/prism");
			}
		}
		return null;
	}

}

class LinuxSearch extends PrismSearch {
	String userHome = System.getProperty("user.home");
	FilenameFilter filter = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			return name.toLowerCase().contains("prism");
		}
	};

	public LinuxSearch() {
		// search for Prism on Linux
		File[] paths = { new File("prism/bin/prism"), new File("bin/prism"), new File("../bin/prism"), new File("/bin/prism") };
		// get Filelist from pattern
		File[] patterns = new File(userHome + "/bin/").listFiles(filter);

		prismPaths = new File[paths.length + patterns.length];
		System.arraycopy(paths, 0, prismPaths, 0, paths.length);
		System.arraycopy(patterns, 0, prismPaths, paths.length, patterns.length);
		System.out.println("Linux");

	}
}

class WindowsSearch extends PrismSearch {

}

class MacOSSearch extends PrismSearch {

}