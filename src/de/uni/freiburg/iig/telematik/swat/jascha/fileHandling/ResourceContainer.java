package de.uni.freiburg.iig.telematik.swat.jascha.fileHandling;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.invation.code.toval.debug.SimpleDebugger;
import de.invation.code.toval.misc.wd.AbstractComponentContainer;
import de.invation.code.toval.misc.wd.ComponentListener;
import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.swat.jascha.AwesomeResourceContext;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceStore;

public class ResourceContainer extends AbstractComponentContainer<IResourceContext> implements ComponentListener<IResourceContext>{
	
	protected final String CONTAINER_DESCRIPTOR="Resource Contexts";

	public ResourceContainer(String basePath){
		this(basePath,null);
	}
	
	public ResourceContainer(String basePath, SimpleDebugger debugger) {
		super(basePath, debugger);
        setIgnoreIncompatibleFiles(true);
        setUseSubdirectoriesForComponents(true);
        this.addComponentListener(this);
        setUseSubdirectoriesForComponents(false);
	}

	@Override
	public void componentAdded(IResourceContext component) throws ProjectComponentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentRemoved(IResourceContext component) throws ProjectComponentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentRenamed(IResourceContext component, String oldName, String newName)
			throws ProjectComponentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentsChanged() throws ProjectComponentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getComponentDescriptor() {
		return CONTAINER_DESCRIPTOR;
	}

	@Override
	protected IResourceContext loadComponentFromFile(String file) throws Exception {
		return ResourceContextParser.parse(new File(file));
	}

	@Override
	protected void serializeComponent(IResourceContext component, String basePath, String fileName) throws Exception {
		ResourceContextSerializer.serialize(component, new File(basePath,fileName));
		
	}
	
    public Set<String> getAcceptedFileEndings() {
        return new HashSet<>(Arrays.asList("rc"));
    }
    
    protected File getComponentFile(File pathFile, String componentName) throws ProjectComponentException {
            return new File(pathFile,componentName+ ".rc");
    }
    
    protected String getFileEndingForComponent(IResourceContext component) {
        return "rc";
    }

	public void linkResourceStores(ResourceStoreContainer resourceStoreContainer) {
		for(IResourceContext context: getComponents()){
			String storeName=((AwesomeResourceContext) context).getResourceStoreName();
			if(resourceStoreContainer.containsComponent(storeName))
				try {
					((AwesomeResourceContext)context).setResourceStore(resourceStoreContainer.getComponent(storeName));
				} catch (ProjectComponentException e) {
					System.out.println("Could not find/link ResourceStore: "+storeName);
				}
		}
		
	}

}
