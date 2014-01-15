package de.uni.freiburg.iig.telematik.swat.workbench;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import de.invation.code.toval.file.FileUtils;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNSerializationFormat;
import de.uni.freiburg.iig.telematik.swat.lola.XMLFileViewer;
import de.uni.freiburg.iig.telematik.swat.sciff.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;
public class SwatComponents {
	
	private static SwatComponents instance = null;
	
	@SuppressWarnings("rawtypes")
	private Map<AbstractGraphicalPN, File> nets = new HashMap<AbstractGraphicalPN, File>();
	private Map<LogFileViewer, File> logs = new HashMap<LogFileViewer, File>();
	private Map<XMLFileViewer, File> xml = new HashMap<XMLFileViewer, File>();
	
	private SwatComponents(){
		try {
			loadSwatComponents();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Cannot access working directory:\n" + e.getMessage(), "Internal Exception", JOptionPane.ERROR_MESSAGE);
			
		}
	}
	
	public void reload() throws ParameterException {
		nets.clear();
		logs.clear();
		xml.clear();
		loadSwatComponents();
	}

	public static SwatComponents getInstance(){
		if(instance == null){
			instance = new SwatComponents();
		}
		return instance;
	}
	
	public void putIntoSwatComponent(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> net, File file) {
		nets.put(net, file);
	}

	private void loadSwatComponents() throws ParameterException {
		// 1. Load Petri nets
		MessageDialog.getInstance().addMessage("1. Searching for Petri nets:");
		List<File> pnmlFiles = null;
		try {
			pnmlFiles = FileUtils.getFilesInDirectory(SwatProperties.getInstance().getWorkingDirectory(), true, true, "pnml");
		} catch (Exception e) {
			throw new ParameterException("Cannot access working directory.\nReason: "+e.getMessage());
		} 
		for (File netFile : pnmlFiles) {
			MessageDialog.getInstance().addMessage("Loading Petri net: " + FileUtils.getName(netFile) + "...   ");
			try {
				AbstractGraphicalPN<?,?,?,?,?,?,?> loadedNet = null;
				loadedNet = new PNMLParser().parse(netFile, SwatProperties.getInstance().getRequestNetType(), SwatProperties.getInstance().getPNValidation());
				nets.put(loadedNet, netFile);
				MessageDialog.getInstance().addMessage("Done.");
			} catch (Exception e) {
				MessageDialog.getInstance().addMessage("Error: " + e.getMessage());
				e.printStackTrace();
			}
		}
		MessageDialog.getInstance().newLine();

		// 2. Load logfiles
		MessageDialog.getInstance().addMessage("2. Searching for mxml log files:");
		List<File> mxmlFiles = null;
		try {
			mxmlFiles = FileUtils.getFilesInDirectory(SwatProperties.getInstance().getWorkingDirectory(), true, true, "mxml");
		} catch (Exception e) {
			throw new ParameterException("Cannot access working directory.\nReason: " + e.getMessage());
		}
		for (File logFile : mxmlFiles) {
			try {
				logs.put(new LogFileViewer(logFile), logFile);
				MessageDialog.getInstance().addMessage("Done.");
			} catch (IOException e) {
				MessageDialog.getInstance().addMessage("Error: " + e.getMessage());
				e.printStackTrace();
			}
		}

		// 3. Load xml files for Lola
		MessageDialog.getInstance().addMessage("2. Searching for xml files:");
		List<File> xmlFiles = null;
		try {
			xmlFiles = FileUtils.getFilesInDirectory(SwatProperties.getInstance().getWorkingDirectory(), true, true, "xml");
		} catch (Exception e) {
			throw new ParameterException("Cannot access working directory.\nReason: " + e.getMessage());
		}
		for (File xmlFile : xmlFiles) {
			try {
				xml.put(new XMLFileViewer(xmlFile), xmlFile);
				MessageDialog.getInstance().addMessage("Done.");
			} catch (IOException e) {
				MessageDialog.getInstance().addMessage("Error: " + e.getMessage());
				e.printStackTrace();
			}
		}

	}
	
	
	@SuppressWarnings("rawtypes")
	public Set<AbstractGraphicalPN> getPetriNets(){
		return nets.keySet();
	}
	
	public Set<LogFileViewer> getLogFiles() {
		return logs.keySet();
	}

	public Set<XMLFileViewer> getXMLFiles() {
		return xml.keySet();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void storePetriNet(AbstractGraphicalPN net) throws ParameterException {
		try {
			PNSerialization.serialize(net, PNSerializationFormat.PNML, nets.get(net).getCanonicalPath());
		} catch (Exception e) {
			throw new ParameterException("Cannot store Petri net.\nReason: "+e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	public String getFileName(AbstractGraphicalPN net){
		return FileUtils.getName(getFile(net));
	}
	
	@SuppressWarnings("rawtypes")
	public File getFile(AbstractGraphicalPN net){
		return nets.get(net);
	}
	
	public boolean containsNetWithID(String name){
		for (AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> pn : getPetriNets()) {
			if (name.equals(pn.getPetriNet().getName()))
				return true;
		}
		return false;
	}
	
	public boolean containsNetWithFileName(String name){
		return nets.containsValue(new File(name));
	}

	public AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> getNetFromFileName(String name) {
		// TODO: unimplemented
		System.out.println("UNIMPLEMENTED: getNetFromFileName");
		return null;
	}

	/** Stores every {@link AbstractGraphicalPN} within the project */
	public void storeAllPetriNets() throws ParameterException {
		for (AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> pn : getPetriNets()) {
			try {
				storePetriNet(pn);
			} catch (ParameterException e) {
				try {
					throw new ParameterException("Cannot store Petri net " + nets.get(pn).getCanonicalPath() + "\nReason: "
							+ e.getMessage());
				} catch (IOException e1) {
					MessageDialog.getInstance().addMessage("Could not get (canonical) filepath for: " + pn.getPetriNet().getName());
				}
			}
		}

	}

}
