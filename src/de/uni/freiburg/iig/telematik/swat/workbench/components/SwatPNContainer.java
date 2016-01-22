package de.uni.freiburg.iig.telematik.swat.workbench.components;

import de.invation.code.toval.debug.SimpleDebugger;
import de.invation.code.toval.misc.wd.ComponentListener;
import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPNNameComparator;
import de.uni.freiburg.iig.telematik.sepia.graphic.container.AbstractGraphicalPNContainer;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContextContainer;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.ACModelContainer;
import de.uni.freiburg.iig.telematik.swat.analysis.Analysis;
import de.uni.freiburg.iig.telematik.swat.analysis.AnalysisContainer;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContext;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContextContainer;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 *
 * @author stocker
 */
public class SwatPNContainer extends AbstractGraphicalPNContainer implements ComponentListener<AbstractGraphicalPN> {

    private final Map<String, AnalysisContextContainer> analysisContextContainers = new HashMap<>();
    private final Map<String, AnalysisContainer> analysisContainers = new HashMap<>();
    private final Map<String, TimeContextContainer> timeContextContainers = new HashMap<>();
    private final HashSet<String> askForLayout = new HashSet<>();

    private ACModelContainer availableACModels = null;

    public SwatPNContainer(String serializationPath, ACModelContainer availableACModels) {
        this(serializationPath, availableACModels, null);
    }

    public SwatPNContainer(String serializationPath, ACModelContainer availableACModels, SimpleDebugger debugger) {
        super(serializationPath, debugger);
        Validate.notNull(availableACModels);
        this.availableACModels = availableACModels;
        setIgnoreIncompatibleFiles(true);
        setUseSubdirectoriesForComponents(true);
        addComponentListener(this);
    }

    public List<AbstractGraphicalPN> getComponentsSorted() {
        return getComponentsSorted(new GraphicalPNNameComparator());
    }
    
    public void setAskForLayout(String net){
    	askForLayout.add(net);
    }
    
    public boolean needsLayout(String net){
    	return askForLayout.contains(net);
    }
    
    public boolean removeLayoutNeed(String net){
    	return askForLayout.remove(net);
    }

    public AnalysisContextContainer getContainerAnalysisContext(String netName) throws ProjectComponentException {
        if (!containsContainerAnalysisContext(netName)) {
            throw new ProjectComponentException("Analysis context container for net \"" + netName + "\" is NULL");
        }
        return analysisContextContainers.get(netName);
    }
    
    public AnalysisContainer getContainerAnalysis(String netName) throws ProjectComponentException {
        if (!containsContainerAnalysis(netName)) {
            throw new ProjectComponentException("Analysis container for net \"" + netName + "\" is NULL");
        }
        return analysisContainers.get(netName);
    }
    
    public TimeContextContainer getContainerTimeContexts(String netName) throws ProjectComponentException {
        if (!containsContainerTimeContexts(netName)) {
            throw new ProjectComponentException("Time context container for net \"" + netName + "\" is NULL");
        }
        return timeContextContainers.get(netName);
    }

    public boolean containsContainerTimeContexts(String netName) {
        return timeContextContainers.containsKey(netName);
    }
    
    public AnalysisContext getAnalysisContext(String netName, String aContextName) throws ProjectComponentException {
        validateComponent(netName);
        return getContainerAnalysisContext(netName).getComponent(aContextName);
    }
    
    public TimeContext getTimeContext(String netName, String timeContextName) throws ProjectComponentException {
        validateComponent(netName);
        return getContainerTimeContexts(netName).getComponent(timeContextName);
    }
    
    public Analysis getAnalysis(String netName, String analysisName) throws ProjectComponentException {
        validateComponent(netName);
        return getContainerAnalysis(netName).getComponent(analysisName);
    }

    public boolean containsContainerAnalysisContext(String netName) {
        return analysisContextContainers.containsKey(netName);
    }
    
    public boolean containsContainerAnalysis(String netName) {
        return analysisContainers.containsKey(netName);
    }

