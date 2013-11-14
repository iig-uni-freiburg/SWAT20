package de.uni.freiburg.iig.telematik.swat.workbench;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import de.invation.code.toval.misc.ArrayUtils;
import de.invation.code.toval.misc.StringUtils;
import de.invation.code.toval.properties.AbstractProperties;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.prism.searcher.PrismSearcher;


public class SwatProperties extends AbstractProperties{
	
	protected static final String defaultWorkingDirectory = ".";
	public static final String defaultWorkingDirectoryName = "SwatWorkingDirectory";
	
	protected static final String propertyFileName = "SwatProperties";
	
	private static SwatProperties instance = null;
	
	private String applicationPath = null;
	
	public SwatProperties() throws IOException {
		try {
			load(propertyFileName);
		} catch (IOException e) {
			// Create new property file.
			loadDefaultProperties();
			store();
		}
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
	
	public void setWorkingDirectory(String directory) throws ParameterException, IOException, PropertyException {
		validateWorkingDirectory(directory);
		setProperty(SwatProperty.WORKING_DIRECTORY, directory);
		File directoryFile = new File(directory);
		if(!directoryFile.exists()){
			directoryFile.mkdir();
		}
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
	
	public void addKnownWorkingDirectory(String workingDirectory) throws ParameterException{
		validateWorkingDirectory(workingDirectory);
		Set<String> currentDirectories = getKnownWorkingDirectories();
		currentDirectories.add(workingDirectory);
		setProperty(SwatProperty.KNOWN_WORKING_DIRECTORIES, ArrayUtils.toString(prepareWorkingDirectories(currentDirectories)));
	}
	
	public void removeKnownWorkingDirectory(String simulationDirectory) throws ParameterException{
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
		StringTokenizer directoryTokens = StringUtils.splitArrayString(propertyValue, String.valueOf(ArrayUtils.VALUE_SEPARATION));
		while(directoryTokens.hasMoreTokens()){
			String nextToken = directoryTokens.nextToken();
			result.add(nextToken.substring(1, nextToken.length()-1));
		}
		return result;
	}
	
	//------- PNML Parser properties -------------------------------------------------------
	
	public void setRequestNetType(boolean requestNetType) throws ParameterException{
		Validate.notNull(requestNetType);
		setProperty(SwatProperty.REQUIRE_NET_TYPE, requestNetType);
	}
	
	public boolean getRequestNetType() throws PropertyException, ParameterException{
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
	
	public boolean getPNValidation() throws PropertyException, ParameterException{
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
	}
	
	public String getPrismPath() throws PropertyException, ParameterException {
		String propertyValue = getProperty(SwatProperty.PRISM_PATH);
		if(propertyValue == null)
			throw new PropertyException(SwatProperty.PRISM_PATH, propertyValue);
		PrismSearcher.validatePrismPath(propertyValue);
		return propertyValue;
	}
	
	//------- Validation -------------------------------------------------------------------
	
	private void validateWorkingDirectory(String path) throws ParameterException{
		Validate.directory(path);
	}
	
	
	
	//------- Default Properties -----------------------------------------------------------
	
	@Override
	protected Properties getDefaultProperties(){
		Properties defaultProperties = new Properties();
		
		defaultProperties.setProperty(SwatProperty.REQUIRE_NET_TYPE.toString(), "false");
		defaultProperties.setProperty(SwatProperty.VERIFY_PNML_SCHEMA.toString(), "false");
		
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
