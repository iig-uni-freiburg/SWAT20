package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.invation.code.toval.graphic.dialog.FileNameDialog;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.lola.XMLFileViewer;
import de.uni.freiburg.iig.telematik.swat.sciff.presenter.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponent;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class RenameAction extends AbstractAction {

	private SwatTabView tabView;
	private SwatTreeView treeView;

	public RenameAction(SwatTabView tabView, SwatTreeView treeView) {
		this.tabView = tabView;
		this.treeView = treeView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		SwatComponent currentComponent = (SwatComponent) tabView.getSelectedComponent();
		SwatTreeNode node = (SwatTreeNode) Workbench.getInstance().getTreeView().getSelectionPath().getLastPathComponent();
		//take current file, rename and update
		try {
			File oldFile = node.getFileReference();
			String extensions[] = oldFile.toString().split("\\.");
			String extension = "." + extensions[extensions.length - 1];
			File newFile = getAbsolutePathToWorkingDir(requestFileName("New name for net?", "New name") + extension, currentComponent);
			if (oldFile.renameTo(newFile) ) {
				tabView.remove(node);
				SwatComponents.getInstance().remove(oldFile);
				SwatComponents.getInstance().reload();
				Workbench.consoleMessage("Rename of " + oldFile.getName() + " to " + newFile.getName() + " Successfull");
			}

			else { //Something went wrong
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(tabView.getParent()), "Rename not possible", "Problem",
						JOptionPane.ERROR_MESSAGE);
				Workbench.consoleMessage("Rename of " + oldFile.getName() + " to " + newFile.getName() + " NOT Successfull");
				return;
			}

		} catch (ParameterException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		} catch (PropertyException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		//			PNEditor editor = (PNEditor) ((SwatComponent)tabView.getSelectedComponent()).getMainComponent();
		//			AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> net = editor.getNetContainer();
		//			File currentFile = SwatComponents.getInstance().getFile(net);


	}

	private String requestFileName(String message, String title) {
		return new FileNameDialog(SwingUtilities.getWindowAncestor(treeView.getParent()), message, title, false).requestInput();
	}

	private File getAbsolutePathToWorkingDir(String name, SwatComponent currentComponent) throws PropertyException, ParameterException,
			IOException {
		String extension="";
		if (currentComponent instanceof PNEditor && !name.endsWith(".pnml"))
			extension = ".pnml";
		if (currentComponent instanceof XMLFileViewer && !name.endsWith(".xml"))
			extension = ".xml";
		if (currentComponent instanceof LogFileViewer && !name.endsWith(".xml"))
			extension = ".mxml";
		File file = new File(SwatProperties.getInstance().getWorkingDirectory(), name + extension);
		if (file.exists())
			throw new ParameterException("File already exists");
		//TODO: Validate, test if SWATComponent already contains net with same name... etc?
		return file;
	}

}