    @Override
    /**load petri nets and contexts**/
    public void loadComponents() throws ProjectComponentException {
    	//super.setUseSubdirectoriesForComponents(true);
        super.loadComponents();
        // Petri nets have been added in super-method and reported to this class
        // -> Analysis context containers have been created and put into the corresponding maps
        // -> analysis containers have been created and put into the corresponding maps
        debugMessage("Load analysis contexts of loaded nets");
        for (AnalysisContextContainer analysisContextContainer : analysisContextContainers.values()) {
            analysisContextContainer.loadComponents();
        }
        debugMessage("Load analyses of loaded nets");
        for (AnalysisContainer analysisContainer : analysisContainers.values()) {
            analysisContainer.loadComponents();
        }
        debugMessage("Load time contexts of loaded nets");
        for (TimeContextContainer timeContextContainer : timeContextContainers.values()) {
            timeContextContainer.loadComponents();
        }
    }

    @Override
    public void componentAdded(AbstractGraphicalPN component) throws ProjectComponentException {
        //Try to create and add containers for analysis contexts and analyses
        debugMessage("Create and add analysis context container for added net \"" + component.getName() + "\"");
        analysisContextContainers.put(component.getName(), createNewAnalysisContextContainer(component));
        debugMessage("Create analysis container for added net \"" + component.getName() + "\"");
        analysisContainers.put(component.getName(), createNewAnalysisContainer(component));
        debugMessage("Create time context container for added net \"" + component.getName() + "\"");
        timeContextContainers.put(component.getName(), createNewTimeContextContainer(component));
    }

    private String getDirectoryAnalysisContexts(String netName) throws Exception {
        return getConceptDirectory(netName, SwatProperties.getInstance().getAnalysisContextDirectoryName());
    }

    private String getDirectoryAnalysis(String netName) throws Exception {
        return getConceptDirectory(netName, SwatProperties.getInstance().getNetAnalysesDirectoryName());
    }
    
    private String getDirectoryTimeContexts(String netName) throws Exception {
        return getConceptDirectory(netName, SwatProperties.getInstance().getTimeContextDirectoryName());
    }
    
    private String getConceptDirectory(String netName, String conceptDirectoryName) throws Exception {
        String netPath = getBasePath();
        if (!netPath.endsWith(System.getProperty("file.separator"))) {
            netPath = netPath.concat(System.getProperty("file.separator"));
        }
        netPath = netPath.concat(netName);
        netPath = netPath.concat(System.getProperty("file.separator"));
        netPath = netPath.concat(conceptDirectoryName);
        return netPath;
    }
    
    @Override
    public void componentRemoved(AbstractGraphicalPN component) throws ProjectComponentException {
        //Try to remove all related analysis contexts
        if (analysisContextContainers.containsKey(component.getName())) {
            debugMessage("Identify related analysis contexts of removed Petri net");
            try {
                // Removal of files is not necessary/possible since PNContainer already removed the whole net directory
                analysisContextContainers.get(component.getName()).removeComponents(false); //Just for informing listeners
                analysisContextContainers.remove(component.getName());
            } catch (Exception e) {
                throw new ProjectComponentException("Cannot remove related analysis contexts of removed Petri net", e);
            }
        }
        //Try to remove all related analyses
        if (analysisContainers.containsKey(component.getName())) {
            debugMessage("Identify related analyses of removed Petri net");
            try {
                // Removal of files is not necessary/possible since PNContainer already removed the whole net directory
                analysisContainers.get(component.getName()).removeComponents(false); //Just for informing listeners
                analysisContainers.remove(component.getName());
            } catch (Exception e) {
                throw new ProjectComponentException("Cannot remove related analyses of removed Petri net", e);
            }
        }
        //Try to remove all related time contexts
        if (timeContextContainers.containsKey(component.getName())) {
            debugMessage("Identify related time contexts of removed Petri net");
            try {
                // Removal of files is not necessary/possible since PNContainer already removed the whole net directory
                timeContextContainers.get(component.getName()).removeComponents(false); //Just for informing listeners
                timeContextContainers.remove(component.getName());
            } catch (Exception e) {
                throw new ProjectComponentException("Cannot remove related time contexts of removed Petri net", e);
            }
        }
    }

