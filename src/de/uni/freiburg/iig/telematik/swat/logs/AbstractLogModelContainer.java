/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.swat.logs;

import de.invation.code.toval.debug.SimpleDebugger;
import de.invation.code.toval.misc.wd.AbstractComponentContainer;
import de.invation.code.toval.misc.wd.ComponentListener;
import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.swat.analysis.Analysis;
import de.uni.freiburg.iig.telematik.swat.analysis.AnalysisContainer;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author stocker
 */
public abstract class AbstractLogModelContainer extends AbstractComponentContainer<LogModel> implements ComponentListener<LogModel> {

    Map<String, AnalysisContainer> analysisContainers = new HashMap<>();

    public AbstractLogModelContainer(String basePath) {
        super(basePath, null);
    }

    public AbstractLogModelContainer(String basePath, SimpleDebugger debugger) {
        super(basePath, debugger);
        setIgnoreIncompatibleFiles(true);
        setUseSubdirectoriesForComponents(true);
        this.addComponentListener(this);
    }

    public AnalysisContainer getContainerAnalysis(String logModelName) throws ProjectComponentException {
        validateComponent(logModelName);
        if (!containsAnalysisContainer(logModelName)) {
            createNewAnalysisContainer(getComponent(logModelName));
            //throw new ProjectComponentException("analysis container for log model \"" + logModelName + "\" is NULL");
        }
        return analysisContainers.get(logModelName);
    }

    public boolean containsAnalysisContainer(String logModelName) {
        return analysisContainers.containsKey(logModelName);
    }

    protected abstract SwatLogType getLogType();

    public List<LogModel> getComponentsSorted() {
        return getComponentsSorted(new LogModelComparator());
    }

    @Override
    public String getComponentDescriptor() {
        return getLogType().getDescription();
    }

    public Analysis getAnalysis(String logName, String analysisName) throws ProjectComponentException {
        validateComponent(logName);
        return getContainerAnalysis(logName).getComponent(analysisName);
    }

    @Override
    protected File getComponentFile(File pathFile, String componentName) throws ProjectComponentException {
        File log;
        try {
            log = new File(pathFile.getCanonicalPath(), componentName + ".csv");
            if (log.exists()) {
                return log; //AristaFlow
            }
            log = new File(pathFile.getCanonicalPath(), componentName + ".xes");
            if (log.exists()) {
                return log; //xes
            }
            log = new File(pathFile.getCanonicalPath(), componentName + ".mxml");
            return log; //mxml
        } catch (IOException e) {
            throw new ProjectComponentException("could not compose log file for " + componentName + ": " + e.getMessage());
        }
    }

    public boolean containsContainerAnalysis(String netName) {
        return analysisContainers.containsKey(netName);
    }

    @Override
    public void loadComponents() throws ProjectComponentException {
        super.loadComponents();
        // Petri nets have been added in super-method and reported to this class
        // -> Analysis containers have been created and put into the corresponding maps
        debugMessage("Load log");
        for (AnalysisContainer analysisContainer : analysisContainers.values()) {
            analysisContainer.loadComponents();
        }
    }

    @Override
    protected LogModel loadComponentFromFile(String file) throws Exception {
        return new LogModel(new File(file), getLogType());
    }

    @Override
    protected void serializeComponent(LogModel component, String basePath, String fileName) throws Exception {
        //System.out.println("");
        //actually nothing to do for logModels
        //Files.copy(component.getFileReference().toPath(), new File(basePath,fileName).toPath(),StandardCopyOption.REPLACE_EXISTING);
        //component.setFileReference(new File(basePath,fileName));
    }

    @Override
    public Set<String> getAcceptedFileEndings() {
        return new HashSet<>(Arrays.asList(getLogType().getFileEnding()));
    }

    @Override
    public void renameComponent(String oldName, String newName, boolean notifyListeners) throws ProjectComponentException {
        super.renameComponent(oldName, newName, notifyListeners);
        getComponent(newName).setFileReference(getComponentFile(newName));
    }

    @Override
    public void componentsChanged() throws ProjectComponentException {
    }

    ;

