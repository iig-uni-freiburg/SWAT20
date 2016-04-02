package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.KeyStroke;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.WorkingDirectoryDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class ManageWorkingDirectoryAction extends AbstractWorkbenchAction {

	int commandKey = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
	
	private static final long serialVersionUID = 4540373111307405160L;

	public ManageWorkingDirectoryAction() {
		super("");
		setTooltip("Manage Working Directories");
		try {
			setIcon(IconFactory.getIcon("switch_directory"));
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ManageWorkingDirectoryAction(Icon icon) {
		super("Manage Working Directories", icon);
		setAcceleratorKey(KeyStroke.getKeyStroke('W', commandKey));
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