    @Override
    public void componentRenamed(AbstractGraphicalPN component, String oldName, String newName) throws ProjectComponentException {
        if (analysisContextContainers.containsKey(oldName)) {
            Collection<AnalysisContext> analysisContexts = getContainerAnalysisContext(oldName).getComponents();
            Map<String,Collection<Labeling>> contextLabelings = new HashMap<>();
            for(AnalysisContext analysisContext: analysisContexts){
                contextLabelings.put(analysisContext.getName(), new ArrayList<>());
                for(Labeling labeling: getContainerAnalysisContext(oldName).getLabelings(analysisContext.getName())){
                    contextLabelings.get(analysisContext.getName()).add(labeling);
                }
            }
            getContainerAnalysisContext(oldName).removeComponents(true, false);
            analysisContextContainers.put(newName, createNewAnalysisContextContainer(component));
            for(AnalysisContext analysisContext: analysisContexts){
                getContainerAnalysisContext(newName).addComponent(analysisContext, true, false);
                for(Labeling labeling: contextLabelings.get(analysisContext.getName())){
                    getContainerAnalysisContext(newName).addLabeling(labeling, analysisContext.getName(), true, false);
                }
            }
        }
        if (analysisContainers.containsKey(oldName)) {
            Collection<Analysis> analyses = getContainerAnalysis(oldName).getComponents();
            getContainerAnalysis(oldName).removeComponents(true, false);
            analysisContainers.put(newName, createNewAnalysisContainer(component));
            for(Analysis analysis: analyses){
                getContainerAnalysis(newName).addComponent(analysis, true, false);
            }
        }
        if (timeContextContainers.containsKey(oldName)) {
            Collection<TimeContext> timeContexts = getContainerTimeContexts(oldName).getComponents();
            getContainerTimeContexts(oldName).removeComponents(true, false);
            timeContextContainers.put(newName, createNewTimeContextContainer(component));
            for(TimeContext timeContext: timeContexts){
                getContainerTimeContexts(newName).addComponent(timeContext, true, false);
            }
        }
    }
    
    private AnalysisContextContainer createNewAnalysisContextContainer(AbstractGraphicalPN component) throws ProjectComponentException{
        debugMessage("Create analysis context container for added net \"" + component.getName() + "\"");
        try {
            return new AnalysisContextContainer(getDirectoryAnalysisContexts(component.getName()), availableACModels, getDebugger());
        } catch (Exception e) {
            throw new ProjectComponentException("Cannot create analysis context container");
        }
    }
    
    private AnalysisContainer createNewAnalysisContainer(AbstractGraphicalPN component) throws ProjectComponentException{
        debugMessage("Create analysis container for added net \"" + component.getName() + "\"");
        try {
            return new AnalysisContainer(getDirectoryAnalysis(component.getName()), getDebugger());
        } catch (Exception e) {
            throw new ProjectComponentException("Cannot create analysis container");
        }
    }
    
    private TimeContextContainer createNewTimeContextContainer(AbstractGraphicalPN component) throws ProjectComponentException{
        debugMessage("Create time context container for added net \"" + component.getName() + "\"");
        try {
            return new TimeContextContainer(getDirectoryTimeContexts(component.getName()), getDebugger());
        } catch (Exception e) {
            throw new ProjectComponentException("Cannot create time context container");
        }
    }
  

    @Override
    public void componentsChanged() throws ProjectComponentException {}

    @Override
    public void storeComponent(String componentName) throws ProjectComponentException {
    	super.storeComponent(componentName);
    	componentAdded((AbstractGraphicalPN) this.getComponent(componentName));
        getContainerAnalysisContext(componentName).storeComponents();
        getContainerAnalysis(componentName).storeComponents();
        getContainerTimeContexts(componentName).storeComponents();
    }

    public void storeAnalysisContexts() throws ProjectComponentException {
        for (String netName : analysisContextContainers.keySet()) {
            analysisContextContainers.get(netName).storeComponents();
        }
    }
    
    public void storeAnalyses() throws ProjectComponentException {
        for (String netName : analysisContainers.keySet()) {
            analysisContainers.get(netName).storeComponents();
        }
    }
    
    public void storetimeContexts() throws ProjectComponentException {
        for (String netName : timeContextContainers.keySet()) {
            timeContextContainers.get(netName).storeComponents();
        }
    }

    public void storeAnalysisContexts(String netName) throws ProjectComponentException {
        validateComponent(netName);
        getContainerAnalysisContext(netName).storeComponents();
    }

