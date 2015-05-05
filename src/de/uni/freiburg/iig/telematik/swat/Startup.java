package de.uni.freiburg.iig.telematik.swat;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.invation.code.toval.graphic.misc.AbstractStartup;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.WorkingDirectoryDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class Startup extends AbstractStartup {

	private static final String TOOL_NAME = "SWAT";

	@Override
	protected String getToolName() {
		return TOOL_NAME;
	}

	@Override
	protected void startApplication() throws Exception {
		// Check if there is a path to a simulation directory.
		if (!checkWorkingDirectory()) {
			// There is no path and it is either not possible to set a path or the user aborted the corresponding dialog.
			return;
		}
		MessageDialog.getInstance();
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					SwatComponents.getInstance();
				}
			});
		} catch (Exception e) {
			throw new Exception("Exception during startup:<br>Reason: " + e.getMessage(), e);
		}
		// new Workbench();
		Workbench.getInstance();
	}
	
	private boolean checkWorkingDirectory() {
		try {
			SwatProperties.getInstance().getWorkingDirectory();
			return true;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Internal exception: Cannot load/create general property file:\n" + e.getMessage(), "Internal Exception", JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (PropertyException e) {
			// There is no recent simulation directory
			// -> Let the user choose a path for the simulation directory
			return chooseWorkingDirectory();
		} catch (ParameterException e) {
			// Value for simulation directory is invalid, possibly due to moved directories
			// -> Remove entry for actual simulation directory
			try {
				SwatProperties.getInstance().removeWorkingDirectory();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "Internal exception: Cannot fix corrupt property entries:\n" + e1.getMessage(), "Internal Exception", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			// -> Let the user choose a path for the simulation directory
			return chooseWorkingDirectory();
		}
	}

	private boolean chooseWorkingDirectory(){
		String workingDirectory = null;
		try {
			workingDirectory = WorkingDirectoryDialog.showDialog(null);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "<html>Cannot start working directory dialog.<br>Reason: "+e.getMessage()+"</html>", "Internal Exception", JOptionPane.ERROR_MESSAGE);
		}
		if(workingDirectory == null)
			return false;
		try {
			SwatProperties prop = SwatProperties.getInstance();
			prop.setWorkingDirectory(workingDirectory, false);
			return true;
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "I/O Exception", JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (SwatComponentException e2) {
			JOptionPane.showMessageDialog(null, e2.getMessage(), "Component Exception", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	public static void main(String[] args){
		new Startup();
	}

}
