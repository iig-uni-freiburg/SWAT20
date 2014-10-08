package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.WorkingDirectoryDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class SwitchWorkingDirectoryAction extends AbstractAction {

	private SwatTreeView treeView;
	private SwatTabView tabView;

	public SwitchWorkingDirectoryAction(SwatTreeView treeView, SwatTabView tabView) {
		this.treeView = treeView;
		this.tabView = tabView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		WorkingDirectoryDialog dialog = new WorkingDirectoryDialog(SwingUtilities.getWindowAncestor(treeView));
		String workingDirectory = dialog.getSimulationDirectory();
		if(workingDirectory == null)
			return;
		
		try {
			// Update Properties and reload SwatComponents.
			SwatProperties.getInstance().setWorkingDirectory(workingDirectory, true);
			SwatProperties.getInstance().addKnownWorkingDirectory(workingDirectory, true);
			SwatProperties.getInstance().store();
			SwatComponents.getInstance().reload();
			// Inform TabView, etc...
			tabView.removeAll();
			treeView.removeAndUpdateSwatComponents();
		} catch (ParameterException e2) {
			JOptionPane.showMessageDialog(null, e2.getMessage(), "Parameter Exception", JOptionPane.ERROR_MESSAGE);
			e2.printStackTrace();
		} catch (IOException e3) {
			JOptionPane.showMessageDialog(null, e3.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
			e3.printStackTrace();
		} catch (SwatComponentException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "SWAT Component Exception", JOptionPane.ERROR_MESSAGE);

			e1.printStackTrace();
		}

	}

}
