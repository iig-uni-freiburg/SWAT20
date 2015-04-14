package de.uni.freiburg.iig.telematik.swat.workbench;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import com.thoughtworks.xstream.XStream;

import de.invation.code.toval.file.FileUtils;
import de.invation.code.toval.misc.soabase.SOABase;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPNNameComparator;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet.AnalysisContextParser;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet.LabelingParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;
import de.uni.freiburg.iig.telematik.sepia.serialize.AnalysisContextSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.LabelingSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.SerializationException;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNSerializationFormat;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.AbstractACModel;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.acl.ACLModel;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.properties.ACLModelProperties;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.properties.ACMValidationException;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.properties.ACModelProperties;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.properties.ACModelType;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.properties.RBACModelProperties;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.rbac.RBACModel;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.logs.LogModelComparator;
import de.uni.freiburg.iig.telematik.swat.logs.SwatLog;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContext;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatComponentListenerSupport;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatComponentsListener;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class SwatComponents {
	
	private static final String CSVLogNameFormat = "%s%s.csv";
	private static final String AnalysisNameFormat = "%s%s.xml";
	
	private static SwatComponents instance = null;
	
	@SuppressWarnings("rawtypes")
	private Map<String, AbstractGraphicalPN> nets = new HashMap<String, AbstractGraphicalPN>();
	private Map<String, File> netFiles = new HashMap<String, File>();
	private Map<String, List<AnalysisContext>> analysisContexts = new HashMap<String, List<AnalysisContext>>();
	private Map<String, List<Labeling>> labelings = new HashMap<String, List<Labeling>>();
	private Map<String, List<TimeContext>> timeContexts = new HashMap<String, List<TimeContext>>();
	private Map<String, List<Analysis>> analyses = new HashMap<String, List<Analysis>>();
	
	private Map<String, SOABase> contexts = new HashMap<String, SOABase>();
	
	@SuppressWarnings("rawtypes")
	private Map<String, AbstractACModel> acModels = new HashMap<String, AbstractACModel>();
	
	private List<LogModel> aristaLogs = new ArrayList<LogModel>();
	private List<LogModel> mxmlLogs = new ArrayList<LogModel>();
	private List<LogModel> xesLogs = new ArrayList<LogModel>();
	
	private SwatComponentListenerSupport listenerSupport = new SwatComponentListenerSupport();
	

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
	
	public void addSwatComponentListener(SwatComponentsListener listener) {
		listenerSupport.addListener(listener);
	}

	public void removeSwatComponentListener(SwatComponentsListener listener) {
		listenerSupport.removeListener(listener);
	}
	
	//------- Load simulation components ------------------------------------------------------------------------------------
	
	private void loadSwatComponents() throws SwatComponentException {
		//1. Load  Contexts
		loadContexts();
		MessageDialog.getInstance().addMessage("Done.");
		
		//2. Load AC-Models
		loadACModels();
		MessageDialog.getInstance().addMessage("Done.");
		
		//3. Load Petri nets
		//   - analysis contexts
		//   - labelings
		//   - time contexts
		loadPetriNets();
		MessageDialog.getInstance().addMessage("Done.");
		MessageDialog.getInstance().newLine();

		//4. Load logfiles
		loadLogFiles();
		MessageDialog.getInstance().addMessage("Done.");
		MessageDialog.getInstance().newLine();
		
		//5. Load Analyses
		loadAnalyses();
		MessageDialog.getInstance().addMessage("Done.");
		MessageDialog.getInstance().newLine();
	}
	

	public void reload() throws SwatComponentException {
		nets.clear();
		netFiles.clear();
		aristaLogs.clear();
		mxmlLogs.clear();
		xesLogs.clear();
		analysisContexts.clear();
		timeContexts.clear();
		acModels.clear();
		contexts.clear();
		loadSwatComponents();
		listenerSupport.notifyComponentsChanged();
	}
	
	private void loadContexts(){
		MessageDialog.getInstance().addMessage("Loading Contexts...");
		String contextDirectory = null;
		try{
			contextDirectory = SwatProperties.getInstance().getPathForContexts();
		} catch (PropertyException e) {
			MessageDialog.getInstance().addMessage("Exception: Cannot extract path for contexts.\nReason:" + e.getMessage());
			return;
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Exception: Cannot access context directory.\nReason:" + e.getMessage());
			return;
		}
		
		// Extract context files
		Collection<String> contextFiles = null;
		try {
			contextFiles = FileUtils.getFileNamesInDirectory(contextDirectory, true);
		} catch(Exception e){
			MessageDialog.getInstance().addMessage("Exception: Cannot extract context files.\nReason:" + e.getMessage());
			return;
		}
		
		for(String contextFile: contextFiles){
			try{
				SOABase soaBase = SOABase.createFromFile(new File(contextFile));
				try {
					if (soaBase != null)
						addContext(soaBase, false);
				} catch (SwatComponentException e) {
					MessageDialog.getInstance().addMessage("Exception: Cannot add access control model.\nReason:" + e.getMessage());
				}
			} catch(Exception e){
				MessageDialog.getInstance().addMessage("Exception: Cannot create context from file\""+FileUtils.getName(contextFile)+"\".\nReason:" + e.getMessage());
				return;
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void loadACModels(){
		MessageDialog.getInstance().addMessage("Loading AC models...");
		String modelDirectory = null;
		try{
			modelDirectory = SwatProperties.getInstance().getPathForACModels();
		} catch (PropertyException e) {
			MessageDialog.getInstance().addMessage("Exception: Cannot extract path for access control models.\nReason:" + e.getMessage());
			return;
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Exception: Cannot access access control model directory.\nReason:" + e.getMessage());
			return;
		}
		
		// Extract AC model files
		Collection<String> acFiles = null;
		try {
			acFiles = FileUtils.getFileNamesInDirectory(modelDirectory, true);
		} catch(Exception e){
			MessageDialog.getInstance().addMessage("Exception: Cannot extract access control model files.\nReason:" + e.getMessage());
			return;
		}
		
		for(String acFile: acFiles){
			AbstractACModel acModel = loadACModel(acFile);
			try{
				if(acModel != null)
					addACModel(acModel, false);
			} catch(SwatComponentException e){
				MessageDialog.getInstance().addMessage("Exception: Cannot add access control model.\nReason:" + e.getMessage());
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private AbstractACModel loadACModel(String acFile) {
		MessageDialog.getInstance().addMessage("Loading AC model: " + FileUtils.getName(acFile) + "...   ");
		ACModelProperties testProperties = new ACModelProperties();
		try {
			testProperties.load(acFile);
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Cannot load access control model.\nReason:" + e.getMessage());
			return null;
		}
		
		String contextName = null;
		try{
			contextName = testProperties.getContextName();
		}catch(Exception e){
			
		}
		//Check if referred context has been loaded before
		if(!containsContext(contextName)){
			MessageDialog.getInstance().addMessage("Cannot load access control model.\nReason: Reference to non-existing context\"" + contextName + "\"");
			return null;
		}

		AbstractACModel newModel = null;
		try {
			// Check ACModel type
			if (testProperties.getType().equals(ACModelType.ACL)) {
				ACLModelProperties aclProperties = new ACLModelProperties();
				aclProperties.load(acFile);
				newModel = new ACLModel(aclProperties, getContext(contextName));
			} else {
				RBACModelProperties rbacProperties = new RBACModelProperties();
				rbacProperties.load(acFile);
				newModel = new RBACModel(rbacProperties.getName());
			}
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Cannot load access control model.\nReason:" + e.getMessage());
			return null;
		}
		
		try {
			newModel.checkValidity();
		} catch (ACMValidationException e) {
			MessageDialog.getInstance().addMessage("Cannot load invalid access control model.\nReason:" + e.getMessage());
			return null;
		}
		return newModel;
	}
	
	
	private void loadPetriNets() throws SwatComponentException {
		MessageDialog.getInstance().addMessage("Loading Petri nets...");
		String netDirectory = null;
		try{
			netDirectory = SwatProperties.getInstance().getPathForNets();
		} catch (PropertyException e) {
			MessageDialog.getInstance().addMessage("Exception: Cannot extract path for Petri nets.\nReason:" + e.getMessage());
			return;
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Exception: Cannot access Petri net directory.\nReason:" + e.getMessage());
			return;
		}
		
		// Extract Petri net files
		Collection<File> netFiles = null;
		try {
			netFiles = FileUtils.getSubdirectories(netDirectory);
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Exception: Cannot extract Petri net files.\nReason:" + e.getMessage());
			return;
		}
		
		for (File netFile : netFiles) {
			String loadedNetID = loadPetriNet(netFile);
			if(loadedNetID != null){
				if(getPetriNet(loadedNetID).getPetriNet().getNetType() == NetType.IFNet){
					Collection<AnalysisContext> analysisContexts = loadAnalysisContextsFor(loadedNetID);
					loadLabelings(loadedNetID, analysisContexts);
				}
				loadTimeContextsFor(loadedNetID);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private String loadPetriNet(File netDirectory) {
		MessageDialog.getInstance().addMessage("Loading Petri net in folder \"" + FileUtils.getDirName(netDirectory) + "\"...");
		
		// Extract PNML files
		List<File> pnmlFiles = null;
		try {
			pnmlFiles = FileUtils.getFilesInDirectory(netDirectory.getAbsolutePath(), true, true, "pnml");
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Exception: Cannot extract PNML files.\nReason:" + e.getMessage());
			return null;
		}
				
		if (pnmlFiles.isEmpty()){
			MessageDialog.getInstance().addMessage("No PNML-file in directory " + FileUtils.getDirName(netDirectory));
			return null;
		}
		if (pnmlFiles.size() > 1){
			MessageDialog.getInstance().addMessage("More than one PNML-file in directory " + FileUtils.getDirName(netDirectory));
			return null;
		}

		File netFile = pnmlFiles.get(0);
		MessageDialog.getInstance().addMessage("Loading Petri net: " + FileUtils.getName(netFile) + "...   ");

		AbstractGraphicalPN loadedNet = null;
		try {
			loadedNet = new PNMLParser().parse(netFile, SwatProperties.getInstance().getRequestNetType(), SwatProperties.getInstance().getPNValidation());
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Cannot parse PNML-file.\nReason: " + e.getMessage());
			return null;
		}
		
		if(loadedNet == null){
			MessageDialog.getInstance().addMessage("Exception: Null-reference for parsed net.");
			return null;
		}
		
		try {
			addPetriNet(loadedNet, netFile, false);
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Cannot add parsed Petri net.\nReason: " + e.getMessage());
			return null;
		}
		
		return loadedNet.getPetriNet().getName();
	}
	
	private Collection<AnalysisContext> loadAnalysisContextsFor(String netID) {
		MessageDialog.getInstance().addMessage("Loading analysis contexts for net \"" +netID+ "\"...");
		Collection<AnalysisContext> result = new HashSet<AnalysisContext>();
		File pathToAnalysisContexts = null;
		try{
			pathToAnalysisContexts = new File(getPetriNetFile(netID).getParent(), SwatProperties.getInstance().getAnalysisContextDirectoryName());
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Exception: Cannot access analysis context directory.\nReason:" + e.getMessage());
			return result;
		}
		
		if (!pathToAnalysisContexts.exists()){
			MessageDialog.getInstance().addMessage("No analysis contexts for net.");
			return result;
		}
		
		List<File> contextFiles = null;
		try {
			contextFiles = FileUtils.getFilesInDirectory(pathToAnalysisContexts.getAbsolutePath(), true, true, "acon");
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Exception: Cannot extract analysis context files.\nReason:" + e.getMessage());
			return result;
		}
			
		for (File contextFile : contextFiles) {
			AnalysisContext aContext = null;
			try {
				aContext = AnalysisContextParser.parse(contextFile, acModels.values());
			} catch (Exception e) {
				MessageDialog.getInstance().addMessage("Cannot parse analysis context for net \""+netID+"\"");
				continue;
			}
			
			try {
				addAnalysisContext(aContext, netID, false);
				result.add(aContext);
			} catch (Exception e) {
				MessageDialog.getInstance().addMessage("Cannot add analysis context for net \""+netID+"\"");
			}
			MessageDialog.getInstance().addMessage("Added analysis context \""+aContext.getName()+"\"");
		}
		return result;
	}
	
	private void loadLabelings(String netID, Collection<AnalysisContext> analysisContexts) {
		MessageDialog.getInstance().addMessage("Loading labelings for net \"" +netID+ "\"...");
		File pathToLabelings = null;
		try{
			pathToLabelings = new File(getPetriNetFile(netID).getParent(), SwatProperties.getInstance().getAnalysisContextDirectoryName());
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Exception: Cannot access labeling directory.\nReason:" + e.getMessage());
			return;
		}
		
		if (!pathToLabelings.exists()){
			MessageDialog.getInstance().addMessage("No labelings for net.");
			return;
		}
		
		List<File> labelingFiles = null;
		try {
			labelingFiles = FileUtils.getFilesInDirectory(pathToLabelings.getAbsolutePath(), true, true, "labeling");
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Exception: Cannot extract labeling files.\nReason:" + e.getMessage());
			return;
		}
			
		for (File labelingFile : labelingFiles) {
			Labeling labeling = null;
			try {
				labeling = LabelingParser.parse(labelingFile, analysisContexts);
			} catch (Exception e) {
				MessageDialog.getInstance().addMessage("Cannot parse labeling for net \""+netID+"\"");
				continue;
			}
			
			try {
				addLabeling(labeling, netID, labeling.getAnalysisContext().getName(), false);
			} catch (Exception e) {
				MessageDialog.getInstance().addMessage("Cannot add analysis context for net \""+netID+"\"");
			}
			MessageDialog.getInstance().addMessage("Added analysis context \""+labeling.getName()+"\"");
		}
	}
	
	private void loadLogFiles() {
		MessageDialog.getInstance().addMessage("Loading log files...");
		String pathToLogFiles = null;
		try{
			pathToLogFiles = SwatProperties.getInstance().getPathForLogs();
		} catch (PropertyException e) {
			MessageDialog.getInstance().addMessage("Exception: Cannot extract path for log files.\nReason:" + e.getMessage());
			return;
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Exception: Cannot access log file directory.\nReason:" + e.getMessage());
			return;
		}
		
		// Extract Petri net files
		Collection<File> logFolders = null;
		try {
			logFolders = FileUtils.getSubdirectories(pathToLogFiles);
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Exception: Cannot extract log directories.\nReason:" + e.getMessage());
			return;
		}
		
		for(File logFolder: logFolders){
			MessageDialog.getInstance().addMessage("Loading log files in folder \"" +FileUtils.getDirName(logFolder)+ "\"...");
			// Extract PNML files
			List<File> csvFiles = null;
			List<File> mxmlFiles = null;
			List<File> xesFiles = null;
			try {
				csvFiles = FileUtils.getFilesInDirectory(logFolder.getAbsolutePath(), true, true, "csv");
				mxmlFiles = FileUtils.getFilesInDirectory(logFolder.getAbsolutePath(), true, true, "mxml");
				xesFiles = FileUtils.getFilesInDirectory(logFolder.getAbsolutePath(), true, true, "xes");
			} catch (Exception e) {
				MessageDialog.getInstance().addMessage("Exception: Cannot extract log files in folder \""+FileUtils.getDirName(logFolder)+"\"\nReason:" + e.getMessage());
				return;
			}
			
			short numFiles = (short) (mxmlFiles.size() + xesFiles.size() + csvFiles.size());
			if (numFiles == 0){
				MessageDialog.getInstance().addMessage("Abort: No compatible log file");
				continue;
			}
			if (numFiles > 1){
				MessageDialog.getInstance().addMessage("Abort: More than one file");
				continue;
			}
			
			try {
				if (!mxmlFiles.isEmpty()) {
					MessageDialog.getInstance().addMessage("Loading MXML log...");
					addLogModel(new LogModel(mxmlFiles.get(0), SwatLog.MXML), false);
				} else if (!xesFiles.isEmpty()) {
					MessageDialog.getInstance().addMessage("Loading XES log...");
					addLogModel(new LogModel(xesFiles.get(0), SwatLog.XES), false);
				} else if (!csvFiles.isEmpty()) {
					MessageDialog.getInstance().addMessage("Loading AristaFlow log...");
					addLogModel(new LogModel(csvFiles.get(0), SwatLog.Aristaflow), false);
				}
				MessageDialog.getInstance().addMessage("Done.");
			} catch (Exception e) {
				MessageDialog.getInstance().addMessage("Exception: Cannot add log\nReason: " + e.getMessage());
				continue;
			}
		}
		sortLogModelLists();
	}
	
	private void loadLogAnalysisFor(String logID) {
		MessageDialog.getInstance().addMessage("Loading analyses for log \"" + logID + "\"...");
		File pathToAnalyses = null;
		try {
			pathToAnalyses = new File(getLogModel(logID).getFileReference().getParent(), SwatProperties.getInstance()
					.getNetAnalysesDirectoryName());
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Exception: Cannot access analyses directory.\nReason:" + e.getMessage());
			return;
		}

		if (!pathToAnalyses.exists()) {
			MessageDialog.getInstance().addMessage("No analyses for net.");
			return;
		}

		List<File> analysisFiles = null;
		try {
			analysisFiles = FileUtils.getFilesInDirectory(pathToAnalyses.getAbsolutePath(), true, true, "xml");
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Exception: Cannot extract analysis files.\nReason:" + e.getMessage());
			return;
		}

		for (File analysisFile : analysisFiles) {
			XStream xstream = new XStream();
			Analysis analysis = null;
			try {
				analysis = (Analysis) xstream.fromXML(analysisFile);
			} catch (Exception e) {
				MessageDialog.getInstance().addMessage("Cannot parse analysis for log \"" + logID + "\"");
				continue;
			}

			try {
				addAnalysisForLog(analysis, logID, false);
			} catch (Exception e) {
				MessageDialog.getInstance().addMessage("Cannot add analysis for net \"" + logID + "\"");
			}
			MessageDialog.getInstance().addMessage("Added analysis for Log\"" + analysis.getName() + "\"");
		}
	}

	//---- Store and load Analyse Patterns --------------------------------------------------------------------------------


	//---- Load and Store Time Contexts -----------------------------------------------------------------------------------

	private void loadTimeContextsFor(String netID) {
		MessageDialog.getInstance().addMessage("Loading time contexts for net \"" +netID+ "\"...");
		File pathToAnalysisContexts = null;
		try{
			pathToAnalysisContexts = new File(getPetriNetFile(netID).getParent(), SwatProperties.getInstance().getTimeContextDirectoryName());
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Exception: Cannot access time context directory.\nReason:" + e.getMessage());
			return;
		}
		
		if (!pathToAnalysisContexts.exists()){
			MessageDialog.getInstance().addMessage("No time contexts for net.");
			return;
		}
		
		List<File> contextFiles = null;
		try {
			contextFiles = FileUtils.getFilesInDirectory(pathToAnalysisContexts.getAbsolutePath(), true, true, "xml");
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Exception: Cannot extract time context files.\nReason:" + e.getMessage());
			return;
		}
		
		for (File contextFile : contextFiles) {
			TimeContext context = null;
			try {
				context = TimeContext.parse(contextFile);
			} catch (Exception e) {
				MessageDialog.getInstance().addMessage("Cannot parse time context for net \""+netID+"\"");
				continue;
			}
			
			try {
				addTimeContext(context, netID, false);
			} catch (Exception e) {
				MessageDialog.getInstance().addMessage("Cannot add time context for net \""+netID+"\"");
			}
			MessageDialog.getInstance().addMessage("Added time context \"" + context.getName() + "\"");
		}
	}
	
	private void loadNetAnalysisFor(String netID) {
		MessageDialog.getInstance().addMessage("Loading analyses for net \"" +netID+ "\"...");
		File pathToAnalyses = null;
		try{
			pathToAnalyses = new File(getPetriNetFile(netID).getParent(), SwatProperties.getInstance().getNetAnalysesDirectoryName());
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Exception: Cannot access analyses directory.\nReason:" + e.getMessage());
			return;
		}
		
		if (!pathToAnalyses.exists()){
			MessageDialog.getInstance().addMessage("No analyses for net.");
			return;
		}
		
		List<File> analysisFiles = null;
		try {
			analysisFiles = FileUtils.getFilesInDirectory(pathToAnalyses.getAbsolutePath(), true, true, "xml");
		} catch (Exception e) {
			MessageDialog.getInstance().addMessage("Exception: Cannot extract analysis files.\nReason:" + e.getMessage());
			return;
		}
		
		for (File analysisFile : analysisFiles) {
			XStream xstream = new XStream();
			Analysis analysis = null;
			try {
				analysis = (Analysis) xstream.fromXML(analysisFile);
			} catch (Exception e) {
				MessageDialog.getInstance().addMessage("Cannot parse analysis for net \""+netID+"\"");
				continue;
			}
			
			try {
				addAnalysis(analysis, netID, false);
			} catch (Exception e) {
				MessageDialog.getInstance().addMessage("Cannot add analysis for net \""+netID+"\"");
			}
			MessageDialog.getInstance().addMessage("Added analysis \"" + analysis.getName() + "\"");
		}
	}
	
	
	//---- Adding and removing Petri nets -----------------------------------------------------------------------------------
	
	@SuppressWarnings("rawtypes")
	public void addPetriNet(AbstractGraphicalPN net) throws SwatComponentException {
		File pathFile = generateNetPath(net.getPetriNet().getName());
		pathFile.mkdir();
		File netFile = generateNetFile(pathFile, net.getPetriNet().getName());
//		netFile.mkdirs();
		
		addPetriNet(net, netFile, true);
	}
	
	private File generateNetFile(File pathFile, String netID) throws SwatComponentException {
		try {
			return new File(pathFile.getCanonicalPath(), netID + ".pnml");
		} catch (IOException e) {
			throw new SwatComponentException("Cannot load properties file.<br>Reason: "+e.getMessage());
		}
	}
	
	private File generateNetPath(String netID) throws SwatComponentException {
		try {
			return new File(SwatProperties.getInstance().getPathForNets(), netID + "/");
		} catch (PropertyException e) {
			throw new SwatComponentException("Cannot extract net directory.<br>Reason: "+e.getMessage());
		} catch (IOException e) {
			throw new SwatComponentException("Cannot load properties file.<br>Reason: "+e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	private String addPetriNet(AbstractGraphicalPN net, File file, boolean storeToFile) throws SwatComponentException {
		Validate.notNull(net);
		Validate.notNull(file);
		try {
			System.out.println("file: "+ file.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String netID = net.getPetriNet().getName();
		if(nets.containsKey(netID))
			throw new SwatComponentException("SwatComponents already contains net with ID \"" + netID + "\"");
		
		nets.put(netID, net);
		netFiles.put(netID, file);
		if(storeToFile){
			storePetriNet(netID);
		}
		listenerSupport.notifyPetriNetAdded(net);
		return netID;
		
		//TODO: Listener informieren!
//		informListenerOfModelChange();
//		informNodeAdded(SwatTreeView.getInstance().new SwatTreeNode(net, SwatComponentType.PETRI_NET, file));
	}
	
	public void storeAllPetriNets() throws SwatComponentException{
		for(String netID: nets.keySet()){
			storePetriNet(netID);
			//Workbench.consoleMessage("Saved " + netID);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void storePetriNet(String netID) throws SwatComponentException {
		try {
			AbstractGraphicalPN net = getPetriNet(netID);
			String path = getPetriNetFile(netID).getCanonicalPath();
			System.out.println("net: "+net );
			System.out.println("path: "+path );
			PNSerialization.serialize(net, PNSerializationFormat.PNML, path);
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
	@SuppressWarnings("rawtypes")
	public Collection<AbstractGraphicalPN> getPetriNets(){
		return Collections.unmodifiableCollection(nets.values());
	}
	
	@SuppressWarnings("rawtypes")
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
	@SuppressWarnings("rawtypes")
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
	@SuppressWarnings("rawtypes")
	public void removePetriNet(String netID, boolean removeFilesFromDisk) throws SwatComponentException{
		validatePetriNet(netID);
		AbstractGraphicalPN netToRemove = nets.get(netID);
		File netFile = getPetriNetFile(netID);
		netFiles.remove(netID);
		nets.remove(netID);
		if(removeFilesFromDisk){
			FileUtils.deleteDirectory(FileUtils.getPath(netFile), true);
		}
		listenerSupport.notifyPetriNetRemoved(netToRemove);
		for(AnalysisContext context: getAnalysisContexts(netID)){
			listenerSupport.notifyAnalysisContextRemoved(netID, context);
		}
		analysisContexts.remove(netID);
		for(TimeContext context: getTimeContexts(netID)){
			listenerSupport.notifyTimeContextRemoved(netID, context);
		}
		timeContexts.remove(netID);
	}
	
	@SuppressWarnings("rawtypes")
	public void renamePetriNet(String oldID, String newID) throws SwatComponentException{
		validatePetriNet(oldID);
		AbstractGraphicalPN newNet = getPetriNet(oldID);
		newNet.getPetriNet().setName(newID);
		File newFile = getPetriNetFile(oldID);
		nets.remove(oldID);
		netFiles.remove(oldID);
		nets.put(newID, newNet);
		netFiles.put(newID, newFile);
		storePetriNet(newID);
		listenerSupport.notifyPetriNetRenamed(nets.get(newID));
	}

	private void validatePetriNet(String netID) throws SwatComponentException{
		if(!nets.containsKey(netID))
			throw new SwatComponentException("SwatComponents does not contain a net with name \"" + netID + "\"");
		if(!netFiles.containsKey(netID))
			throw new SwatComponentException("SwatComponents does not contain a file reference for net \"" + netID + "\"");
	}
	
	
	//---- Adding and removing Logs -----------------------------------------------------------------------------------------
	
	public void addAristaFlowLog(File file, String name) throws SwatComponentException{
		try {
			File destFile = new File(String.format(CSVLogNameFormat, SwatProperties.getInstance().getPathForLogs(), name));
			FileUtils.copy(file, destFile);
			addLogModel(new LogModel(destFile, SwatLog.Aristaflow), true);
		} catch (PropertyException e) {
			throw new SwatComponentException("Cannot extract log directory.\nReason: " + e.getMessage());
		} catch (IOException e) {
			throw new SwatComponentException("Cannot access log directory.\nReason: " + e.getMessage());
		}
	}
	

	public LogModel storeLogModelAs(LogModel model, String LogModelName) {
		try {
			int i = 0;
			while (getLogModel(LogModelName) != null) {
				LogModelName += ++i;
				model.setName(LogModelName);
			}

			File pathForLogModel = new File(SwatProperties.getInstance().getPathForLogs(), LogModelName);
			pathForLogModel.mkdirs();
			String fileSuffix = FileUtils.getExtension(model.getFileReference());
			File newLog = new File(pathForLogModel, LogModelName +"." +fileSuffix);
			FileUtils.copy(model.getFileReference(), newLog);
			LogModel newModel = new LogModel(newLog, model.getType());
			return newModel;
		} catch (PropertyException e) {
			JOptionPane.showMessageDialog(Workbench.getInstance(), "Could not retrieve path for log.\nReason: " + e.getMessage());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(Workbench.getInstance(), "Could not store log.\nReason: " + e.getMessage());
		}
		return model;
	}
	
	public void addLogModel(LogModel model){
		addLogModel(model, true);
	}
	
	private void addLogModel(LogModel model, boolean sort) {
		switch (model.getType()) {
		case Aristaflow:
			aristaLogs.add(model);
			if (sort)
				Collections.sort(aristaLogs, new LogModelComparator());
			break;
		case MXML:
			mxmlLogs.add(model);
			if (sort)
				Collections.sort(mxmlLogs, new LogModelComparator());
			break;
		case XES:
			xesLogs.add(model);
			if (sort)
				Collections.sort(xesLogs, new LogModelComparator());
			break;
		}
	listenerSupport.notifyLogAdded(model);
	}

	private void sortLogModelLists() {
		for (SwatLog logType : SwatLog.values())
			sortLogModelList(logType);
	}

	private void sortLogModelList(SwatLog type) {
		switch (type) {
		case Aristaflow:
			Collections.sort(aristaLogs, new LogModelComparator());
			break;
		case MXML:
			Collections.sort(mxmlLogs, new LogModelComparator());
			break;
		case XES:
			Collections.sort(xesLogs, new LogModelComparator());
			break;
		}
	}
	
	public List<LogModel> getLogs(SwatLog type) {
		switch(type){
		case Aristaflow:
			return Collections.unmodifiableList(aristaLogs);
		case MXML:
			return Collections.unmodifiableList(mxmlLogs);
		case XES:
			return Collections.unmodifiableList(xesLogs);
		}
		return null;
	}
	
	public void removeLogFile(String logName, boolean deleteFromDisk) {
		LogModel logToDelete = getLogModel(logName);
		
		if (logToDelete == null)
			return;
		
		removeLogFromList(logName);

		if (deleteFromDisk) {
			FileUtils.deleteDirectory(logToDelete.getFileReference().getParent(), true, false);
		}

		listenerSupport.notifyLogRemoved(logToDelete);

	}

	public LogModel getLogModel(String logName) {
		for(LogModel model:xesLogs){
			if (model.getName().equals(logName))
				return model;
		}

		for (LogModel model : aristaLogs) {
			if (model.getName().equals(logName))
				return model;
		}

		for (LogModel model : mxmlLogs) {
			if (model.getName().equals(logName))
			return model;
		}

		return null;
	}

	public List<LogModel> getLogs() {
		LinkedList<LogModel> result = new LinkedList<LogModel>();
		result.addAll(aristaLogs);
		result.addAll(mxmlLogs);
		result.addAll(xesLogs);
		return result;
	}

	private void removeLogFromList(String logName) {
		LogModel modelToRemove = getLogModel(logName);
		aristaLogs.remove(modelToRemove);
		xesLogs.remove(modelToRemove);
		mxmlLogs.remove(modelToRemove);
	}

	public void renameLog(String oldID, String newID) throws SwatComponentException {
		LogModel oldModel = getLogModel(oldID);
		addLogModel(storeLogModelAs(getLogModel(oldID), newID));
		removeLogFile(oldID, true);
		listenerSupport.notifyLogRenamed(oldModel, getLogModel(newID));
	}

	public boolean containsLogWithID(String logID) {
		return (getLogModel(logID) != null);
	}

	//---- Adding and removing analyses -------------------------------------------------------------------------------------
	
	public void addAnalysis(Analysis analysis, String netID, boolean storeToFile) throws SwatComponentException {
		System.out.println("Adding " + analysis.getName());
		if (!analyses.containsKey(netID) || analyses.get(netID) == null)
			analyses.put(netID, new ArrayList<Analysis>());

		//test if this analysis is already present. Remove if so
		for (Analysis availableAnalyses : getAnalyses(netID)) {
			if (availableAnalyses.getName().equalsIgnoreCase(analysis.getName())) {
				getAnalyses(netID).remove(availableAnalyses);
				break;
			}
		}

		analyses.get(netID).add(analysis);
		if(storeToFile){
			storeAnalysis(analysis, netID);
		}
		listenerSupport.notifyAnalysisAdded(netID, analysis);
	}
	
	public void addAnalysisForLog(Analysis analysis, String logID, boolean storeToFile) throws SwatComponentException {
		System.out.println("Adding " + analysis.getName());
		if (!analyses.containsKey(logID) || !containsLogWithID(logID))
			analyses.put(logID, new ArrayList<Analysis>()); //create new entry

		//put analysis in HashMap
		analyses.get(logID).add(analysis);

		//test if this analysis is already present. Remove if so.
		for (Analysis availableAnalyses : getAnalyses(logID)) {
			if (availableAnalyses.getName().equalsIgnoreCase(analysis.getName())) {
				getAnalyses(logID).remove(availableAnalyses);
				break;
			}
		}

		if (storeToFile) {
			storeAnalysisForLog(analysis, logID);
		}
		listenerSupport.notifyAnalysisAdded(logID, analysis);

	}

	public void storeAnalysis(Analysis analysis, String netID) throws SwatComponentException {
		System.out.println("Storing " + analysis.getName());
		try {
			File storagePath = new File(getPetriNetFile(netID).getParent(), SwatProperties.getInstance().getNetAnalysesDirectoryName());
			storagePath.mkdirs();
			XStream xstream=new XStream();
			String serialString = xstream.toXML(analysis);
			PrintWriter writer = new PrintWriter(new File(storagePath.getAbsolutePath(), analysis.getName() + ".xml"));
			//PrintWriter writer = new PrintWriter(String.format(AnalysisNameFormat, storagePath.getAbsolutePath(), analysis.getName()));
			writer.write(serialString);
			writer.checkError();
			writer.close();
		} catch (Exception e) {
			throw new SwatComponentException("Cannot store analysis \"" + analysis.getName() + "\".\nReason: "+e.getMessage());
		}
	}
	
	public void storeAnalysisForLog(Analysis analysis, String logID) throws SwatComponentException {
		try {
			System.out.println("Storing Log" + analysis.getName());
		File storagePath = new File(getLogModel(logID).getFileReference().getParent(), SwatProperties.getInstance()
				.getNetAnalysesDirectoryName());
		storagePath.mkdirs();
		XStream xstream = new XStream();
		String serialString = xstream.toXML(analysis);
		PrintWriter writer = new PrintWriter(new File(storagePath.getAbsolutePath(), analysis.getName() + ".xml"));
		writer.write(serialString);
		writer.checkError();
		writer.close();
		}
		catch (Exception e) {
			throw new SwatComponentException("Cannot store analysis \"" + analysis.getName() + "\".\nReason: "+e.getMessage());
		}

	}

	private void loadAnalyses() {
		for (String netID : nets.keySet()) {
			MessageDialog.getInstance().addMessage("Loading analyses for: " + netID);
			loadNetAnalysisFor(netID);
		}

		//logfiles

		for (LogModel model : getLogs()) {
			loadLogAnalysisFor(model.getName());
		}

	}
	
	private void removeAnalysis(String netID, String analysisName, boolean fromFileSystem) throws SwatComponentException {
		// build path were it was stored
		if (fromFileSystem) {
			File analysisFile = new File(getPetriNetFile(netID).getParentFile(), analysisName + ".xml");
			if (!analysisFile.exists())
				throw new SwatComponentException("Could not resolve analysis file for " + analysisName);
			FileUtils.removeLinkOnly(analysisFile);
		}

		analyses.remove(getAnalysis(netID, analysisName));
	}

//	public void renameAnalysis(String netID, String oldName, String newName){
//		Analysis oldAnalysis = getAnalysis(netID, oldName);
//		Analysis newAnalysis=new Analysis(newName, oldAnalysis.getPatternSetting());
//		try {
//			addAnalysis(newAnalysis, netID, true);
//			removeAnalysis(netID, oldName, true);
//			listenerSupport.notifyAnalysisRemoved(netID, oldAnalysis);
//			listenerSupport.notifyAnalysisAdded(netID, newAnalysis);
//		} catch (SwatComponentException e) {
//			Workbench.errorMessage("Could not remove " + oldName + ": Could not resolve file");
//			JOptionPane.showMessageDialog(Workbench.getInstance(), "Could not remove " + oldName + ": Could not resolve file");
//			e.printStackTrace();
//		}
//	}
	
	public List<Analysis> getAnalyses(String netID){
		if (!analyses.containsKey(netID))
			return new ArrayList<Analysis>();
		return analyses.get(netID);
	}

	public Analysis getAnalysis(String netID, String analysisName) {
		if (!analyses.containsKey(netID))
			return null;

		for (Analysis analysis : analyses.get(netID)) {
			if (analysis.getName().equals(analysisName))
				return analysis;
		}
		return null;
	}

	public Analysis getAnalysisByName(String analysisName) {
		for (List<Analysis> analysises : analyses.values()) {
			for (Analysis analysis : analysises) {
				if (analysis.getName().equalsIgnoreCase(analysisName))
					return analysis;
			}
		}
		return null;
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
	
	public AnalysisContext getAnalysisContext(String aContextName) {
		for(String netID: analysisContexts.keySet()){
			for(AnalysisContext context: analysisContexts.get(netID)){
				if(context.getName().equals(aContextName))
					return context;
			}
		}
		return null;
	}
	
	public boolean containsAnalysisContext(String aContextName) {
		return getAnalysisContext(aContextName) != null;
	}

	public void addAnalysisContext(AnalysisContext aContext, String netID, boolean storeToFile) throws SwatComponentException {
		if(analysisContexts.get(netID) == null)
			analysisContexts.put(netID, new ArrayList<AnalysisContext>());
		analysisContexts.get(netID).add(aContext);
		if(storeToFile){
			storeAnalysisContext(aContext, netID);
		}
		listenerSupport.notifyAnalysisContextAdded(netID, aContext);
	}
	
	public void removeAnalysisContext(String analysisContextName, String netID, boolean removeFilesFromDisk) throws SwatComponentException{
		validatePetriNet(netID);
		validateAnalysisContext(analysisContextName);
		if(!analysisContexts.containsKey(netID))
			return;
		AnalysisContext contextToRemove = null;
		for(AnalysisContext context: analysisContexts.get(netID)){
			if(context.getName().equals(analysisContextName)){
				contextToRemove = context;
				break;
			}
		}
		analysisContexts.get(netID).remove(contextToRemove);
		if(analysisContexts.get(netID).isEmpty())
			analysisContexts.remove(netID);
		
		if(removeFilesFromDisk){
			try {
				FileUtils.deleteFile(new File(getPetriNetFile(netID).getParent(), SwatProperties.getInstance().getAnalysisContextDirectoryName()) + analysisContextName + ".acon");
			} catch (IOException e) {
				throw new SwatComponentException("Cannot delete analysis context file from disk.<br>Reason: " + e.getMessage());
			}
		}

		if(labelings.containsKey(analysisContextName) && removeFilesFromDisk){
			for(Labeling labeling: labelings.get(analysisContextName)){
				try {
					FileUtils.deleteFile(new File(getPetriNetFile(netID).getParent(), SwatProperties.getInstance().getAnalysisContextDirectoryName()) + labeling.getName() + ".labeling");
				} catch (IOException e) {
					throw new SwatComponentException("Cannot delete analysis context file from disk.<br>Reason: " + e.getMessage());
				}
			}
		}
		labelings.remove(analysisContextName);
		listenerSupport.notifyAnalysisContextRemoved(netID, contextToRemove);
	}

	public void storeAnalysisContext(AnalysisContext aContext, String netID) throws SwatComponentException {
		try {
			File pathToAnalysisContexts = new File(getPetriNetFile(netID).getParent(), SwatProperties.getInstance().getAnalysisContextDirectoryName());
			pathToAnalysisContexts.mkdirs();
			AnalysisContextSerialization.serialize(aContext, pathToAnalysisContexts.getAbsolutePath() + aContext.getName());
		} catch (SerializationException e) {
			throw new SwatComponentException("Cannot serialize analysis context \"" + aContext.getName() + "\".<br>Reason: "+e.getMessage());
		} catch (IOException e) {
			throw new SwatComponentException("Cannot store analysis context \"" + aContext.getName() + "\".<br>Reason: "+e.getMessage());
		}
	}
	
	private void validateAnalysisContext(String analysisContextName) throws SwatComponentException{
		if(!containsAnalysisContext(analysisContextName))
			throw new SwatComponentException("SwatComponents does not contain an analysis context with name \"" + analysisContextName + "\"");
	}
	
	//---- Adding and removing time contexts --------------------------------------------------------------------------------
	
	public List<TimeContext> getTimeContexts(String netID) {
		if(!timeContexts.containsKey(netID))
			return new ArrayList<TimeContext>();
		return timeContexts.get(netID);
	}
	
	public TimeContext getTimeContext(String netID, String aContextName) {
		for(TimeContext aContext: getTimeContexts(netID)){
			if(aContext.getName().equals(aContextName))
				return aContext;
		}
		return null;
	}

	public void addTimeContext(TimeContext aContext, String netID, boolean storeToFile) throws SwatComponentException {
		if(timeContexts.get(netID) == null)
		timeContexts.put(netID, new ArrayList<TimeContext>());
		timeContexts.get(netID).add(aContext);
		if(storeToFile){
			storeTimeContext(aContext, netID);
		}
	}

	public void storeTimeContext(TimeContext aContext, String netID) throws SwatComponentException {
		try {
			File pathToTimeContexts = new File(getPetriNetFile(netID).getParent(), SwatProperties.getInstance().getTimeContextDirectoryName());
			pathToTimeContexts.mkdirs();
			XStream xstream = new XStream();
			xstream.autodetectAnnotations(true);
			String serialString = xstream.toXML(aContext);
			PrintWriter writer = new PrintWriter(pathToTimeContexts.getAbsolutePath() + "/" + aContext.getName() + ".xml");
			writer.write(serialString);
			writer.checkError();
			writer.close();
		} catch (Exception e) {
			throw new SwatComponentException("Cannot store time context \"" + aContext.getName() + "\".\nReason: "+e.getMessage());
		}
	}

	//---- Adding and removing Contexts -------------------------------------------------------------------------------------

	/**
	 * Adds a new context.<br>
	 * 
	 * @param soaBase The context to add.
	 * @throws SwatComponentException
	 */
	public void addContext(SOABase soaBase) throws SwatComponentException {
		addContext(soaBase, true);
	}

	/**
	 * Adds a new context.<br>
	 * Depending on the value of the store-parameter, the model is also stores as property-file in the simulation directory.
	 * 
	 * 
	 * @param soaBase The context to add.
	 * @param storeToFile Indicates if the context should be stored to disk.
	 * @throws SwatComponentException
	 */
	public void addContext(SOABase soaBase, boolean storeToFile) throws SwatComponentException {
		Validate.notNull(soaBase);
		Validate.notNull(storeToFile);
		contexts.put(soaBase.getName(), soaBase);
		if (storeToFile) {
			storeContext(soaBase);
		}
		listenerSupport.notifyContextAdded(soaBase);
	}

	/**
	 * Stores the given context in form of a property-file in the working directory.<br>
	 * 
	 * @param soaBase The context to store.
	 * @throws SwatComponentException
	 */
	public void storeContext(SOABase soaBase) throws SwatComponentException {
		Validate.notNull(soaBase);
		try {
			File pathToStore = new File(SwatProperties.getInstance().getPathForContexts(), soaBase.getName());
			soaBase.getProperties().store(pathToStore.getAbsolutePath());
		} catch (PropertyException e) {
			throw new SwatComponentException("Cannot extract context path.<br>Reason: " + e.getMessage());
		} catch (IOException e) {
			throw new SwatComponentException("Cannot store context file to disk.<br>Reason: " + e.getMessage());
		}
	}

	/**
	 * Checks, if there are context-components.
	 * 
	 * @return <code>true</code> if there is at least one context;<br>
	 *         <code>false</code> otherwise.
	 */
	public boolean containsContexts() {
		return !contexts.isEmpty();
	}

	/**
	 * Checks, if there is a context with the given name.
	 * 
	 * @return <code>true</code> if there is such a context;<br>
	 *         <code>false</code> otherwise.
	 */
	public boolean containsContext(String name) {
		return contexts.get(name) != null;
	}

	/**
	 * Returns all contexts, i.e. models stored in the working directory.
	 * 
	 * @return A set containing all contexts.
	 */
	public Collection<SOABase> getContexts() {
		return Collections.unmodifiableCollection(contexts.values());
	}

	/**
	 * Returns the context with the given name, if there is one.
	 * 
	 * @param name The name of the desired context.
	 * @return The context with the given name, or <code>null</code> if there is no such model.
	 */
	public SOABase getContext(String name) throws ParameterException {
		Validate.notNull(name);
		return contexts.get(name);
	}

	/**
	 * Returns the names of all contexts.
	 */
	public Set<String> getContextNames() {
		return Collections.unmodifiableSet(contexts.keySet());
	}

	/**
	 * Removes the context with the given name from the swat components<br>
	 * and also deletes the corresponding property-file in the working directory.
	 * 
	 * @param name The name of the context to remove.
	 */
	public void removeContext(String name, boolean removeFileFromDisk) throws SwatComponentException {
		validateContext(name);
		SOABase contextToRemove = contexts.get(name);
		contexts.remove(name);

		if (removeFileFromDisk) {
			try {
				FileUtils.deleteFile(SwatProperties.getInstance().getPathForContexts() + name);
			} catch (PropertyException e) {
				throw new SwatComponentException("Cannot extract context path.<br>Reason: " + e.getMessage());
			} catch (IOException e) {
				throw new SwatComponentException("Cannot delete context file from disk.<br>Reason: " + e.getMessage());
			}
		}
		listenerSupport.notifyContextRemoved(contextToRemove);
	}

	private void validateContext(String name) throws SwatComponentException {
		if (!contexts.containsKey(name))
			throw new SwatComponentException("SwatComponents does not contain a context with name \"" + name + "\"");
	}
	
	//---- Adding and removing AC models ------------------------------------------------------------------------------------
	
	/**
	 * Adds a new access control model.<br>
	 * @param acModel The model to add.
	 * @throws SwatComponentException
	 */
	@SuppressWarnings("rawtypes")
	public void addACModel(AbstractACModel acModel) throws SwatComponentException {
		addACModel(acModel, true);
	}
	
	/**
	 * Adds a new access control model.<br>
	 * Depending on the value of the store-parameter, the model is also stores as property-file in the simulation directory.
	 * @param acModel The new model to add.
	 * @param storeToFile Indicates if the model should be stored on disk.
	 * @throws SwatComponentException
	 */
	@SuppressWarnings("rawtypes")
	public void addACModel(AbstractACModel acModel, boolean storeToFile) throws SwatComponentException {
		Validate.notNull(acModel);
		Validate.notNull(storeToFile);
		acModels.put(acModel.getName(), acModel);
		if(storeToFile){
			storeACModel(acModel);
		}
		listenerSupport.notifyACModelAdded(acModel);
	}
	
	/**
	 * Stores the given access control model in form of a property-file in the simulation directory.<br>
	 * The context name will be used as file name.
	 * @param acModel The model to store.
	 * @throws SwatComponentException
	 */
	@SuppressWarnings("rawtypes")
	public void storeACModel(AbstractACModel acModel) throws SwatComponentException {
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
	@SuppressWarnings("rawtypes")
	public Collection<AbstractACModel> getACModels(){
		return Collections.unmodifiableCollection(acModels.values());
	}
	
	/**
	 * Returns the access control model with the given name, if there is one.
	 * @param name The name of the desired access control model.
	 * @return The model with the given name, or <code>null</code> if there is no such model.
	 */
	@SuppressWarnings("rawtypes")
	public AbstractACModel getACModel(String name) throws ParameterException{
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
	@SuppressWarnings("rawtypes")
	public void removeACModel(String name, boolean removeFileFromDisk) throws SwatComponentException {
		validateACModel(name);
		AbstractACModel modelToRemove = acModels.get(name);
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
		listenerSupport.notifyACModelRemoved(modelToRemove);
	}

	private void validateACModel(String name) throws SwatComponentException{
		if(!acModels.containsKey(name))
			throw new SwatComponentException("SwatComponents does not contain an access control model with name \"" + name + "\"");
	}
	
	
	//---- Adding and removing labelings ------------------------------------------------------------------------------------

	public void addLabeling(Labeling labeling, String netID, String analysisContextName, boolean storeToFile) throws SwatComponentException {
		if(labelings.get(analysisContextName) == null)
			labelings.put(analysisContextName, new ArrayList<Labeling>());
		labelings.get(analysisContextName).add(labeling);
		if(storeToFile){
			storeLabeling(labeling, netID, analysisContextName);
		}
		listenerSupport.notifyLabelingAdded(netID, analysisContextName, labeling);
	}

	public void storeLabeling(Labeling labeling, String netID, String analysisContextName) throws SwatComponentException {
		try {
			File pathToLabelings = new File(getPetriNetFile(netID).getParent(), SwatProperties.getInstance().getAnalysisContextDirectoryName());
			pathToLabelings.mkdirs();
			LabelingSerialization.serialize(labeling, pathToLabelings.getAbsolutePath() + labeling.getName());
		} catch (SerializationException e) {
			throw new SwatComponentException("Cannot serialize labeling \"" + labeling.getName() + "\".<br>Reason: "+e.getMessage());
		} catch (IOException e) {
			throw new SwatComponentException("Cannot store labeling \"" + labeling.getName() + "\".<br>Reason: "+e.getMessage());
		}
	}
	
	public List<Labeling> getLabelings(String analysisContextName){
		if (!labelings.containsKey(analysisContextName))
			return new ArrayList<Labeling>();
		return labelings.get(analysisContextName);
	}
	
//	private Labeling getLabeling(String analysisContextName, String labelingName) {
//		for(Labeling labeling: getLabelings(analysisContextName)){
//			if(labeling.getName().equals(labelingName))
//				return labeling;
//		}
//		return null;
//	}
//	
	private Labeling getLabelingByName(String labelingName) {
		for (List<Labeling> labelingList : labelings.values()) {
			for (Labeling labeling : labelingList) {
				if (labeling.getName().equals(labelingName))
					return labeling;
			}
		}
		return null;
	}
	
	public boolean containsLabeling(String labelingName) {
		return getLabelingByName(labelingName) != null;
	}
	
	public void removeLabeling(String labelingName, String netID, String analysisContextName, boolean removeFileFromDisk) throws SwatComponentException{
		validateLabeling(labelingName);
		validateAnalysisContext(analysisContextName);
		if(!labelings.containsKey(analysisContextName))
			return;
		labelings.get(analysisContextName).remove(labelingName);
		if(labelings.get(analysisContextName).isEmpty())
			labelings.remove(analysisContextName);
		if(removeFileFromDisk){
			try {
				FileUtils.deleteFile(new File(getPetriNetFile(netID).getParent(), SwatProperties.getInstance().getAnalysisContextDirectoryName()) + labelingName + ".labeling");
			} catch (IOException e) {
				throw new SwatComponentException("Cannot delete labeling file from disk.<br>Reason: " + e.getMessage());
			}
		}
	}
	
	private void validateLabeling(String labelingName) throws SwatComponentException{
		if(!containsLabeling(labelingName))
			throw new SwatComponentException("SwatComponents does not contain a labeling with name \"" + labelingName + "\"");
	}
	
	//---- Adding and removing analyses ----------------------------------------------------------------------------



	//	/** get analyses associated with net netID **/
	//	public List<String> getAnalysesNames(String netID) {
	//		ArrayList<String> names = new ArrayList<String>();
	//		for (Analysis analysis : analyses.get(netID))
	//			names.add(analysis.getName());
	//		return names;
	//	}

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


//
//	/**
//	 * get File that belongs to currentComponent
//	 * 
//	 * @throws ParameterException
//	 *             if not found
//	 **/
//	public File getFile(SwatComponent currentComponent) throws ParameterException {
//		// Traverse all lists
//		File file;
//		//nets
//		if (currentComponent instanceof PNEditor) {
//			for (String pnet : nets.keySet()) {
//				if (pnet == ((PNEditor) currentComponent).getNetContainer().getPetriNet().getName())
//					return netFiles.get(pnet);
//			}
//		}
//
//		//csv file
//		if (currentComponent instanceof LogFileViewer) {
//			for (XESLogModel model : aristaLogs.keySet()) {
//				if (model == ((LogFileViewer) currentComponent).getModel())
//						return aristaLogs.get(model);
//			}
//
//		}
//		//xml file
//		if (currentComponent instanceof XMLFileViewer) {
//			for (XESLogModel model : xmlLogs.keySet()) {
//				if (model == ((XMLFileViewer) currentComponent).getModel())
//					return aristaLogs.get(model);
//			}
//
//		}
//		throw new ParameterException("not a valid SwatComponent");
//	}
	

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

	
//	public void putLogAnalysisIntoSwatComponent(LogAnalysisModel model, XESLogModel correspondingLog){
//		if(logAnalysis.containsKey(correspondingLog))
//			logAnalysis.get(correspondingLog).add(model);//put into list
//		ArrayList<LogAnalysisModel> list = new ArrayList<LogAnalysisModel>();
//		list.add(model);
//		logAnalysis.put(correspondingLog, list);
//		storeLogAnalysis(model, correspondingLog);
//	}


//	public List<LogAnalysisModel> getAnalysisForLog(XESLogModel model) {
//		return logAnalysis.get(model);
//	}
//	
//
//	public TimeContext getTimeAnalysisForNet(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> cur_net) {
//		String name = cur_net.getPetriNet().getName();
//		TimeContext context = timeContext.get(name);
//		return context;
//	}
}