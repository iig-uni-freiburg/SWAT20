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

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import de.invation.code.toval.file.FileUtils;
import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet.PNMLIFNetAnalysisContextParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
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
	private Map<String, LinkedList<AnalysisContext>> analyseContext = new HashMap<String, LinkedList<AnalysisContext>>(0);
	private Map<String, TimeContext> timeContext = new HashMap<String, TimeContext>();

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
	
	public List<AnalysisContext> getIFAnalysisContextForNet(String name) {
		return analyseContext.get(name);
	}
	
	public AnalysisContext getIFAnalysisContextForNetWithName(String name, String analysisContextModelName) {
		List<AnalysisContext> list = getIFAnalysisContextForNet(name);
		for(AnalysisContext a:list){
			if(a.getName().equals(analysisContextModelName))
				return a;
		}
		return null;
	}

	public void addAnalysisContextForNet(String name, AnalysisContext aContext) {
		if (analyseContext.get(name) == null)
			analyseContext.put(name, new LinkedList<AnalysisContext>());
		analyseContext.get(name).add(aContext);
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
		net.getPetriNet().setName(name);
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
		String workingName;
		if (name.endsWith(".pnml"))
			workingName = name.substring(0, name.indexOf(".pnml"));
		else
			workingName = name;
		File folder = new File(SwatProperties.getInstance().getNetWorkingDirectory(), workingName);
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

		//3. Load ACModels
		MessageDialog.getInstance().addMessage("3. Loading AC Models");
		loadAcModels();
		MessageDialog.getInstance().addMessage("Done.");
	}
	
	
	private void loadAcModels() throws ParameterException, PropertyException, IOException {
		File path = new File(SwatProperties.getInstance().getAcModelWorkingDirectory());
		if (!path.exists())
			path.mkdir();

		List<String> acFiles = null;
		try {
			acFiles = FileUtils.getFileNamesInDirectory(SwatProperties.getInstance().getAcModelWorkingDirectory(), true);
		} catch (IOException e1) {
			throw new IOException("Cannot access access control model directory.");
		}
		for (String acFile : acFiles) {
			MessageDialog.getInstance().addMessage(
					"Loading access control model: " + acFile.substring(acFile.lastIndexOf('/') + 1) + "...   ");
			try {
				addACModel(loadACModel(acFile), false);
				MessageDialog.getInstance().addMessage("Done.");
			} catch (Exception e) {
				MessageDialog.getInstance().addMessage("Error: " + e.getMessage());
			}
		}
		//MessageDialog.getInstance().newLine();

	}

	private ACModel loadACModel(String acFile) throws PropertyException, ParameterException, IOException {
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
			throw new IOException("Cannot load properties file: " + acFile + ".");
		}
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
				if (loadedNet != null) {
					getAnalysisFor(loadedNet, folder);
				loadAnalysisContextFor(loadedNet, folder);
					loadTimeAnalysisContextFor(loadedNet, folder);
				}
			} catch (NullPointerException e) {
				//folder does not exists. create
				new File(SwatProperties.getInstance().getNetWorkingDirectory()).mkdir();
			}
		}

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

	private void loadAnalysisContextFor(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> loadedNet, File folder) {
		List<File> analysisContext;
		try {
			SwatProperties prop = SwatProperties.getInstance();
			File pathToAnalysisContext = new File(getFile(loadedNet).getParent(), prop.getAnalysisFolderName());
			String pathToAnalysisContextString = pathToAnalysisContext.getAbsolutePath();
			if (!pathToAnalysisContext.exists())
				return;
			analysisContext = FileUtils.getFilesInDirectory(pathToAnalysisContextString, true, true, "xml");

			for (File context : analysisContext) {
				try {
					AnalysisContext ac = PNMLIFNetAnalysisContextParser.parse(context, false);
					addAnalysisContextForNet(loadedNet.getPetriNet().getName(), ac);
					MessageDialog.getInstance().addMessage("Loaded Analysis-Context for " + loadedNet.getPetriNet().getName());

				} catch (ParserException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		catch (ParameterException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
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
		if(net.getPetriNet() instanceof IFNet){
			IFNet ifnet = (IFNet) net.getPetriNet();
			if(ifnet.getAnalysisContext() != null)
				storeAnalysisContextOfIFNet(ifnet);
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

	public AbstractGraphicalPN getNetFromFileName(String name) {
		for (AbstractGraphicalPN net : nets.keySet()) {
		if (net.getPetriNet().getName().equals(name))
			return net;
	}
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

	private void storeAnalysisContextOfIFNet(IFNet ifnet) {
		AnalysisContext context = ifnet.getAnalysisContext();
		if(context!=null){
			//Currently only the selected net with its corresponding analysis contexts is stored
//			for (AnalysisContext context : contexts) {
				try {
					File path = new File(getFile(getNetFromFileName(ifnet.getName())).getParent(), SwatProperties.getInstance().getAnalysisFolderName());
					if (!path.exists())
						path.mkdir(); //make the directory if it does not exist
					ACSerialization.serialize(context,path.getAbsolutePath()+ System.getProperty("file.separator"),context.getName());

				} catch (SerializationException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

//			}
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
		File pathToStore = new File(SwatProperties.getInstance().getAcModelWorkingDirectory(), acModel.getName());
		acModel.getProperties().store(pathToStore.getAbsolutePath());
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
	
	public boolean containsACModelSelection() throws ParameterException{
	
		return selectedACModel != null;
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

	public TimeContext getTimeAnalysisForNet(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> cur_net) {
		String name = cur_net.getPetriNet().getName();
		TimeContext context = timeContext.get(name);
		return context;
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