    public void storeAnalysisContext(String netName, String analysisContextName) throws ProjectComponentException {
        validateComponent(netName);
        getContainerAnalysisContext(netName).validateComponent(analysisContextName);
        getContainerAnalysisContext(netName).storeComponent(analysisContextName);
    }
    
    public void addAnalysisContext(AnalysisContext analysisContext, String netName, boolean storeToFile) throws ProjectComponentException {
        addAnalysisContext(analysisContext, netName, storeToFile, true);
    }
    
    public void addAnalysisContext(AnalysisContext analysisContext, String netName, boolean storeToFile, boolean notifyListeners) throws ProjectComponentException {
        validateComponent(netName);
        new File(getBasePath()).mkdirs();
        if(containsAnalysisContext(analysisContext.getName())){
            throw new ProjectComponentException("Container already contains an analysis context with name \"" + analysisContext.getName() + "\"");
        }
        getContainerAnalysisContext(netName).addComponent(analysisContext, storeToFile, notifyListeners);
    }

    public boolean containsAnalysisContext(String analysisContextName) {
        for (AnalysisContextContainer analysisContextContainer : analysisContextContainers.values()) {
            if (analysisContextContainer.containsComponent(analysisContextName)) {
                return true;
            }
        }
        return false;
    }
    
    public void storeAnalyses(String netName) throws ProjectComponentException {
        validateComponent(netName);
        getContainerAnalysis(netName).storeComponents();
    }

    public void storeAnalysis(String netName, String analysisName) throws ProjectComponentException {
        validateComponent(netName);
        getContainerAnalysis(netName).validateComponent(analysisName);
        getContainerAnalysis(netName).storeComponent(analysisName);
    }
    
    public void addAnalysis(Analysis analysis, String netName, boolean storeToFile) throws ProjectComponentException {
        addAnalysis(analysis, netName, storeToFile, true);
    }
    
    public void addAnalysis(Analysis analysis, String netName, boolean storeToFile, boolean notifyListeners) throws ProjectComponentException {
        validateComponent(netName);
        if(containsAnalysis(analysis.getName())){
            throw new ProjectComponentException("Container already contains an analysis with name \"" + analysis.getName() + "\"");
        }
        getContainerAnalysis(netName).addComponent(analysis, storeToFile, notifyListeners);
    }

    public boolean containsAnalysis(String analysisName) {
        for (AnalysisContainer analysisContainer : analysisContainers.values()) {
            if (analysisContainer.containsComponent(analysisName)) {
                return true;
            }
        }
        return false;
    }

    public void storeTimeContexts(String netName) throws ProjectComponentException {
        validateComponent(netName);
        getContainerTimeContexts(netName).storeComponents();
    }

    public void storeTimeContext(String netName, String timeContextName) throws ProjectComponentException {
        validateComponent(netName);
        getContainerTimeContexts(netName).validateComponent(timeContextName);
        getContainerTimeContexts(netName).storeComponent(timeContextName);
    }
    
    public void addTimeContext(TimeContext timeContext, String netName, boolean storeToFile) throws ProjectComponentException {
        addTimeContext(timeContext, netName, storeToFile, true);
    }
    
    public void addTimeContext(TimeContext timeContext, String netName, boolean storeToFile, boolean notifyListeners) throws ProjectComponentException {
        validateComponent(netName);
        if(containsTimeContext(timeContext.getName())){
            throw new ProjectComponentException("Container already contains a time context with name \"" + timeContext.getName() + "\"");
        }
        getContainerTimeContexts(netName).addComponent(timeContext, storeToFile, notifyListeners);
    }

    public boolean containsTimeContext(String timeContextName) {
        for (TimeContextContainer timeContextContainer : timeContextContainers.values()) {
            if (timeContextContainer.containsComponent(timeContextName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void storeComponents() throws ProjectComponentException {
        super.storeComponents();
        for(AnalysisContextContainer analysisContextContainer: analysisContextContainers.values()){
            analysisContextContainer.storeComponents();
        }
        for(AnalysisContainer analysisContainer: analysisContainers.values()){
            analysisContainer.storeComponents();
        }
        for(TimeContextContainer timeContextContainer: timeContextContainers.values()){
            timeContextContainer.storeComponents();
        }
    }
}
