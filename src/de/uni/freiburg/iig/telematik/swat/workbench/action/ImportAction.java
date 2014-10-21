package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.SwingUtilities;

import de.invation.code.toval.graphic.dialog.FileNameDialog;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.parser.graphic.PNParserDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class ImportAction extends AbstractWorkbenchAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8945460474139815880L;
	private SwatTreeView treeView;

	//	public ImportAction(SwatTreeView treeView) {
	//		this.treeView = treeView;
	//	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> net = PNParserDialog
.showPetriNetDialog(SwingUtilities.getWindowAncestor(treeView
				.getParent()));
		if (net == null)
			return;
		String name = requestFileName("Name for imported net?", "New name?");
		try {
			SwatComponents.getInstance().putNetIntoSwatComponent(net, name);
			//Put File into Swat Workspace and save
			//			File file = getAbsolutePathToWorkingDir(fileName);
			//			SwatComponents.getInstance().putIntoSwatComponent(net, file);
			//			SwatComponents.getInstance().storePetriNet(net);
			//			treeView.removeAndUpdateSwatComponents();
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

	private static void copyFileUsingStream(File source, File dest) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
		}
	}
}