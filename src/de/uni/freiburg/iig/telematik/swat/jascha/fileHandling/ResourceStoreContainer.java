package de.uni.freiburg.iig.telematik.swat.jascha.fileHandling;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.invation.code.toval.debug.SimpleDebugger;
import de.invation.code.toval.misc.NamedComponent;
import de.invation.code.toval.misc.wd.AbstractComponentContainer;
import de.invation.code.toval.misc.wd.ComponentListener;
import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceStore;

public class ResourceStoreContainer extends AbstractComponentContainer<ResourceStore> implements ComponentListener<ResourceStore>{

	public ResourceStoreContainer(String basePath){
		this(basePath,null);
	}
	
	public ResourceStoreContainer(String basePath, SimpleDebugger debugger) {
		super(basePath, debugger);
        setIgnoreIncompatibleFiles(true);
        setUseSubdirectoriesForComponents(true);
        this.addComponentListener(this);
        setUseSubdirectoriesForComponents(false);
	}

	@Override
	public String getComponentDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ResourceStore loadComponentFromFile(String file) throws Exception {
		return ResourceStoreParser.parse(new File(file));
	}

	@Override
	protected void serializeComponent(ResourceStore component, String basePath, String fileName) throws Exception {
		ResourceContainerSerializer.serialize(component,new File(basePath,fileName));

	}

	@Override
	public void componentAdded(ResourceStore component) throws ProjectComponentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentRemoved(ResourceStore component) throws ProjectComponentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentRenamed(ResourceStore component, String oldName, String newName)
			throws ProjectComponentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentsChanged() throws ProjectComponentException {
		// TODO Auto-generated method stub
		
	}
	
    public Set<String> getAcceptedFileEndings() {
        return new HashSet<>(Arrays.asList("rs"));
    }
    
    protected File getComponentFile(File pathFile, String componentName) throws ProjectComponentException {
            return new File(pathFile,componentName+ ".rs");
    }

}
