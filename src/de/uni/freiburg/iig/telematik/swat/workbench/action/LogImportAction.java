package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.logs.SwatLog;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;

public class LogImportAction extends AbstractWorkbenchAction {

	public LogImportAction() {
		this("Import Log");
	}

	public LogImportAction(String name) {
		super(name);
		setTooltip("Import LogFile");
		try {
			setIcon(IconFactory.getIcon("import"));
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

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		File f = getFile();
		SwatLog type = getType(f);
		if (type == null) {
			JOptionPane.showMessageDialog(Workbench.getInstance(), "Log of unknown format");
			return;
		}
		LogModel model = new LogModel(f, type);
		String logName = f.getName().replaceFirst("[.][^.]+$", "");
		SwatComponents.getInstance().storeLogModelAs(model, logName);
		SwatComponents.getInstance().addLogModel(model);
	}

	protected File getFile() {
		File f = null;
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = chooser.showOpenDialog(Workbench.getInstance());
		if(returnVal == JFileChooser.APPROVE_OPTION){
			f = chooser.getSelectedFile();
		}
		return f;
	}

	protected SwatLog getType(File f) {
		String fileName = f.getName().toLowerCase();
		if (fileName.endsWith(".csv"))
			return SwatLog.Aristaflow;
		if (fileName.endsWith(".mxml"))
			return SwatLog.MXML;
		if (fileName.endsWith(".xes"))
			return SwatLog.XES;
		else
			return null;
	}

}