    @Override
    public void componentRenamed(LogModel component, String oldName, String newName) throws ProjectComponentException {
        if (analysisContainers.containsKey(oldName)) {
            Collection<Analysis> analyses = getContainerAnalysis(oldName).getComponents();
            getContainerAnalysis(oldName).removeComponents(true, false);
            analysisContainers.put(newName, createNewAnalysisContainer(component));
            for (Analysis analysis : analyses) {
                getContainerAnalysis(newName).addComponent(analysis, true, false);
            }
        }
    }

    private AnalysisContainer createNewAnalysisContainer(LogModel component) throws ProjectComponentException {
        debugMessage("Create analysis container for added log model \"" + component.getName() + "\"");
        try {
            return new AnalysisContainer(getAnalysisDirectory(component.getName()), getDebugger());
        } catch (Exception e) {
            throw new ProjectComponentException("Cannot create analysis container");
        }
    }

    @Override
    public void componentRemoved(LogModel component) throws ProjectComponentException {
        //Try to remove all related analyses
        if (analysisContainers.containsKey(component.getName())) {
            debugMessage("Identify related analyses of removed log model");
            try {
                // Removal of files is not necessary/possible since log model container already removed the whole log directory
                analysisContainers.get(component.getName()).removeComponents(false); //Just for informing listeners
                analysisContainers.remove(component.getName());
            } catch (Exception e) {
                throw new ProjectComponentException("Cannot remove related analyses of removed log model", e);
            }
        }
    }

    @Override
    public void componentAdded(LogModel component) throws ProjectComponentException {
        //Try to create and add containers for log model
        analysisContainers.put(component.getName(), createNewAnalysisContainer(component));
    }

    private String getAnalysisDirectory(String logModelName) throws Exception {
        String logPath = getBasePath();
        if (!logPath.endsWith(System.getProperty("file.separator"))) {
            logPath = logPath.concat(System.getProperty("file.separator"));
        }
        logPath = logPath.concat(logModelName);
        logPath = logPath.concat(System.getProperty("file.separator"));
        return logPath;
    }

    public void storeAnalyses(String logName) throws ProjectComponentException {
        validateComponent(logName);
        getContainerAnalysis(logName).storeComponents();
    }

    public void storeAnalysis(String logName, String analysisName) throws ProjectComponentException {
        validateComponent(logName);
        getContainerAnalysis(logName).validateComponent(analysisName);
        getContainerAnalysis(logName).storeComponent(analysisName);
    }

    public void addAnalysis(Analysis analysis, String logName, boolean storeToFile) throws ProjectComponentException {
        addAnalysis(analysis, logName, storeToFile, true);
    }

    public void addAnalysis(Analysis analysis, String logName, boolean storeToFile, boolean notifyListeners) throws ProjectComponentException {
        validateComponent(logName);
        if (containsAnalysis(analysis.getName())) {
            throw new ProjectComponentException("Container already contains an analysis with name \"" + analysis.getName() + "\"");
        }
        getContainerAnalysis(logName).addComponent(analysis, storeToFile, notifyListeners);
    }

    public boolean containsAnalysis(String analysisName) {
        for (AnalysisContainer analysisContainer : analysisContainers.values()) {
            if (analysisContainer.containsComponent(analysisName)) {
                return true;
            }
        }
        return false;
    }

    public void storeAnalyses() throws ProjectComponentException {
        for (String logName : analysisContainers.keySet()) {
            analysisContainers.get(logName).storeComponents();
        }
    }

    @Override
    public void storeComponent(String componentName) throws ProjectComponentException {
        super.storeComponent(componentName);
        //store analysis components, if any
        AnalysisContainer analysisContainer = getContainerAnalysis(componentName);
        if (analysisContainer != null && getContainerAnalysis(componentName).containsComponents()) {
            getContainerAnalysis(componentName).storeComponents();
        }
    }

    @Override
    public void storeComponents() throws ProjectComponentException {
        super.storeComponents();
        for (AnalysisContainer analysisContainer : analysisContainers.values()) {
            analysisContainer.storeComponents();
        }
    }

//     @Override
//     protected File getComponentFile(File pathFile, String componentName) throws ProjectComponentException {
//         return new File(pathFile, componentName);
//     }
}
