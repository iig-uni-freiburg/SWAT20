package de.uni.freiburg.iig.telematik.swat.workbench;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.WorkingDirectoryDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class Startup {
	
	public static void main(String[] args) {
		String osType = System.getProperty("os.name");
		if(osType.equals("Mac OS") || osType.equals("Mac OS X")){
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Swat");
			System.setProperty("com.apple.macos.useScreenMenuBar", "true");
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Swat");
		}
		
		//Check if there is a path to a simulation directory.
		if (!checkWorkingDirectory()) {
			// There is no path and it is either not possible to set a path or the user aborted the corresponding dialog.
			System.exit(0);
		}
				
		MessageDialog.getInstance();
		try {
			SwingUtilities.invokeAndWait(new Runnable(){

				@Override
				public void run() {
					SwatComponents.getInstance();
				}
				
			});
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Cannot launch Swat", "Exception during startup:<br>Reason: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
		}
		//new Workbench();
		Workbench.getInstance();
	}
	
	private static boolean checkWorkingDirectory(){
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
	
	private static boolean chooseWorkingDirectory(){
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
			prop.setWorkingDirectory(workingDirectory);
			//add needed folders
			new File(prop.getLogWorkingDirectory()).mkdir();
			new File(prop.getNetWorkingDirectory()).mkdir();
			new File(prop.getAcModelWorkingDirectory()).mkdir();

			return true;
		} catch (ParameterException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Invalid Parameter", JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "I/O Exception", JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (PropertyException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Property Exception", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

}
