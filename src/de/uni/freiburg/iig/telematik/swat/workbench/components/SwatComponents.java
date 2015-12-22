package de.uni.freiburg.iig.telematik.swat.workbench.components;

import java.util.LinkedList;
import java.util.List;


import de.invation.code.toval.graphic.dialog.MessageDialog;
import de.invation.code.toval.misc.wd.AbstractProjectComponents;
import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.ACModelContainer;
import de.uni.freiburg.iig.telematik.sewol.context.process.ProcessContextContainer;
import de.uni.freiburg.iig.telematik.swat.logs.AbstractLogModelContainer;
import de.uni.freiburg.iig.telematik.swat.logs.AristaflowLogContainer;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.logs.MxmlLogContainer;
import de.uni.freiburg.iig.telematik.swat.logs.SwatLogType;
import de.uni.freiburg.iig.telematik.swat.logs.XesLogContainer;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class SwatComponents extends AbstractProjectComponents {

    private static final String CSVLogNameFormat = "%s%s.csv";
    private static final String AnalysisNameFormat = "%s%s.xml";

    private static SwatComponents instance = null;

    private ProcessContextContainer containerProcessContexts;
    private ACModelContainer containerACModels;
    private SwatPNContainer containerPetriNets;
    private Map<SwatLogType, AbstractLogModelContainer> logModelContainers;

    public SwatComponents() throws ProjectComponentException {
        super(MessageDialog.getInstance());
    }

    public static SwatComponents getInstance() throws ProjectComponentException {
        if (instance == null) {
            instance = new SwatComponents();
        }
        return instance;
    }

    public ProcessContextContainer getContainerProcessContexts() {
        return containerProcessContexts;
    }

    public ACModelContainer getContainerACModels() {
        return containerACModels;
    }

    public SwatPNContainer getContainerPetriNets() {
        return containerPetriNets;
    }
    
<<<<<<< HEAD
=======
    public ResourceContainer getResourceContainer(){
    	return resourceContainer;
    }
    
    public ResourceStoreContainer getResourceStoreContainer(){
    	return resourceStoreContainer;
    }
    
>>>>>>> e6a74c7... correct file handling for resourcestore
    public XesLogContainer getContainerXESLogs(){
    	if(logModelContainers.containsKey(SwatLogType.XES))
        return (XesLogContainer) logModelContainers.get(SwatLogType.XES);
		else
			try {
				return new XesLogContainer(SwatProperties.getInstance().getPathForLogs(), MessageDialog.getInstance());
			} catch (PropertyException | IOException e) {
				throw new RuntimeException(e);
			}
    }
    
    public MxmlLogContainer getContainerMXMLLogs(){
    	if(logModelContainers.containsKey(SwatLogType.MXML))
    		return (MxmlLogContainer) logModelContainers.get(SwatLogType.MXML);
		else
			try {
				return new MxmlLogContainer(SwatProperties.getInstance().getPathForLogs(), MessageDialog.getInstance());
			} catch (PropertyException | IOException e) {
				throw new RuntimeException(e);
			}
    }
    
    public AristaflowLogContainer getContainerAristaflowLogs(){
    	if(logModelContainers.containsKey(SwatLogType.Aristaflow))
        return (AristaflowLogContainer) logModelContainers.get(SwatLogType.Aristaflow);
		else
			try {
				return new AristaflowLogContainer(SwatProperties.getInstance().getPathForLogs(), MessageDialog.getInstance());
			} catch (PropertyException | IOException e) {
				throw new RuntimeException(e);
			}
    }
    
    
    public boolean containsPetriNets(){
        return getContainerPetriNets().containsComponents();
    }
    
    public boolean containsProcessContexts(){
        return getContainerProcessContexts().containsComponents();
    }
    
    public boolean containsACModels(){
        return getContainerACModels().containsComponents();
    }
    

    @Override
    protected void addComponentContainers() throws ProjectComponentException {
    	if(logModelContainers == null )
    		logModelContainers = new HashMap<>();
    	
        try {
            containerProcessContexts = new ProcessContextContainer(SwatProperties.getInstance().getPathForContexts(), MessageDialog.getInstance());
            containerProcessContexts.setIgnoreIncompatibleFiles(true);
            addComponentContainer(containerProcessContexts);
            containerACModels = new ACModelContainer(SwatProperties.getInstance().getPathForACModels(), getContainerProcessContexts(), MessageDialog.getInstance());
            addComponentContainer(containerACModels);
            containerProcessContexts.linkACModels(getContainerACModels(), true);
            containerPetriNets = new SwatPNContainer(SwatProperties.getInstance().getPathForNets(), getContainerACModels(), MessageDialog.getInstance());
            containerPetriNets.loadComponents();

            //TODO: Create and add other containers
            // Petri nets
            // - AnalysisContexts
            // - Labelings
            // - TimeContexts
            // Log Files 
            //AristaflowLogContainer aflogs=new AristaflowLogContainer(SwatProperties.getInstance().getPathForLogs(), MessageDialog.getInstance());
            logModelContainers.put(SwatLogType.MXML, getContainerMXMLLogs());
            logModelContainers.put(SwatLogType.XES, getContainerXESLogs());
            logModelContainers.put(SwatLogType.Aristaflow,getContainerAristaflowLogs());
            getContainerMXMLLogs().loadComponents();
            getContainerAristaflowLogs().loadComponents();
            getContainerXESLogs().loadComponents();

            // Analyses
        } catch (Exception e) {
            throw new ProjectComponentException("Exception while creating component containers", e);
        }
    }
	
    public List<LogModel> getLogs(SwatLogType type) {
        switch (type) {
            case Aristaflow:
                return getContainerAristaflowLogs().getComponentsSorted();
            case MXML:
                return getContainerMXMLLogs().getComponentsSorted();
            case XES:
                return getContainerXESLogs().getComponentsSorted();
        }
        return null;
    }

    public boolean containsLogs() {
        return getContainerMXMLLogs().containsComponents() || getContainerXESLogs().containsComponents() || getContainerAristaflowLogs().containsComponents();
    }

    public List<LogModel> getLogs() {
        LinkedList<LogModel> result = new LinkedList<>();
        result.addAll(getContainerXESLogs().getComponents());
        result.addAll(getContainerMXMLLogs().getComponents());
        result.addAll(getContainerAristaflowLogs().getComponents());
        return result;
    }

    public boolean containsLog(String logName) {
        if(getContainerXESLogs().containsComponent(logName))
            return true;
        if(getContainerMXMLLogs().containsComponent(logName))
            return true;
        if(getContainerAristaflowLogs().containsComponent(logName))
            return true;
        return false;
    }

    public static void main(String[] args) throws Exception {
        SwatComponents.getInstance();
    }
}
