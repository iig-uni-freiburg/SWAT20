package de.uni.freiburg.iig.telematik.swat.workbench.properties;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import de.invation.code.toval.misc.ArrayUtils;
import de.invation.code.toval.misc.StringUtils;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory.IconSize;
import de.uni.freiburg.iig.telematik.swat.prism.searcher.PrismSearcher;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatPropertyChangeListener;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangProperties;


public class SwatProperties extends WolfgangProperties{
	
	protected static final String defaultWorkingDirectory = ".";
	public static final String defaultWorkingDirectoryName = "SwatWorkingDirectory";
	
	protected static final String pathNets = "nets/";
	protected static final String pathLogs = "logs/";
	protected static final String pathACModels = "acModel/";
	protected static final String pathContexts = "contexts/";
	protected static final String pathNameAnalysisContext = "analysis-contexts";
	protected static final String pathNameTimeContext = "time-contexts";
	protected static final String pathNameNetAnalyses = "analysis/";
	
	protected static final String propertyFileName = "SwatProperties";
	
	private static SwatProperties instance = null;
	
	private String applicationPath = null;
	
	private Set<SwatPropertyChangeListener> listeners = new HashSet<SwatPropertyChangeListener>();

	public SwatProperties() throws IOException {
		try {
			load(propertyFileName);
		} catch (IOException e) {
			// Create new property file.
			loadDefaultProperties();
			store();
		}
		// get current working dir
		applicationPath = new File (".").getCanonicalPath();
	}
	
