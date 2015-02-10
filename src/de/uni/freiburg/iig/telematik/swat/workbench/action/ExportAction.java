package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

import de.invation.code.toval.file.FileUtils;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;

public class ExportAction extends AbstractWorkbenchAction {

	public ExportAction(String name) {
		super(name);
	}

	private static final long serialVersionUID = -6290002178847609267L;

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		// Export current active tab
		Object current = SwatTabView.getInstance().getSelectedComponent();
		File file = null;
		if (current instanceof PNEditor) {
			file = SwatComponents.getInstance().getPetriNetFile(((PNEditor) current).getNetContainer().getPetriNet().getName());
		} else if (current instanceof LogModel) {
			file = ((LogModel) current).getFileReference();
		}

		if (file != null) {
			JFileChooser dialog = new JFileChooser();
			int returnValue = dialog.showSaveDialog(Workbench.getInstance());
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				try {
					FileUtils.copy(file, dialog.getSelectedFile());
				} catch (Exception e1) {
					Workbench.errorMessageWithNotification("Could not copy " + file + " to " + dialog.getSelectedFile() + ": " + e1.getMessage());
					e1.printStackTrace();
				}
			}
		}

	}

}
