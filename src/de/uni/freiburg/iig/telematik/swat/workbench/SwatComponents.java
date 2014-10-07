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
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import de.invation.code.toval.file.FileUtils;
import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPNNameComparator;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet.PNMLIFNetAnalysisContextParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.serialize.ACSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.SerializationException;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNSerializationFormat;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.ACModel;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.acl.ACLModel;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.properties.ACLModelProperties;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.properties.ACMValidationException;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.properties.ACModelProperties;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.properties.ACModelType;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.properties.RBACModelProperties;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.rbac.RBACModel;
import de.uni.freiburg.iig.telematik.swat.bernhard.AnalysisStorage;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.logs.LogAnalysisModel;
import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.logs.XMLFileViewer;
import de.uni.freiburg.iig.telematik.swat.misc.FileHelper;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContext;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatComponentsListener;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class SwatComponents {
	
	private static SwatComponents instance = null;
	
	private Set<SwatComponentsListener> listener = new HashSet<SwatComponentsListener>();
	
	private Map<String, AbstractGraphicalPN> nets = new HashMap<String, AbstractGraphicalPN>();
	private Map<String, File> netFiles = new HashMap<String, File>();
	private Map<String, List<AnalysisContext>> analysisContexts = new HashMap<String, List<AnalysisContext>>();

	
	
	private Map<String, ACModel> acModels = new HashMap<String, ACModel>();
	private Map<LogModel, File> logs = new HashMap<LogModel, File>();
	private Map<LogModel, File> xml = new LinkedHashMap<LogModel, File>();
	private Map<LogModel, List<LogAnalysisModel>> logAnalysis = new HashMap<LogModel, List<LogAnalysisModel>>();
	
	private Map<String, TimeContext> timeContext = new HashMap<String, TimeContext>();

	private SwatComponents() {
		try {
			loadSwatComponents();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Cannot access working directory:<br>" + e.getMessage(), "Internal Exception", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static SwatComponents getInstance() {
		if(instance == null){
			instance = new SwatComponents();
		}
		return instance;
	}
	
	//------- Load simulation components ------------------------------------------------------------------------------------
	
	private void loadSwatComponents() throws SwatComponentException {
		//1. Load AC-Models
		MessageDialog.getInstance().addMessage("3. Loading AC Models");
		loadACModels();
		MessageDialog.getInstance().addMessage("Done.");
		
		//2. Load Petri nets
		MessageDialog.getInstance().addMessage("1. Searching for Petri nets:");
		loadPetriNets();
		MessageDialog.getInstance().addMessage("Done.");
		MessageDialog.getInstance().newLine();

		//3. Load logfiles
		MessageDialog.getInstance().addMessage("2. Searching for log files:");
		loadLogFiles();
		MessageDialog.getInstance().addMessage("Done.");
		MessageDialog.getInstance().newLine();
	}
	
	public void reload() throws SwatComponentException {
		nets.clear();
		netFiles.clear();
		logs.clear();
		xml.clear();
		loadSwatComponents();
//		informListenerOfModelChange();
	}
	
	private void loadACModels() throws SwatComponentException{
		try{
			String modelDirectory = SwatProperties.getInstance().getPathForACModels();
			for(String acFile: FileUtils.getFileNamesInDirectory(modelDirectory, true)){
				ACModel acModel = loadACModel(acFile);
				if(acModel == null)
					throw new SwatComponentException("null-reference for loaded access control model.");
				addACModel(acModel, false);
			}
		} catch (PropertyException e) {
			throw new SwatComponentException("Cannot extract Swat-property.<br>Reason:" + e.getMessage());
		} catch (IOException e) {
			throw new SwatComponentException("Cannot access Petri net directory.<br>Reason:" + e.getMessage());
		}
	}
	
	private ACModel loadACModel(String acFile) throws SwatComponentException {
		MessageDialog.getInstance().addMessage("Loading AC model: " + FileUtils.getName(acFile) + "...   ");
		ACModelProperties testProperties = new ACModelProperties();
		try {
			testProperties.load(acFile);

			// Check ACModel type
			ACModel newModel = null;
			if (testProperties.getType().equals(ACModelType.ACL)) {
				ACLModelProperties aclProperties = new ACLModelProperties();
				aclProperties.load(acFile);
				newModel = new ACLModel(aclProperties);
			} else {
				RBACModelProperties rbacProperties = new RBACModelProperties();
				rbacProperties.load(acFile);
				newModel = new RBACModel(rbacProperties);
			}
			try {
				newModel.checkValidity();
			} catch (ACMValidationException e) {
				throw new ParameterException(e.getMessage());
			}
			return newModel;
		} catch (IOException e) {
			throw new SwatComponentException("Cannot access access control model file.<br>Reason:" + e.getMessage());
		} catch (PropertyException e) {
			throw new SwatComponentException("Cannot extract Swat-property.<br>Reason:" + e.getMessage());
		}
	}
	
	
	private void loadPetriNets() throws SwatComponentException {
		try {
			String netDirectory = SwatProperties.getInstance().getPathForNets();
			for (File folder : FileUtils.getSubdirectories(netDirectory)) {
				String loadedNetID = loadPetriNet(folder);
				loadAnalysisContextsFor(loadedNetID);
			}
		} catch (PropertyException e) {
			throw new SwatComponentException("Cannot extract Swat-property.<br>Reason:" + e.getMessage());
		} catch (IOException e) {
			throw new SwatComponentException("Cannot access Petri net directory.<br>Reason:" + e.getMessage());
		}

	}
	
	/** 
	 * Loads the first found pnml net from folder 
	 * @throws SwatComponentException 
	 **/
	private String loadPetriNet(File netDirectory) throws SwatComponentException {
		Validate.notNull(netDirectory);
		List<File> pnmlFiles = null;
		try {
			pnmlFiles = FileUtils.getFilesInDirectory(netDirectory.getAbsolutePath(), true, true, "pnml");
		} catch (IOException e) {
			throw new SwatComponentException("Cannot access files in net directory.<br>Reason: " + e.getMessage());
		}
		if (pnmlFiles.size() > 1)
			throw new SwatComponentException("Found more than on pnml-File in " + netDirectory.toString());

		File netFile = pnmlFiles.get(0);
		MessageDialog.getInstance().addMessage("Loading Petri net: " + FileUtils.getName(netFile) + "...   ");

		try {
			MessageDialog.getInstance().addMessage(netFile.getCanonicalPath());
			AbstractGraphicalPN loadedNet = new PNMLParser().parse(netFile, SwatProperties.getInstance().getRequestNetType(), SwatProperties.getInstance().getPNValidation());
			if(loadedNet == null)
				throw new SwatComponentException("null-reference for parsed net.");
			addPetriNet(loadedNet, netFile, false);
			return loadedNet.getPetriNet().getName();
		} catch (IOException e) {
			throw new SwatComponentException("Cannot access Petri net file.<br>Reason: " + e.getMessage());
		} catch (ParserException e) {
			throw new SwatComponentException("Cannot parse Petri net.<br>Reason: " + e.getMessage());
		} catch (PropertyException e) {
			throw new SwatComponentException("Cannot extract Swat-property.<br>Reason: " + e.getMessage());
		}
	}
	
	private void loadAnalysisContextsFor(String netID) throws SwatComponentException {
		try {
			File pathToAnalysisContexts = new File(getPetriNetFile(netID).getParent(), SwatProperties.getInstance().getAnalysisContextDirectoryName());
			if (!pathToAnalysisContexts.exists())
				return;
			
			for (File context : FileUtils.getFilesInDirectory(pathToAnalysisContexts.getAbsolutePath(), true, true, "xml")) {
				try {
					AnalysisContext aContext = PNMLIFNetAnalysisContextParser.parse(context, false);
					addAnalysisContext(aContext, netID, false);
					MessageDialog.getInstance().addMessage("Loaded Analysis-Context for net " + netID);
				} catch (ParserException e) {
					throw new SwatComponentException("Cannot parse analysis context for net \""+netID+"\"");
				} catch (IOException e) {
					throw new SwatComponentException("Cannot access analysis context file for net \""+netID+"\"");
				}
			}
		} catch (IOException e1) {
			throw new SwatComponentException("Cannot access analysis context directory for net \""+netID+"\"");
		}
	}
	
	private void loadLogFiles() throws SwatComponentException {
		try {
			for (File folder : FileUtils.getSubdirectories(SwatProperties.getInstance().getPathForLogs())) {
				List<File> mxmlFiles = FileUtils.getFilesInDirectory(folder.getAbsolutePath(), true, true, "mxml");
				List<File> xmlFiles = FileUtils.getFilesInDirectory(folder.getAbsolutePath(), true, true, "xml");
				List<File> csvFiles = FileUtils.getFilesInDirectory(folder.getAbsolutePath(), true, true, "csv");

				short numFiles = (short) (mxmlFiles.size() + xmlFiles.size() + csvFiles.size());
				if (numFiles == 0)
					throw new SwatComponentException("No compatible log file in folder \"" + FileUtils.getDirName(folder.getAbsolutePath()) + "\"");
				if (numFiles > 1)
					throw new SwatComponentException("More than one file in folder \"" + FileUtils.getDirName(folder.getAbsolutePath()) + "\"");

				if (!mxmlFiles.isEmpty()) {

				} else if (!xmlFiles.isEmpty()) {

				} else if (!csvFiles.isEmpty()) {

				}
			}
		} catch (IOException e) {
			throw new SwatComponentException("Cannot acces log directory.<br>Reason: " + e.getMessage());
		} catch (PropertyException e) {
			throw new SwatComponentException("Cannot extract log path from Swat-properties.<br>Reason: " + e.getMessage());
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

	
	
	//---- Adding and removing Petri nets -----------------------------------------------------------------------------------
	
	private void addPetriNet(AbstractGraphicalPN net) throws SwatComponentException {
		File netFile = generateNetFile(net.getPetriNet().getName());
		netFile.mkdirs();
		addPetriNet(net, netFile, true);
	}
	
	private File generateNetFile(String netID) throws SwatComponentException {
		try {
			return new File(SwatProperties.getInstance().getPathForNets(), netID + "/" + netID + ".pnml");
		} catch (PropertyException e) {
			throw new SwatComponentException("Cannot extract net directory.<br>Reason: "+e.getMessage());
		} catch (IOException e) {
			throw new SwatComponentException("Cannot load properties file.<br>Reason: "+e.getMessage());
		}
	}
	
	private String addPetriNet(AbstractGraphicalPN net, File file, boolean storeToFile) throws SwatComponentException {
		Validate.notNull(net);
		Validate.notNull(file);
		String netID = net.getPetriNet().getName();
		if(nets.containsKey(netID))
			throw new SwatComponentException("SwatComponents already contains net with ID \"" + netID + "\"");
		
		nets.put(netID, net);
		netFiles.put(netID, file);
		if(storeToFile){
			storePetriNet(netID);
		}
		
		return netID;
		
		//TODO: Listener informieren!
//		informListenerOfModelChange();
//		informNodeAdded(SwatTreeView.getInstance().new SwatTreeNode(net, SwatComponentType.PETRI_NET, file));
	}
	
	public void storeAllPetriNets() throws SwatComponentException{
		for(String netID: nets.keySet()){
			storePetriNet(netID);
		}
	}
	
	public void storePetriNet(String netID) throws SwatComponentException {
		try {
			PNSerialization.serialize(getPetriNet(netID), PNSerializationFormat.PNML, getPetriNetFile(netID).getCanonicalPath());
		} catch (SerializationException e) {
			throw new SwatComponentException("Cannot serialize Petri net.<br>Reason: "+e.getMessage());
		} catch (IOException e) {
			throw new SwatComponentException("Cannot store Petri net.<br>Reason: "+e.getMessage());
		}
		
		//TODO: Verbindung zu AC?
//		if(net.getPetriNet() instanceof IFNet){
//			IFNet ifnet = (IFNet) net.getPetriNet();
//			if(ifnet.getAnalysisContext() != null)
//				storeAnalysisContextOfIFNet(ifnet);
//		}
		
//		JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(SwatTreeView.getInstance()), "Could not save PT-net",
//				"Could not save",
//				JOptionPane.ERROR_MESSAGE);
//		Workbench.errorMessage("Cannot store petri net: " + net.getPetriNet().getName());
	}
	
	/**
	 * Checks, if there are petri nets.
	 * @return <code>true</code> if there is at least one net;<br>
	 * <code>false</code> otherwise.
	 */
	public boolean hasPetriNets(){
		return !nets.isEmpty();
	}
	
	public boolean containsPetriNetWithID(String netID){
		return nets.containsKey(netID);
	}
	
	/**
	 * Returns the names of all Petri nets.
	 * @return
	 */
	public Set<String> getPetriNetNames(){
		return Collections.unmodifiableSet(nets.keySet());
	}
	
	/**
	 * Returns all Petri nets, i.e. nets stored in the simulation directory.
	 * @return A set containing all Petri nets.
	 */
	public Collection<AbstractGraphicalPN> getPetriNets(){
		return Collections.unmodifiableCollection(nets.values());
	}
	
	public List<AbstractGraphicalPN> getPetriNetsSorted(){
		List<AbstractGraphicalPN> netList = new ArrayList<AbstractGraphicalPN>(SwatComponents.getInstance().getPetriNets());
		Collections.sort(netList, new GraphicalPNNameComparator());
		return netList;
	}

	/**
	 * Returns the Petri net with the given name, if there is one.
	 * @param name The name of the desired net.
	 * @return The Petri net with the given name, or <code>null</code> if there is no such net.
	 * @throws ParameterException if the given name is <code>null</code>.
	 */
	public AbstractGraphicalPN getPetriNet(String netID) throws SwatComponentException{
		validatePetriNet(netID);
		return nets.get(netID);
	}
	
	public File getPetriNetFile(String netID) throws SwatComponentException{
		validatePetriNet(netID);
		return netFiles.get(netID);
	}
	
	
	/**
	 * Removes the Petri net with the given name from the simulation components<br>
	 * and also deletes the corresponding property-file in the simulation directory.
	 * @param name The name of the net to remove.
	 * @throws PropertyException if the path for the simulation directory cannot be extracted from the general properties file.
	 * @throws ParameterException if there is an internal parameter misconfiguration.
	 * @throws IOException if the corresponding property file for the Petri net cannot be deleted.
	 */
	public void removePetriNet(String netID, boolean removeFileFromDisk) throws SwatComponentException{
		validatePetriNet(netID);
		File netFile = getPetriNetFile(netID);
		netFiles.remove(netID);
		nets.remove(netID);
		if(removeFileFromDisk){
			FileUtils.deleteFile(netFile);
		}
		//TODO: Verbindung zu AC?
	}
	
	public void renamePetriNet(String oldID, String newID) throws SwatComponentException{
		validatePetriNet(oldID);
		nets.put(newID, getPetriNet(oldID));
		nets.remove(oldID);
		netFiles.put(newID, getPetriNetFile(oldID));
		netFiles.remove(oldID);
		getPetriNet(newID).getPetriNet().setName(newID);
		//TODO: Verbindung zu AC?
	}

	private void validatePetriNet(String netID) throws SwatComponentException{
		if(!nets.containsKey(netID))
			throw new SwatComponentException("SwatComponents does not contain a net with name \"" + netID + "\"");
		if(!netFiles.containsKey(netID))
			throw new SwatComponentException("SwatComponents does not contain a file reference for net \"" + netID + "\"");
	}
	
	public static void main(String args[]) {
		SwatComponents.getInstance();
	}
	
	
	//---- Adding and removing analysis contexts ----------------------------------------------------------------------------
	
	public List<AnalysisContext> getAnalysisContexts(String netID) {
		if(!analysisContexts.containsKey(netID))
			return new ArrayList<AnalysisContext>();
		return analysisContexts.get(netID);
	}
	
	public AnalysisContext getAnalysisContext(String netID, String aContextName) {
		for(AnalysisContext aContext: getAnalysisContexts(netID)){
			if(aContext.getName().equals(aContextName))
				return aContext;
		}
		return null;
	}

	private void addAnalysisContext(AnalysisContext aContext, String netID, boolean storeToFile) throws SwatComponentException {
		if(analysisContexts.get(netID) == null)
			analysisContexts.put(netID, new ArrayList<AnalysisContext>());
		analysisContexts.get(netID).add(aContext);
		if(storeToFile){
			storeAnalysisContext(aContext, netID);
		}
	}

	public void storeAnalysisContext(AnalysisContext aContext, String netID) throws SwatComponentException {
		try {
			File pathToAnalysisContexts = new File(getPetriNetFile(netID).getParent(), SwatProperties.getInstance().getAnalysisContextDirectoryName());
			pathToAnalysisContexts.mkdirs();
			ACSerialization.serialize(aContext, pathToAnalysisContexts.getAbsolutePath() + aContext.getName());
		} catch (SerializationException e) {
			throw new SwatComponentException("Cannot serialize analysis context \"" + aContext.getName() + "\".<br>Reason: "+e.getMessage());
		} catch (IOException e) {
			throw new SwatComponentException("Cannot store analysis context \"" + aContext.getName() + "\".<br>Reason: "+e.getMessage());
		}
	}
	
	
	//---- Adding and removing AC models ------------------------------------------------------------------------------------
	
	/**
	 * Adds a new access control model.<br>
	 * The context is also stores as property-file in the simulation directory.
	 * @param acModel The model to add.
	 * @throws SwatComponentException
	 */
	public void addACModel(ACModel acModel) throws SwatComponentException {
		addACModel(acModel, true);
	}
	
	/**
	 * Adds a new access control model.<br>
	 * Depending on the value of the store-parameter, the model is also stores as property-file in the simulation directory.
	 * @param acModel The new model to add.
	 * @param storeToFile Indicates if the model should be stored on disk.
	 * @throws SwatComponentException
	 */
	public void addACModel(ACModel acModel, boolean storeToFile) throws SwatComponentException {
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
	 * @throws SwatComponentException
	 */
	public void storeACModel(ACModel acModel) throws SwatComponentException {
		Validate.notNull(acModel);
		try{
			File pathToStore = new File(SwatProperties.getInstance().getPathForACModels(), acModel.getName());
			acModel.getProperties().store(pathToStore.getAbsolutePath());
		} catch (PropertyException e) {
			throw new SwatComponentException("Cannot extract AC model path.<br>Reason: " + e.getMessage());
		} catch (IOException e) {
			throw new SwatComponentException("Cannot store AC model file to disk.<br>Reason: " + e.getMessage());
		}
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
	 */
	public ACModel getACModel(String name) throws ParameterException{
		Validate.notNull(name);
		return acModels.get(name);
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
	 */
	public void removeACModel(String name, boolean removeFileFromDisk) throws SwatComponentException {
		validateACModel(name);
		acModels.remove(name);
		
		if(removeFileFromDisk){
			try {
				FileUtils.deleteFile(SwatProperties.getInstance().getPathForACModels()+name);
			} catch (PropertyException e) {
				throw new SwatComponentException("Cannot extract AC model path.<br>Reason: " + e.getMessage());
			} catch (IOException e) {
				throw new SwatComponentException("Cannot delete AC model file from disk.<br>Reason: " + e.getMessage());
			}
		}
	}

	private void validateACModel(String name) throws SwatComponentException{
		if(!acModels.containsKey(name))
			throw new SwatComponentException("SwatComponents does not contain an access control model with name \"" + name + "\"");
	}
	
	
	//---- Adding and removing Log files ------------------------------------------------------------------------------------
	
	
	
	//-----------------------------------------------------------------------------------------------------------------------

	

//	public void setLayoutNeed(AbstractGraphicalPN net) {
//		needsLayout.add(net.getPetriNet().getName());
//	}
//
//	public boolean getLayoutNeed(AbstractGraphicalPN net) {
//		return needsLayout.contains(net.getPetriNet().getName());
//	}
//
//	public void removeLayoutNeed(AbstractGraphicalPN net) {
//		needsLayout.remove(net.getPetriNet().getName());
//	}

//	public void remove(File file) {
//		HashMap<Object, File> all = new HashMap<Object, File>();
//		all.putAll(xml);
//		all.putAll(logs);
//		all.putAll(nets);
//		for (Entry<Object, File> entry : all.entrySet()) {
//			if (entry.getValue().equals(file))
//			remove(entry.getKey());
//		}
//	}

//	private void remove(Object key) {
//		boolean found=false;
//		if (xml.containsKey(key)) {
//			xml.remove(key);
//			found=true;
//		}
//
//		if (logs.containsKey(key)) {
//			logs.remove(key);
//			found=true;
//		}
//		if (nets.containsKey(key)) {
//			nets.remove(key);
//			found=true;
//		}
//		
//		if(found) informElementRemoved(key);
//	}
	
	



	public File putCsvIntoSwatComponent(LogModel model, String name) throws PropertyException, IOException {
		File file = generateCsvLogPath(name);
		//Copy file
		FileHelper.copyFile(model.getFileReference(), file);
		LogModel newModel = new LogModel(file);
		logs.put(newModel, file);
		//informListenerOfModelChange();
//		informNodeAdded(SwatTreeView.getInstance().new SwatTreeNode(newModel, SwatComponentType.LOG_FILE, file));
		return file;
	}

//	private void informNodeAdded(SwatTreeNode swatTreeNode) {
//		for (SwatComponentsListener listener : this.listener) {
//			listener.nodeAdded(swatTreeNode);
//		}
//
//	}

	private File generateCsvLogPath(String name) throws PropertyException, IOException {
		//Make Directory
		File folder = new File(SwatProperties.getInstance().getLogWorkingDirectory(), name);
		folder.mkdir();
		return new File(folder, name + ".csv");
	}

	private void loadTimeAnalysisContextFor(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> loadedNet, File folder) throws IOException {
		File timeFolder = new File(folder, SwatProperties.getInstance().getTimeAnalysisFolderName());
		for (File file : timeFolder.listFiles()) {
			if (file.getAbsolutePath().endsWith("xml")) {
				TimeContext context = TimeContext.parse(file);
				timeContext.put(loadedNet.getPetriNet().getName(), context);
			}
		}

	}

	public List<File> getAnalysisForNet(String basePath){
		List<File> sort = new ArrayList<File>();
		File dir=new File(basePath);
		for (File file : dir.listFiles()) {
			if (file.getName().startsWith(AnalysisStorage.PREFIX))
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

//	public File getFile(LogModel model) {
//		File file = logs.get(model);
//		if (file == null)
//			return xml.get(model);
//		return file;
//	}
	
//	@SuppressWarnings("rawtypes")
//	public void changeFileforNet(AbstractGraphicalPN net, File newFile) {
//		nets.put(net.getPetriNet().getName(), newFile);
//	}
	
//	public boolean containsNetWithFileName(String name){
//		return nets.containsValue(new File(name));
//	}

//	public AbstractGraphicalPN getNetFromFileName(String name) {
//		for (String netID : nets.keySet()) {
//		if (netID.equals(name))
//			return nets.get(netID);
//	}
//		return null;
//	}



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

//	private void informListenerOfModelChange() {
//		for (SwatComponentsListener listener : this.listener) {
//			listener.modelChanged();
//		}
//	}

//	private void informAnalysisAdded(SwatTreeNode node, Object AnalysisElement) {
//		for (SwatComponentsListener listener : this.listener) {
//			listener.analysisAdded(node, AnalysisElement);
//		}
//	}

	//	private void informNodeAdded(SwatTreeNode node) {
	//		for (SwatComponentsListener listener : this.listener) {
	//			listener.swatTreeNodeAdded(node);
	//		}
	//	}

//	private void informElementRemoved(Object elem) {
//		for (SwatComponentsListener listener : this.listener) {
//			listener.elementRemoved(elem);
//		}
//	}

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

	public void putTimeAnalysisIntoSwatComponent(String name, TimeContext context) {
		timeContext.put(name, context);
	}

	public void putTimeAnalysisIntoSwatComponent(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> net, TimeContext context) {
		timeContext.put(net.getPetriNet().getName(), context);
	}

	private void storeLogAnalysis(LogAnalysisModel model, LogModel correspondingLog) {
		// copy correspondingLog

	}

	public List<LogAnalysisModel> getAnalysisForLog(LogModel model) {
		return logAnalysis.get(model);
	}
	

	public TimeContext getTimeAnalysisForNet(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> cur_net) {
		String name = cur_net.getPetriNet().getName();
		TimeContext context = timeContext.get(name);
		return context;
	}
}

///** For use with TreeMap **/
//class SwatComparator implements Comparator<Object> {
//	// Note: this comparator imposes orderings that are inconsistent with equals. Only compares File Names   
//	Map<String, File> base;
//
//	/** because AbstractGraphicalPN does not carry its name:need mapping **/
//	public SwatComparator(Map<String, File> base) {
//		this.base = base;
//	}
//
//	public SwatComparator() {
//		this.base = null;
//	}
//
//	public int compare(AbstractGraphicalPN a, AbstractGraphicalPN b) {
//		return base.get(a).getName().compareTo(base.get(b).getName());
//		} // returning 0 would merge keys
//
//	public int compare(SwatComponent comp1, SwatComponent comp2) {
//		return comp1.getName().compareTo(comp2.getName());
//	}
//
//	public int compare(LogModel comp1, LogModel comp2) {
//		return comp1.getName().compareTo(comp2.getName());
//	}
//
//	@Override
//	public int compare(Object o1, Object o2) {
//		if (o1 instanceof SwatComponent)
//			return compare((SwatComponent) o1, (SwatComponent) o2);
//		if (o1 instanceof LogModel)
//			return compare((LogModel) o1, (LogModel) o2);
//		else
//			return compare((AbstractGraphicalPN) o1, (AbstractGraphicalPN) o2);
//	}
//}
