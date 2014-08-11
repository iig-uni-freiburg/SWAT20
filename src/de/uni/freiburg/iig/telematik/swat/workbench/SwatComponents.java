package de.uni.freiburg.iig.telematik.swat.workbench;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.invation.code.toval.file.FileUtils;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNSerializationFormat;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.ACModel;
import de.uni.freiburg.iig.telematik.swat.bernhard.AnalysisStore;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.logs.LogAnalysisModel;
import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.logs.XMLFileViewer;
import de.uni.freiburg.iig.telematik.swat.misc.FileHelper;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatComponentsListener;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class SwatComponents {
	
	private static SwatComponents instance = null;
	
	private Set<SwatComponentsListener> listener = new HashSet<SwatComponentsListener>();

	@SuppressWarnings("rawtypes")
	private Map<String, ACModel> acModels = new HashMap<String, ACModel>();
	private Map<AbstractGraphicalPN, File> nets = new HashMap<AbstractGraphicalPN, File>();
	private Map<LogModel, File> logs = new HashMap<LogModel, File>();
	private Map<LogModel, File> xml = new LinkedHashMap<LogModel, File>();
	private Map<LogModel, List<LogAnalysisModel>> logAnalysis = new HashMap<LogModel, List<LogAnalysisModel>>();
	private List<String> needsLayout = new LinkedList<String>();

	private String selectedACModel;
	

	private SwatComponents() {
		try {
			loadSwatComponents();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Cannot access working directory:<br>" + e.getMessage(), "Internal Exception", JOptionPane.ERROR_MESSAGE);
			
		}
	}
	
	public static void main(String args[]) {
		SwatComponents.getInstance();
	}

	public void reload() throws ParameterException, PropertyException, IOException {
		nets.clear();
		logs.clear();
		xml.clear();
		loadSwatComponents();
		informListenerOfModelChange();
	}

	public static SwatComponents getInstance() {
		if(instance == null){
			instance = new SwatComponents();
		}
		return instance;
	}
	
	public void setLayoutNeed(AbstractGraphicalPN net) {
		needsLayout.add(net.getPetriNet().getName());
	}

	public boolean getLayoutNeed(AbstractGraphicalPN net) {
		return needsLayout.contains(net.getPetriNet().getName());
	}

	public void removeLayoutNeed(AbstractGraphicalPN net) {
		needsLayout.remove(net.getPetriNet().getName());
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

	/** TODO: store petrinet and put into SwatComponents **/
	private void putIntoSwatComponent(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> net, File file) {
		nets.put(net, file);
		informListenerOfModelChange();
	}

	/** TODO: store petrinet and put into SwatComponents **/
	public File putNetIntoSwatComponent(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> net, String name) throws ParameterException,
			PropertyException, IOException {
		File file = generateNetPath(name);
		nets.put(net, file);
		storePetriNet(net);
		informNodeAdded(SwatTreeView.getInstance().new SwatTreeNode(net, SwatComponentType.PETRI_NET, file));
		return file;
	}

	public File putCsvIntoSwatComponent(LogModel model, String name) throws ParameterException, PropertyException, IOException {
		File file = generateCsvLogPath(name);
		//Copy file
		FileHelper.copyFile(model.getFileReference(), file);
		LogModel newModel = new LogModel(file);
		logs.put(newModel, file);
		//informListenerOfModelChange();
		informNodeAdded(SwatTreeView.getInstance().new SwatTreeNode(newModel, SwatComponentType.LOG_FILE, file));
		return file;
	}

	private void informNodeAdded(SwatTreeNode swatTreeNode) {
		for (SwatComponentsListener listener : this.listener) {
			listener.nodeAdded(swatTreeNode);
		}

	}

	private File generateNetPath(String name) throws ParameterException, PropertyException, IOException {
		//Make Directory
		File folder = new File(SwatProperties.getInstance().getNetWorkingDirectory(), name);
		folder.mkdir();
		return new File(folder, name + ".pnml");
	}

	private File generateCsvLogPath(String name) throws ParameterException, PropertyException, IOException {
		//Make Directory
		File folder = new File(SwatProperties.getInstance().getLogWorkingDirectory(), name);
		folder.mkdir();
		return new File(folder, name + ".csv");
	}


	private void loadSwatComponents() throws ParameterException, PropertyException, IOException {
		// 1. Load Petri nets
		MessageDialog.getInstance().addMessage("1. Searching for Petri nets:");
		loadPetriNets();
		MessageDialog.getInstance().addMessage("Done.");
		MessageDialog.getInstance().newLine();

		// 2. Load logfiles
		MessageDialog.getInstance().addMessage("2. Searching for log files:");
		loadLogFiles();
		MessageDialog.getInstance().addMessage("Done.");
		MessageDialog.getInstance().newLine();








	}
	
	
	private void loadLogFiles() throws ParameterException, PropertyException, IOException {
		
		SwatProperties prop = SwatProperties.getInstance();
		File logFolder = new File(prop.getWorkingDirectory(), "logs");
		
		for (File folder : getAllFoldersFrom(logFolder)) {
			try {
				loadMxmlLogFromFolder(folder);
			} catch (NullPointerException e) {
				//folder does not exists. create
				new File(prop.getLogWorkingDirectory()).mkdir();
			}
		}

		for (File folder : getAllFoldersFrom(logFolder)) {
			try {
			loadCsvLogFromFolder(folder);
			} catch (NullPointerException e) {
			}
		}

		for (File folder : getAllFoldersFrom(logFolder)) {
			try {
			loadXmlLogFromFolder(folder);
			} catch (NullPointerException e) {
			}
		}
		
		
	}

	private void loadXmlLogFromFolder(File folder) {

		// 3. Load xml files for Lola
		//MessageDialog.getInstance().addMessage("2. Searching for xml files:");
		List<File> xmlFiles = null;
		try {
			xmlFiles = FileUtils.getFilesInDirectory(folder.getAbsolutePath(), true, true, "xml");
		} catch (Exception e) {
			throw new ParameterException("Cannot access working directory.<br>Reason: " + e.getMessage());
		}
		for (File xmlFile : xmlFiles) {
			MessageDialog.getInstance().addMessage("Loading xml file: " + FileUtils.getName(xmlFile) + "...   ");
			xml.put(new LogModel(xmlFile), xmlFile);
			MessageDialog.getInstance().addMessage("Done.");
		}

	}

	private void loadCsvLogFromFolder(File folder) {
		//Second csv-Files
		List<File> csvFiles = null;
		List<File> analysisFile = null;
		try {
			csvFiles = FileUtils.getFilesInDirectory(folder.getAbsolutePath(), true, true, "csv");
			analysisFile = FileUtils.getFilesInDirectory(folder.getAbsolutePath(), true, true, "analysis");
		} catch (Exception e) {
			throw new ParameterException("Cannot access working directory.<br>Reason: " + e.getMessage());
		}
		for (File logFile : csvFiles) {
			try {
				MessageDialog.getInstance().addMessage("Loading log file: " + FileUtils.getName(logFile) + "...   ");
				logs.put(new LogModel(logFile), logFile);
				MessageDialog.getInstance().addMessage("Done.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void loadMxmlLogFromFolder(File folder) {
		//First mxml
		List<File> mxmlFiles = null;
		try {
			mxmlFiles = FileUtils.getFilesInDirectory(folder.getAbsolutePath(), true, true, "mxml");
		} catch (Exception e) {
			throw new ParameterException("Cannot access working directory.<br>Reason: " + e.getMessage());
		}
		for (File logFile : mxmlFiles) {
			try {
				MessageDialog.getInstance().addMessage("Loading log file: " + FileUtils.getName(logFile) + "...   ");
				logs.put(new LogModel(logFile), logFile);
				MessageDialog.getInstance().addMessage("Done.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}

	/** load all petri nets and search for corresponding analysis **/
	private void loadPetriNets() throws ParameterException, PropertyException, IOException {

		//get all subfolders in $WorkingDir$/nets
		File netFolder = new File(SwatProperties.getInstance().getNetWorkingDirectory());
		for (File folder : getAllFoldersFrom(netFolder)) {
			try {
			AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> loadedNet = loadPetriNetFromFolder(folder);
			if (loadedNet != null)
				getAnalysisFor(loadedNet, folder);
			} catch (NullPointerException e) {
				//folder does not exists. create
				new File(SwatProperties.getInstance().getNetWorkingDirectory()).mkdir();
			}
		}

	}

	/** TODO: get all available Analysis on specified net **/
	private void getAnalysisFor(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> loadedNet, File folder) {
		List<File> analysisFiles = null;
		try {
			analysisFiles = FileUtils.getFilesInDirectory(folder.getAbsolutePath(), true, true, "analysis");
		} catch (Exception e) {
			throw new ParameterException("Cannot access working directory.<br>Reason: " + e.getMessage());
		}

	}

	/** loads the first found pnml net from folder **/
	private AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> loadPetriNetFromFolder(File folder) {
		List<File> pnmlFiles = null;
		try {
			pnmlFiles = FileUtils.getFilesInDirectory(folder.getAbsolutePath(), true, true, "pnml");
		} catch (Exception e) {
			throw new ParameterException("Cannot access working directory.<br>Reason: " + e.getMessage());
		}
		if (pnmlFiles.size() > 1)
			throw new ParameterException("There is more than one PetriNet in " + folder.toString());

		File netFile = pnmlFiles.get(0);
		MessageDialog.getInstance().addMessage("Loading Petri net: " + FileUtils.getName(netFile) + "...   ");

		try {
			MessageDialog.getInstance().addMessage(netFile.getCanonicalPath());
			AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> loadedNet = null;
			loadedNet = new PNMLParser().parse(netFile, SwatProperties.getInstance().getRequestNetType(), SwatProperties.getInstance()
					.getPNValidation());
			nets.put(loadedNet, netFile);
			return loadedNet;
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Error: " + e.getMessage());
			e.printStackTrace();
		}
		return null;

	}

	/** get all folders within basePath **/
	private List<File> getAllFoldersFrom(File basePath) throws NullPointerException {
		List<File> folders = new LinkedList<File>();
		for (File file : basePath.listFiles()) {
			if (file.isDirectory())
				folders.add(file);
		}
		return folders;
	}

	@SuppressWarnings("rawtypes")
	public Set<AbstractGraphicalPN> getPetriNets(){
		TreeMap<AbstractGraphicalPN, File> sort = new TreeMap<AbstractGraphicalPN, File>(new SwatComperator(nets));
		sort.putAll(nets);//here: sort done through TreeMap
		return sort.keySet();
	}
	public List<File> getAnalysisForNet(String basePath){
		List<File> sort = new ArrayList<File>();
		File dir=new File(basePath);
		for (File file : dir.listFiles()) {
			if (file.getName().startsWith(AnalysisStore.PREFIX))
				sort.add(file);
		}
		return sort;
	}
	
	public Set<LogModel> getLogFiles() {
		TreeMap<LogModel, File> sort = new TreeMap<LogModel, File>(new SwatComperator());
		sort.putAll(logs);
		return sort.keySet();
	}

	public Set<LogModel> getXMLFiles() {
		TreeMap<LogModel, File> sort = new TreeMap<LogModel, File>(new SwatComperator());
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

	public File getFile(LogModel model) {
		File file = logs.get(model);
		if (file == null)
			return xml.get(model);
		return file;
	}

	//	
	//	public File getFile(LogFile){
	//		
	//	}
	
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
			}
		}

		//csv file
		if (currentComponent instanceof LogFileViewer) {
			for (LogModel model : logs.keySet()) {
				if (model == ((LogFileViewer) currentComponent).getModel())
						return logs.get(model);
			}

		}
		//xml file
		if (currentComponent instanceof XMLFileViewer) {
			for (LogModel model : xml.keySet()) {
				if (model == ((XMLFileViewer) currentComponent).getModel())
					return logs.get(model);
			}

		}
		throw new ParameterException("not a valid SwatComponent");
	}

	public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
		List<T> list = new ArrayList<T>(c);
		java.util.Collections.sort(list);
		return list;
	}

	public void addSwatComponentListener(SwatComponentsListener listener) {
		try {
			this.listener.add(listener);
		} catch (Exception e) {//listener may be null
		}
	}

	public void removeSwatComponentListener(SwatComponentsListener listener) {
		this.listener.remove(listener);
	}

	private void informListenerOfModelChange() {
		for (SwatComponentsListener listener : this.listener) {
			listener.modelChanged();
		}
	}

	private void informAnalysisAdded(SwatTreeNode node, Object AnalysisElement) {
		for (SwatComponentsListener listener : this.listener) {
			listener.analysisAdded(node, AnalysisElement);
		}
	}

	//	private void informNodeAdded(SwatTreeNode node) {
	//		for (SwatComponentsListener listener : this.listener) {
	//			listener.swatTreeNodeAdded(node);
	//		}
	//	}

	private void informElementRemoved(Object elem) {
		for (SwatComponentsListener listener : this.listener) {
			listener.elementRemoved(elem);
		}
	}

	public void putCsvIntoSwatComponent(LogModel model) throws ParameterException, PropertyException, IOException {
		putCsvIntoSwatComponent(model, model.getName());
	}
	
	public void putLogAnalysisIntoSwatComponent(LogAnalysisModel model, LogModel correspondingLog){
		if(logAnalysis.containsKey(correspondingLog))
			logAnalysis.get(correspondingLog).add(model);//put into list
		ArrayList<LogAnalysisModel> list = new ArrayList<LogAnalysisModel>();
		list.add(model);
		logAnalysis.put(correspondingLog, list);
		storeLogAnalysis(model, correspondingLog);
	}

	private void storeLogAnalysis(LogAnalysisModel model, LogModel correspondingLog) {
		// copy correspondingLog

	}

	public List<LogAnalysisModel> getAnalysisForLog(LogModel model) {
		return logAnalysis.get(model);
	}
	
	/**
	 * Adds a new access control model.<br>
	 * The context is also stores as property-file in the simulation directory.
	 * @param acModel The model to add.
	 * @throws ParameterException if the given model is <code>null</code>.
	 * @throws PropertyException if the procedure of property extraction fails.
	 * @throws IOException if the property-representation of the new model cannot be stored.
	 */
	public void addACModel(ACModel acModel) throws ParameterException, IOException, PropertyException{
		addACModel(acModel, true);
	}
	
	/**
	 * Adds a new access control model.<br>
	 * Depending on the value of the store-parameter, the model is also stores as property-file in the simulation directory.
	 * @param acModel The new model to add.
	 * @param storeToFile Indicates if the model should be stored on disk.
	 * @throws ParameterException if any parameter is invalid.
	 * @throws PropertyException if the model cannot be stored due to an error during property extraction.
	 * @throws IOException if the model cannot be stored due to an I/O Error.
	 */
	public void addACModel(ACModel acModel, boolean storeToFile) throws ParameterException, IOException, PropertyException{
		Validate.notNull(acModel);
		Validate.notNull(storeToFile);
		acModels.put(acModel.getName(), acModel);
		if(storeToFile){
			storeACModel(acModel);
		}
	}
	
	/**
	 * Stores the given access control model in form of a property-file in the simulation directory.<br>
	 * The context name will be used as file name.
	 * @param acModel The model to store.
	 * @throws ParameterException if the given model is <code>null</code> or invalid.
	 * @throws IOException if the model cannot be stored due to an I/O Error.
	 * @throws PropertyException if the model cannot be stored due to an error during property extraction.
	 */
	public void storeACModel(ACModel acModel) throws ParameterException, IOException, PropertyException{
		Validate.notNull(acModel);
//		acModel.getProperties().store(GeneralProperties.getInstance().getPathForACModels()+acModel.getName());
	}
	
	/**
	 * Checks, if there are access control model-components.
	 * @return <code>true</code> if there is at least one access control model;<br>
	 * <code>false</code> otherwise.
	 */
	public boolean containsACModels(){
		return !acModels.isEmpty();
	}
	
	/**
	 * Checks, if there is an access control model with the given name.
	 * @return <code>true</code> if there is such a model;<br>
	 * <code>false</code> otherwise.
	 */
	public boolean containsACModel(String name){
		return acModels.get(name) != null;
	}
	
	/**
	 * Returns all access control models, i.e. models stored in the simulation directory.
	 * @return A set containing all contexts.
	 */
	public Collection<ACModel> getACModels(){
		return Collections.unmodifiableCollection(acModels.values());
	}
	
	/**
	 * Returns the access control model with the given name, if there is one.
	 * @param name The name of the desired access control model.
	 * @return The model with the given name, or <code>null</code> if there is no such model.
	 * @throws ParameterException if the given name is <code>null</code>.
	 */
	public ACModel getACModel(String name) throws ParameterException{
		Validate.notNull(name);
		selectedACModel = name;
		return acModels.get(name);
	}
	
	public ACModel getSelectedACModel() throws ParameterException{
		Validate.notNull(selectedACModel);
		return acModels.get(selectedACModel);
	}
	
	/**
	 * Returns the names of all access control models.
	 * @return
	 */
	public Set<String> getACModelNames(){
		return Collections.unmodifiableSet(acModels.keySet());
	}
	
	/**
	 * Removes the access control model with the given name from the simulation components<br>
	 * and also deletes the corresponding property-file in the simulation directory.
	 * @param name The name of the model to remove.
	 * @throws PropertyException if the path for the simulation directory cannot be extracted from the general properties file.
	 * @throws ParameterException if there is an internal parameter misconfiguration.
	 * @throws IOException if the corresponding property file for the model cannot be deleted.
	 */
	public void removeACModel(String name) throws PropertyException, IOException, ParameterException{
		if(acModels.remove(name) != null){
//			FileUtils.deleteFile(GeneralProperties.getInstance().getPathForACModels()+name);
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

	public int compare(LogModel comp1, LogModel comp2) {
		return comp1.getName().compareTo(comp2.getName());
	}

	@Override
	public int compare(Object o1, Object o2) {
		if (o1 instanceof SwatComponent)
			return compare((SwatComponent) o1, (SwatComponent) o2);
		if (o1 instanceof LogModel)
			return compare((LogModel) o1, (LogModel) o2);
		else
			return compare((AbstractGraphicalPN) o1, (AbstractGraphicalPN) o2);
	}
}
