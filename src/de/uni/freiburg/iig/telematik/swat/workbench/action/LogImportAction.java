package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.logs.SwatLogType;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;

public class LogImportAction extends AbstractWorkbenchAction {
	
	static JFileChooser chooser;

	public LogImportAction() {
		this("Import Log");
	}

	public LogImportAction(String name) {
		super(name);
		setTooltip("Import LogFile");
		try {
			setIcon(IconFactory.getIcon("import"));
		} catch (ParameterException e) {
		} catch (PropertyException e) {
		} catch (IOException e) {
		}
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		File f = getFile();
		if (f == null)
			return;
		SwatLogType type = getType(f);
		if (type == null) {
			JOptionPane.showMessageDialog(Workbench.getInstance(), "Log of unknown format");
			return;
		}
		//LogModel model = new LogModel(f, type);
		String logName = f.getName().replaceFirst("[.][^.]+$", "");
                switch(type){
                    case Aristaflow:
                        SwatComponents.getInstance().getContainerAristaflowLogs().addComponent(f);
                        SwatComponents.getInstance().getContainerAristaflowLogs().storeComponents();
                        break;
                    case MXML:
                        SwatComponents.getInstance().getContainerMXMLLogs().addComponent(f);
                        SwatComponents.getInstance().getContainerMXMLLogs().storeComponents();
                        break;
                    case XES:
                    default:
                        SwatComponents.getInstance().getContainerXESLogs().addComponent(f);
                        SwatComponents.getInstance().getContainerXESLogs().storeComponents();
                        break;
                }
		Workbench.consoleMessage("Imported " + logName);
	}

	protected File getFile() throws Exception {
		File f = null;
		if(chooser == null){
			chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("mxml, xes, csv", "mxml", "xes", "csv");
			chooser.setFileFilter(filter);
			chooser.setAcceptAllFileFilterUsed(false);
		}
		int returnVal = chooser.showOpenDialog(Workbench.getInstance());
		if(returnVal == JFileChooser.APPROVE_OPTION){
			f = chooser.getSelectedFile();
		}
		return f;
	}

	protected SwatLogType getType(File f) {
		String fileName = f.getName().toLowerCase();
		if (fileName.endsWith(".csv"))
			return SwatLogType.Aristaflow;
		if (fileName.endsWith(".mxml"))
			return SwatLogType.MXML;
		if (fileName.endsWith(".xes"))
			return SwatLogType.XES;
		else
			return null;
	}

}
