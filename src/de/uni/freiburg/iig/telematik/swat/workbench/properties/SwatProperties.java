package de.uni.freiburg.iig.telematik.swat.workbench.properties;

import de.invation.code.toval.graphic.util.SpringUtilities;
import de.invation.code.toval.misc.wd.AbstractProjectComponents;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import de.invation.code.toval.misc.wd.AbstractWorkingDirectoryProperties;
import de.invation.code.toval.os.OSUtils;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.analysis.prism.searcher.PrismSearcher;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory.IconSize;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.EditorProperties;

public class SwatProperties extends AbstractWorkingDirectoryProperties<SwatProperty> {
	// TODO  WHEN A NEW PROPERTY IS ADDED OR REMOVED YOU HAVE TO ADJUST THE SECOND VALUE OF makeCompactGrid in SWATPropertySettingDialog.java
	//	protected void generateGrid() {           vv  
	// 		SpringUtilities.makeCompactGrid(this, 27, 2, 5, 5, 5, 5);
	//	}
    public static final String WORKING_DIRECTORY_DESCRIPTOR = "Swat Working Directory";
    public static final String DEFAULT_SWAT_WORKING_DIRECTORY_NAME = "SwatWorkingDirectory";
    public static final String SWAT_PROPERTY_FILE_NAME = OSUtils.getUserHomeDirectory() + "/.swatProperties";

    protected static final String pathNets = "nets/";
    protected static final String pathLogs = "logs/";
    protected static final String pathViews = "views/";
    protected static final String pathACModels = "acModels/";
    protected static final String pathContexts = "contexts/";
    
    protected static final String timeContexts = "contexts/time/";
    protected static final String resourceContexts = "contexts/resources";
    
    protected static final String pathNameAnalysisContext = "analysis-contexts/";
    protected static final String pathNameTimeContext = "time-contexts/";
    protected static final String pathNameNetAnalyses = "analysis/";

    protected static final Set<String> validationSubDirectories = new HashSet<>();

    static {
        validationSubDirectories.add(pathNets);
        validationSubDirectories.add(pathLogs);
        validationSubDirectories.add(pathViews);
        validationSubDirectories.add(pathContexts);
        validationSubDirectories.add(pathACModels);
    }

    private static SwatProperties instance = null;
    
    private final EditorProperties editorProperties = EditorProperties.getInstance();

    public SwatProperties() throws IOException {
        super();
    }
    
    public EditorProperties getEditorProperties(){
        return editorProperties;
    }

    @Override
    public String getDefaultWorkingDirectoryName() {
        return DEFAULT_SWAT_WORKING_DIRECTORY_NAME;
    }

    @Override
    public String getPropertyFileName() {
        return SWAT_PROPERTY_FILE_NAME;
    }

    @Override
    public String getWorkingDirectoryDescriptor() {
        return WORKING_DIRECTORY_DESCRIPTOR;
    }

    /**
     *
     * @return @throws IOException if there is no property file and a new one
     * cannot be created.
     */
    public static SwatProperties getInstance() throws IOException {
        if (instance == null) {
            instance = new SwatProperties();
        }
        return instance;
    }

    //-- Simulation component paths (not stored in property file)
    public String getPathForContexts() throws PropertyException {
        return getWorkingDirectory().concat(pathContexts);
    }

    public String getPathForNets() throws PropertyException {
        return getWorkingDirectory().concat(pathNets);
    }

    public String getPathForLogs() throws PropertyException {
        return getWorkingDirectory().concat(pathLogs);
    }

    public String getPathForViews() throws PropertyException {
        return getWorkingDirectory().concat(pathViews);
    }

    public String getPathForACModels() throws PropertyException {
        return getWorkingDirectory().concat(pathACModels);
    }
    
    public String getPathForTimeContexts() throws PropertyException {
    	return getWorkingDirectory().concat(timeContexts);
    }
    
    public String getPathForResourceContexts() throws PropertyException {
    	return getWorkingDirectory().concat(resourceContexts);
    }

    public String getNetAnalysesDirectoryName() {
        return pathNameNetAnalyses;
    }

    public String getAnalysisContextDirectoryName() {
        return pathNameAnalysisContext;
    }

    public String getTimeContextDirectoryName() {
        return pathNameTimeContext;
    }

