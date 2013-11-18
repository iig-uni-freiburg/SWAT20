package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatMenuBar;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.WorkingDirectoryDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class SwitchWorkingDirectoryAction extends AbstractAction {
	// TODO:
	// ------ maybe not needed and replaceable by
	// OpenWoorkingDirectoryAction--------
	public static void main(String args[]) {
		SwitchWorkingDirectoryAction swda = new SwitchWorkingDirectoryAction();
		swda.actionPerformed(new ActionEvent(new SwatMenuBar(), 0, DEFAULT));
	}

	private static final long serialVersionUID = 7231652730616663333L;

	@Override
	public void actionPerformed(ActionEvent e) {
		// Show Dialog to choose path
		String workingDirectory = WorkingDirectoryDialog.showDialog(SwingUtilities.getWindowAncestor((Component) e.getSource()));
		try {
			SwatProperties.getInstance().setWorkingDirectory(workingDirectory);
		} catch (ParameterException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Invalid Parameter", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "I/O Exception", JOptionPane.ERROR_MESSAGE);
		} catch (PropertyException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Property Exception", JOptionPane.ERROR_MESSAGE);
		}

		// TODO Auto-generated method stub
	}

}
