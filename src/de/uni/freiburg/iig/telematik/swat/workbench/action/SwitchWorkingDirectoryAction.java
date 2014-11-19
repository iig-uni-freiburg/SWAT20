package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.WorkingDirectoryDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class SwitchWorkingDirectoryAction extends AbstractWorkbenchAction {

	private static final long serialVersionUID = 4540373111307405160L;

	public SwitchWorkingDirectoryAction() {
		super("Switch Working Directory");
	}

	public SwitchWorkingDirectoryAction(Icon icon) {
		super("Switch Working Directory", icon);
	}

	@Override
	public void doFancyStuff(ActionEvent e) throws Exception {
		String workingDirectory = WorkingDirectoryDialog.showDialog(Workbench.getInstance());
		if(workingDirectory == null)
			return;

		if(!SwatProperties.getInstance().getWorkingDirectory().equals(workingDirectory)){
			SwatProperties.getInstance().setWorkingDirectory(workingDirectory, true);
		}
	}

}