    //------- PNML Parser properties -------------------------------------------------------
    public void setRequestNetType(boolean requestNetType) throws ParameterException {
        Validate.notNull(requestNetType);
        setProperty(SwatProperty.REQUIRE_NET_TYPE, requestNetType);
    }

    public boolean getRequestNetType() throws PropertyException {
        String propertyValueRequestNetType = getProperty(SwatProperty.REQUIRE_NET_TYPE);

        Boolean requestNetType = null;
        try {
            requestNetType = Boolean.valueOf(propertyValueRequestNetType);
        } catch (Exception e) {
            throw new PropertyException(SwatProperty.REQUIRE_NET_TYPE, propertyValueRequestNetType);
        }
        return requestNetType;
    }

    public void setPNValidation(boolean pnValidation) throws ParameterException {
        Validate.notNull(pnValidation);
        setProperty(SwatProperty.VERIFY_PNML_SCHEMA, pnValidation);
    }

    public boolean getPNValidation() throws PropertyException {
        String propertyValueValidation = getProperty(SwatProperty.VERIFY_PNML_SCHEMA);

        Boolean validation = null;
        try {
            validation = Boolean.valueOf(propertyValueValidation);
        } catch (Exception e) {
            throw new PropertyException(SwatProperty.VERIFY_PNML_SCHEMA, propertyValueValidation);
        }
        return validation;
    }

    //------- Prism Path -------------------------------------------------------------------
    public void setPrismPath(String directory) throws ParameterException, IOException, PropertyException {
        //PrismSearcher.validatePrismPath(directory);
        setProperty(SwatProperty.PRISM_PATH, directory);
        store();
    }

    public String getPrismPath() throws PropertyException, ParameterException {
        String propertyValue = getProperty(SwatProperty.PRISM_PATH);
        if (propertyValue == null) {
            throw new PropertyException(SwatProperty.PRISM_PATH, propertyValue);
        }
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
        if (propertyValue == null) {
            throw new PropertyException(SwatProperty.LOLA_PATH, propertyValue);
        }
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
        if (propertyValue == null) {
            throw new PropertyException(SwatProperty.SCIFF_PATH, propertyValue);
        }
        //PrismSearcher.validatePrismPath(propertyValue);
        return propertyValue;
    }

    //-------  SQL Properties --------------------------------------------------------------
    public String getAristaFlowURL() {
        String result = getProperty(SwatProperty.ARISTA_FLOW_URL);
        if (result == null || "".equals(result)) {
            result = "jdbc:postgresql://127.0.0.1:5432/InvoiceLocal";
        }
        return result;
    }

    public void setAristaFlowURL(String url) {
        Validate.notNull(url);
        setProperty(SwatProperty.ARISTA_FLOW_URL, url);
    }

    public String getAristaFlowUser() {
        String result = getProperty(SwatProperty.ARISTA_FLOW_USER);
        if (result == null || "".equals(result)) {
            result = "ADEPT2";
        }
        return result;
    }

    public void setAristaFlowUser(String user) {
        Validate.notNull(user);
        setProperty(SwatProperty.ARISTA_FLOW_USER, user);
    }

    public String getAristaFlowPass() {
        String result = getProperty(SwatProperty.ARISTA_FLOW_PASS);
        if (result == null || "".equals(result)) {
            result = "ADEPT2DB";
        }
        return result;
    }

    public void setAristaFlowPass(String pass) {
        Validate.notNull(pass);
        setProperty(SwatProperty.ARISTA_FLOW_PASS, pass);
    }

    @Override
    protected Properties getDefaultProperties() {
        Properties defaultProperties = new Properties();

        defaultProperties.setProperty(SwatProperty.REQUIRE_NET_TYPE.toString(), "false");
        defaultProperties.setProperty(SwatProperty.VERIFY_PNML_SCHEMA.toString(), "false");
        defaultProperties.setProperty(SwatProperty.ICON_SIZE.toString(), IconSize.MEDIUM.toString());

        return defaultProperties;
    }

    @Override
    protected AbstractProjectComponents getProjectComponents() throws Exception {
        return SwatComponents.getInstance();
    }

    @Override
    protected Set<String> getSubDirectoriesForValidation() {
        return validationSubDirectories;
    }

}
