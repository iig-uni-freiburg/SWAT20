package de.uni.freiburg.iig.telematik.swat.workbench;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.invation.code.toval.file.FileUtils;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNSerializationFormat;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.lola.XMLFileViewer;
import de.uni.freiburg.iig.telematik.swat.sciff.presenter.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatComponentListener;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;
public class SwatComponents {
	
	private static SwatComponents instance = null;
	
	private Set<SwatComponentListener> listener = new HashSet<SwatComponentListener>();

	@SuppressWarnings("rawtypes")
	private Map<AbstractGraphicalPN, File> nets = new HashMap<AbstractGraphicalPN, File>();
	private Map<LogFileViewer, File> logs = new HashMap<LogFileViewer, File>();
	private Map<XMLFileViewer, File> xml = new LinkedHashMap<XMLFileViewer, File>();
	

	private SwatComponents(){
		try {
			loadSwatComponents();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Cannot access working directory:<br>" + e.getMessage(), "Internal Exception", JOptionPane.ERROR_MESSAGE);
			
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
	
	public void remove(File file) {
		HashMap<Object, File> all = new HashMap<Object, File>();
		all.putAll(xml);
		all.putAll(logs);
		all.putAll(nets);
		for (Entry<Object, File> entry : all.entrySet()) {
			if (entry.getValue().equals(file))
			remove(entry.getKey());
		}
	}

	//public void add

	private void remove(Object key) {
		boolean found=false;
		if (xml.containsKey(key)) {
			xml.remove(key);
			found=true;
		}

		if (logs.containsKey(key)) {
			logs.remove(key);
			found=true;
		}
		if (nets.containsKey(key)) {
			nets.remove(key);
			found=true;
		}
		
		if(found) informElementRemoved(key);
	}

	public void putIntoSwatComponent(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> net, File file) {
		nets.put(net, file);
	}


	private void loadSwatComponents() throws ParameterException {
		// 1. Load Petri nets
		MessageDialog.getInstance().addMessage("1. Searching for Petri nets:");
		List<File> pnmlFiles = null;
		try {
			pnmlFiles = FileUtils.getFilesInDirectory(SwatProperties.getInstance().getWorkingDirectory(), true, true, "pnml");
		} catch (Exception e) {
			throw new ParameterException("Cannot access working directory.<br>Reason: "+e.getMessage());
		} 
		for (File netFile : pnmlFiles) {
			MessageDialog.getInstance().addMessage("Loading Petri net: " + FileUtils.getName(netFile) + "...   ");
			try {
				MessageDialog.getInstance().addMessage(netFile.getCanonicalPath());
				AbstractGraphicalPN<?,?,?,?,?,?,?, ?, ?> loadedNet = null;
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
		MessageDialog.getInstance().addMessage("2. Searching for log files:");

		//First mxml
		List<File> mxmlFiles = null;
		try {
			mxmlFiles = FileUtils.getFilesInDirectory(SwatProperties.getInstance().getWorkingDirectory(), true, true, "mxml");
		} catch (Exception e) {
			throw new ParameterException("Cannot access working directory.<br>Reason: " + e.getMessage());
		}
		for (File logFile : mxmlFiles) {
			try {
				MessageDialog.getInstance().addMessage("Loading log file: " + FileUtils.getName(logFile) + "...   ");
				logs.put(new LogFileViewer(logFile), logFile);
				MessageDialog.getInstance().addMessage("Done.");
			} catch (IOException e) {
				MessageDialog.getInstance().addMessage("Error: " + e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//Second csv-Files
		List<File> csvFiles = null;
		List<File> analysisFile = null;
		try {
			csvFiles = FileUtils.getFilesInDirectory(SwatProperties.getInstance().getWorkingDirectory(), true, true, "csv");
			analysisFile = FileUtils.getFilesInDirectory(SwatProperties.getInstance().getWorkingDirectory(), true, true, "analysis");
		} catch (Exception e) {
			throw new ParameterException("Cannot access working directory.<br>Reason: " + e.getMessage());
		}
		for (File logFile : csvFiles) {
			try {
				MessageDialog.getInstance().addMessage("Loading log file: " + FileUtils.getName(logFile) + "...   ");
				logs.put(new LogFileViewer(logFile), logFile);
				MessageDialog.getInstance().addMessage("Done.");
			} catch (IOException e) {
				MessageDialog.getInstance().addMessage("Error: " + e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		// 3. Load xml files for Lola
		MessageDialog.getInstance().addMessage("2. Searching for xml files:");
		List<File> xmlFiles = null;
		try {
			xmlFiles = FileUtils.getFilesInDirectory(SwatProperties.getInstance().getWorkingDirectory(), true, true, "xml");
		} catch (Exception e) {
			throw new ParameterException("Cannot access working directory.<br>Reason: " + e.getMessage());
		}
		for (File xmlFile : xmlFiles) {
			try {
				MessageDialog.getInstance().addMessage("Loading xml file: " + FileUtils.getName(xmlFile) + "...   ");
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
		TreeMap<AbstractGraphicalPN, File> sort = new TreeMap<AbstractGraphicalPN, File>(new SwatComperator(nets));
		sort.putAll(nets);//here: sort done through TreeMap
		return sort.keySet();
	}
	
	public Set<LogFileViewer> getLogFiles() {
		TreeMap<LogFileViewer, File> sort = new TreeMap<LogFileViewer, File>(new SwatComperator());
		sort.putAll(logs);
		return sort.keySet();
	}

	public Set<XMLFileViewer> getXMLFiles() {
		TreeMap<XMLFileViewer, File> sort = new TreeMap<XMLFileViewer, File>(new SwatComperator());
		sort.putAll(xml);
		return sort.keySet();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void storePetriNet(AbstractGraphicalPN net) throws ParameterException {
		try {
			PNSerialization.serialize(net, PNSerializationFormat.PNML, nets.get(net).getCanonicalPath());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(SwatTreeView.getInstance()), "Could not save PT-net",
					"Could not save",
					JOptionPane.ERROR_MESSAGE);
			Workbench.errorMessage("Cannot store petri net: " + net.getPetriNet().getName());
			throw new ParameterException("Cannot store Petri net.<br>Reason: "+e.getMessage());
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
	
	@SuppressWarnings("rawtypes")
	public void changeFileforNet(AbstractGraphicalPN net, File newFile) {
		nets.put(net, newFile);
	}

	public boolean containsNetWithID(String name){
		for (AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> pn : getPetriNets()) {
			if (name.equals(pn.getPetriNet().getName()))
				return true;
		}
		return false;
	}
	
	public boolean containsNetWithFileName(String name){
		return nets.containsValue(new File(name));
	}

	public AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> getNetFromFileName(String name) {
		// TODO: unimplemented
		System.out.println("UNIMPLEMENTED: getNetFromFileName");
		return null;
	}

	/** Stores every {@link AbstractGraphicalPN} within the project */
	public void storeAllPetriNets() throws ParameterException {
		for (AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> pn : getPetriNets()) {
			try {
				storePetriNet(pn);
			} catch (ParameterException e) {
				try {
					Workbench.errorMessage("Cannot store petri net: " + pn.getPetriNet().getName());
					throw new ParameterException("Cannot store Petri net " + nets.get(pn).getCanonicalPath() + "\nReason: "
							+ e.getMessage());
				} catch (IOException e1) {
					Workbench.errorMessage("Cannot store petri net: " + pn.getPetriNet().getName() + " (Could not resolve canonical name)");
					MessageDialog.getInstance().addMessage("Could not get (canonical) filepath for: " + pn.getPetriNet().getName());
				}
			}
		}

	}

	/**
	 * get File that belongs to currentComponent
	 * 
	 * @throws ParameterException
	 *             if not found
	 **/
	public File getFile(SwatComponent currentComponent) throws ParameterException {
		// Traverse all lists
		File file;
		//nets
		if (currentComponent instanceof PNEditor) {
		for (AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> pnet : nets.keySet()) {
			if (pnet == ((PNEditor) currentComponent).getNetContainer())
				return nets.get(pnet);
		}}
		//xml
		for (XMLFileViewer xmlFile:xml.keySet()){
			if (xmlFile == currentComponent)
				return xml.get(xmlFile);
			}
		//mnl Logs
		for (LogFileViewer log : logs.keySet()) {
			if (log == currentComponent)
				return logs.get(log);
		}
		throw new ParameterException("not a valid SwatComponent");
	}

	public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
		List<T> list = new ArrayList<T>(c);
		java.util.Collections.sort(list);
		return list;
	}

	public void addSwatComponentListener(SwatComponentListener listener) {
		try {
			this.listener.add(listener);
		} catch (Exception e) {//listener may be null
		}
	}

	public void removeSwatComponentListener(SwatComponentListener listener) {
		this.listener.remove(listener);
	}

	private void informListenerOfModelChange() {
		for (SwatComponentListener listener : this.listener) {
			listener.modelChanged();
		}
	}

	private void informAnalysisAdded(SwatTreeNode node, Object AnalysisElement) {
		for (SwatComponentListener listener : this.listener) {
			listener.analysisAdded(node, AnalysisElement);
		}
	}

	private void informElementRemoved(Object elem) {
		for (SwatComponentListener listener : this.listener) {
			listener.elementRemoved(elem);
		}
	}

}

/** For use with TreeMap **/
class SwatComperator implements Comparator<Object> {
	// Note: this comparator imposes orderings that are inconsistent with equals. Only compares File Names   
	Map<AbstractGraphicalPN, File> base;

	/** because AbstractGraphicalPN does not carry its name:need mapping **/
	public SwatComperator(Map<AbstractGraphicalPN, File> base) {
		this.base = base;
	}

	public SwatComperator() {
		this.base = null;
	}

	public int compare(AbstractGraphicalPN a, AbstractGraphicalPN b) {
		return base.get(a).getName().compareTo(base.get(b).getName());
		} // returning 0 would merge keys

	public int compare(SwatComponent comp1, SwatComponent comp2) {
		return comp1.getName().compareTo(comp2.getName());
	}

	@Override
	public int compare(Object o1, Object o2) {
		if (o1 instanceof SwatComponent)
			return compare((SwatComponent) o1, (SwatComponent) o2);
		else
			return compare((AbstractGraphicalPN) o1, (AbstractGraphicalPN) o2);
	}
}
