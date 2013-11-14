package de.uni.freiburg.iig.telematik.swat.workbench;

import java.io.File;
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
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class SwatComponents {
	
	private static SwatComponents instance = null;
	
	@SuppressWarnings("rawtypes")
	private Map<AbstractGraphicalPN, File> nets = new HashMap<AbstractGraphicalPN, File>();
	
	private SwatComponents(){
		try {
			loadSwatComponents();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Cannot access working directory:\n" + e.getMessage(), "Internal Exception", JOptionPane.ERROR_MESSAGE);
			
		}
	}
	
	public static SwatComponents getInstance(){
		if(instance == null){
			instance = new SwatComponents();
		}
		return instance;
	}
	
	private void loadSwatComponents() throws ParameterException {
		// 3. Load Petri nets
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
	}
	
	
	@SuppressWarnings("rawtypes")
	public Set<AbstractGraphicalPN> getPetriNets(){
		return nets.keySet();
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
		//TODO:
		return false;
	}
	
	public boolean containsNetWithFileName(String name){
		//TODO
		return false;
	}

}
