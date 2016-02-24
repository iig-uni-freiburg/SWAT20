package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import de.invation.code.toval.file.FileUtils;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class ExportAction extends AbstractWorkbenchAction {

	int commandKey = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
	
	public ExportAction(String name) {
		super(name);
		setAcceleratorKey(KeyStroke.getKeyStroke('E', commandKey));
	}

	private static final long serialVersionUID = -6290002178847609267L;

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		// Export current active tab
		Object current = SwatTabView.getInstance().getSelectedComponent();
		File file = null;
		if (current instanceof PNEditorComponent) {
                    //file = SwatComponents.getInstance().getContainerPetriNets().getComponent(((PNEditorComponent) current).getNetContainer().getPetriNet().getName());
                    file = new File (SwatComponents.getInstance().getContainerPetriNets().getBasePath(),"((PNEditorComponent) current).getNetContainer().getPetriNet().getName()"+".pnml");
			//file = SwatComponents.getInstance().getPetriNetFile(((PNEditorComponent) current).getNetContainer().getPetriNet().getName());
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
					Workbench.errorMessage("Could not copy " + file + " to " + dialog.getSelectedFile(), e1, true);
					e1.printStackTrace();
				}
			}
		}

	}

}
