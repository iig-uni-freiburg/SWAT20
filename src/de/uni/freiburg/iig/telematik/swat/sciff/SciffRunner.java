package de.uni.freiburg.iig.telematik.swat.sciff;

import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.processmining.analysis.sciffchecker.SCIFFChecker;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponent;

public class SciffRunner {

	private LogFileViewer logFile;

	public static void main(String[] args) {
		System.out.println("Run");
		SCIFFChecker test = new SCIFFChecker();
		AnalysisInputItem item = new AnalysisInputItem("Test");
		//test.analyse(item);
	}

	public SciffRunner(SwatComponent log) throws ParameterException {
		if (log instanceof LogFileViewer) {
			this.logFile = (LogFileViewer) logFile;
		} else {
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(logFile.getMainComponent()),
					"SCIFF Checker can only analyze Log-Files", "Unsupported Type", JOptionPane.ERROR_MESSAGE);
			throw new ParameterException("Unsopported Type");
		}
	}

	public void analyse(String rules) {
		//TODO: Implement
		SCIFFChecker test = new SCIFFChecker();

		//test.analyse(inputs);
	}

	public void analyse(File rules) {
		//TODO: Implement
	}

	public void configure() {
		//TODO: Implement
	}

}
