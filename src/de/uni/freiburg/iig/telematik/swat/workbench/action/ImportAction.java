package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;

import de.invation.code.toval.graphic.dialog.FileNameDialog;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.parser.graphic.PNParserDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class ImportAction implements ActionListener {

	private SwatTreeView treeView;

	public ImportAction(SwatTreeView treeView) {
		this.treeView = treeView;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> net = PNParserDialog
.showPetriNetDialog(SwingUtilities.getWindowAncestor(treeView
				.getParent()));
		if (net == null)
			return;
		String fileName = requestFileName("Name for imported net?", "New name?");
		try {
			File file = getAbsolutePathToWorkingDir(fileName);
			SwatComponents.getInstance().putIntoSwatComponent(net, file);
			treeView.removeAndUpdateSwatComponents();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String requestFileName(String message, String title) {
		return new FileNameDialog(SwingUtilities.getWindowAncestor(treeView.getParent()), message, title, false).requestInput();
	}

	private File getAbsolutePathToWorkingDir(String name) throws PropertyException, ParameterException, IOException {
		File file = new File(SwatProperties.getInstance().getWorkingDirectory(), name + ".pnml");
		if (file.exists())
			throw new ParameterException("File already exists");
		//TODO: Validate, test if SWATComponent already contains net with same name... etc?
		return file;
	}
}