	public String getApplicationPath(){
		return applicationPath;
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException if there is no property file and a new one cannot be created.
	 */
	public static SwatProperties getInstance() throws IOException {
		if(instance == null){
			instance = new SwatProperties();
		}
		return instance;
	}
	
	//------- Property setting -------------------------------------------------------------
	
	private void setProperty(SwatProperty property, Object value){
		props.setProperty(property.toString(), value.toString());
	}
	
	private String getProperty(SwatProperty property){
		return props.getProperty(property.toString());
	}
	
	private void removeProperty(SwatProperty property){
		props.remove(property.toString());
	}
	
	//------- Working Directory ------------------------------------------------------------
	
	public void setWorkingDirectory(String directory, boolean reloadComponents) throws SwatComponentException {
		validateWorkingDirectory(directory, false);
		setProperty(SwatProperty.WORKING_DIRECTORY, directory);
		
		File directoryFile = new File(directory);
		if(!directoryFile.exists()){
			directoryFile.mkdir();
		}

		if(reloadComponents)
			SwatComponents.getInstance().reload();
	}
	
	public String getWorkingDirectory() throws PropertyException, ParameterException {
		String propertyValue = getProperty(SwatProperty.WORKING_DIRECTORY);
		if(propertyValue == null)
			throw new PropertyException(SwatProperty.WORKING_DIRECTORY, propertyValue);
		validatePath(propertyValue);
		return propertyValue;
	}
	
	public void removeWorkingDirectory(){
		removeProperty(SwatProperty.WORKING_DIRECTORY);
	}

	//------- Known Working Directories ----------------------------------------------------
	
	public void addKnownWorkingDirectory(String workingDirectory, boolean createSubdirectories){
		validateWorkingDirectory(workingDirectory, createSubdirectories);
		Set<String> currentDirectories = getKnownWorkingDirectories();
		currentDirectories.add(workingDirectory);
		setProperty(SwatProperty.KNOWN_WORKING_DIRECTORIES, ArrayUtils.toString(prepareWorkingDirectories(currentDirectories)));
	}
	
	public void removeKnownWorkingDirectory(String simulationDirectory){
		validateStringValue(simulationDirectory);
		Set<String> currentDirectories = getKnownWorkingDirectories();
		currentDirectories.remove(simulationDirectory);
		setProperty(SwatProperty.KNOWN_WORKING_DIRECTORIES, ArrayUtils.toString(prepareWorkingDirectories(currentDirectories)));
	}

	private String[] prepareWorkingDirectories(Set<String> directories){
		String[] result = new String[directories.size()];
		int count = 0;
		for(String directory: directories)
			result[count++] = "'"+directory+"'";
		return result;
	}
	
	public Set<String> getKnownWorkingDirectories(){
		Set<String> result = new HashSet<String>();
		String propertyValue = getProperty(SwatProperty.KNOWN_WORKING_DIRECTORIES);
		if(propertyValue == null)
			return result;
		return new HashSet<String>(StringUtils.splitArrayStringQuoted(propertyValue, '\''));
	}

	//-- Simulation component paths (not stored in property file)
	
	public String getPathForContexts() throws PropertyException{
		return getWorkingDirectory().concat(pathContexts);
	}
	
	public String getPathForNets() throws PropertyException{
		return getWorkingDirectory().concat(pathNets);
	}
	
	public String getPathForLogs() throws PropertyException{
		return getWorkingDirectory().concat(pathLogs);
	}
	
	public String getPathForACModels() throws PropertyException{
		return getWorkingDirectory().concat(pathACModels);
	}
	
	public String getNetAnalysesDirectoryName(){
		return pathNameNetAnalyses;
	}
	
	public String getAnalysisContextDirectoryName(){
		return pathNameAnalysisContext;
	}
	
	public String getTimeContextDirectoryName(){
		return pathNameTimeContext;
	}
	
	//------- PNML Parser properties -------------------------------------------------------
	
	public void setRequestNetType(boolean requestNetType) throws ParameterException{
		Validate.notNull(requestNetType);
		setProperty(SwatProperty.REQUIRE_NET_TYPE, requestNetType);
	}
	
	public boolean getRequestNetType() throws PropertyException{
		String propertyValueRequestNetType = getProperty(SwatProperty.REQUIRE_NET_TYPE);
		
		Boolean requestNetType = null;
		try{
			requestNetType = Boolean.valueOf(propertyValueRequestNetType);
		}catch(Exception e){
			throw new PropertyException(SwatProperty.REQUIRE_NET_TYPE, propertyValueRequestNetType);
		}
		return requestNetType;
	}
	
	public void setPNValidation(boolean pnValidation) throws ParameterException{
		Validate.notNull(pnValidation);
		setProperty(SwatProperty.VERIFY_PNML_SCHEMA, pnValidation);
	}
	
	public boolean getPNValidation() throws PropertyException{
		String propertyValueValidation = getProperty(SwatProperty.VERIFY_PNML_SCHEMA);
		
		Boolean validation = null;
		try{
			validation = Boolean.valueOf(propertyValueValidation);
		}catch(Exception e){
			throw new PropertyException(SwatProperty.VERIFY_PNML_SCHEMA, propertyValueValidation);
		}
		return validation;
	}
	
	//------- Prism Path -------------------------------------------------------------------
	
	public void setPrismPath(String directory) throws ParameterException, IOException, PropertyException {
		PrismSearcher.validatePrismPath(directory);
		setProperty(SwatProperty.PRISM_PATH, directory);
		store();
	}
	
	public String getPrismPath() throws PropertyException, ParameterException {
		String propertyValue = getProperty(SwatProperty.PRISM_PATH);
		if(propertyValue == null)
			throw new PropertyException(SwatProperty.PRISM_PATH, propertyValue);
		PrismSearcher.validatePrismPath(propertyValue);
		return propertyValue;
	}
	
	//------- LoLA Path -------------------------------------------------------------------

	public void setLolaPath(String directory) throws IOException {
		//LolaSearcher.validatePrismPath(directory);
		setProperty(SwatProperty.LOLA_PATH, directory);
		store();
	}

	public String getLolaPath() throws PropertyException, ParameterException {
		String propertyValue = getProperty(SwatProperty.LOLA_PATH);
		if (propertyValue == null)
			throw new PropertyException(SwatProperty.LOLA_PATH, propertyValue);
		return propertyValue;
	}

	//------- SCIFF Path -------------------------------------------------------------------

	public void setSciffPath(String directory) throws ParameterException, IOException, PropertyException {
		//PrismSearcher.validatePrismPath(directory);
		setProperty(SwatProperty.SCIFF_PATH, directory);
		store();
	}

	public String getSciffPath() throws PropertyException, ParameterException {
		String propertyValue = getProperty(SwatProperty.SCIFF_PATH);
		if (propertyValue == null)
			throw new PropertyException(SwatProperty.SCIFF_PATH, propertyValue);
		//PrismSearcher.validatePrismPath(propertyValue);
		return propertyValue;
	}

	//------- Validation -------------------------------------------------------------------
	
	public static void validateWorkingDirectory(String directory, boolean createSubdirectories) throws ParameterException{
		validatePath(directory);
		checkSubDirectory(directory, pathNets, createSubdirectories);
		checkSubDirectory(directory, pathLogs, createSubdirectories);
		checkSubDirectory(directory, pathACModels, createSubdirectories);
		checkSubDirectory(directory, pathContexts, createSubdirectories);
	}
	
	private static void checkSubDirectory(String workingDirectory, String subDirectoryName, boolean ensureSubdirectory){
		File dir = new File(workingDirectory + subDirectoryName);
		if(!dir.exists()){
			if(!ensureSubdirectory)
				throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Corrupt structure of swat working directory:\n"+dir.getAbsolutePath());
			dir.mkdir();
		}
	}
	
	// ------- Icon Size--------------------------------------------------------------------
//	@Override
//	public IconSize getIconSize() throws PropertyException {
//		String propertyValue = getProperty(SwatProperty.ICON_SIZE);
//		if (propertyValue == null || propertyValue.equals("")) {
//			IconSize result = IconSize.valueOf("MEDIUM");
//			return result;
//			//throw new PropertyException(SwatProperty.ICON_SIZE, propertyValue);
//		}
//		try {
//			IconSize result = IconSize.valueOf(propertyValue);
//			return result;
//		} catch (Exception e) {
//			throw new PropertyException(SwatProperty.ICON_SIZE, propertyValue);
//		}
//	}
//	
//	public void setIconSize(IconSize size) throws PropertyException, ParameterException {
//		Validate.notNull(size);
//		setProperty(SwatProperty.ICON_SIZE, size.toString());
//	}
	
	//-------  SQL Properties --------------------------------------------------------------
	
	public String getAristaFlowURL() {
		String result = getProperty(SwatProperty.ARISTA_FLOW_URL);
		if (result == null || result == "")
			result = "jdbc:postgresql://127.0.0.1:5432/InvoiceLocal";
		return result;
	}
	
	public void setAristaFlowURL(String url) {
		Validate.notNull(url);
		setProperty(SwatProperty.ARISTA_FLOW_URL, url);
	}

	public String getAristaFlowUser() {
		String result = getProperty(SwatProperty.ARISTA_FLOW_USER);
		if (result == null || result == "")
			result = "ADEPT2";
		return result;
	}

	public void setAristaFlowUser(String user) {
		Validate.notNull(user);
		setProperty(SwatProperty.ARISTA_FLOW_USER, user);
	}

	public String getAristaFlowPass() {
		String result = getProperty(SwatProperty.ARISTA_FLOW_PASS);
		if (result == null || result == "")
			result = "ADEPT2DB";
		return result;
	}

	public void setAristaFlowPass(String pass) {
		Validate.notNull(pass);
		setProperty(SwatProperty.ARISTA_FLOW_PASS, pass);
	}

	//--------------------------------------------------------------------------------------
	
	//-------  Folder Properties --------------------------------------------------------------

	//--------------------------------------------------------------------------------------

	//------- Default Properties -----------------------------------------------------------
	
	@Override
	protected Properties getDefaultProperties(){
		Properties defaultProperties = new Properties();
		
		defaultProperties.setProperty(SwatProperty.REQUIRE_NET_TYPE.toString(), "false");
		defaultProperties.setProperty(SwatProperty.VERIFY_PNML_SCHEMA.toString(), "false");
		defaultProperties.setProperty(SwatProperty.ICON_SIZE.toString(), IconSize.MEDIUM.toString());
		
		return defaultProperties;
	}
	
	//--------------------------------------------------------------------------------------
	
	public void store() throws IOException {
		try {
			store(propertyFileName);
		} catch (IOException e) {
			throw new IOException("Cannot create/store swat properties file on disk.");
		}
	}
}
