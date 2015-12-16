package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.ResourceStoreListener;

public class ResourceStore {
	
	protected static Map<String,IResource> resources;
	
	private LinkedList<ResourceStoreListener> listeners= new LinkedList<>();
	
	public ResourceStore(){
		resources = new HashMap<>();
	}
	
	public void addResource(IResource item){
		resources.put(item.getName(), item);
		informListenersOfResourceChange(item);
	}
	
	public void removeResource(IResource item){
		resources.remove(item.getName());
		informListenersOfResourceRemoval(item);
	}

	public IResource getResource(String name){
		return resources.get(name);
	}
	
	// Instantiation of resources of type ResourceSet with a given amount
	public IResource instantiateResource(ResourceType type, String name, int amount){
		if(!type.equals(ResourceType.SET))
			throw new ParameterException("can only instantiate Resource Set with amount");
		if (alreadyExists(name)){
			throw new ParameterException("A resource with this name already exists!");
		}
		ResourceSet rs = new ResourceSet(name, amount);
		resources.put(name, rs);		
		for(IResource res:rs.getRes()) resources.put(res.getName(), res);
		informListenersOfResourceChange(rs);
		return rs;
	}
	
	// Instantiating all types of Resources when they are given a type and a name only
	public IResource instantiateResource(ResourceType type, String name){
		if (alreadyExists(name)){
			throw new ParameterException("A resource with this name already exists!");
		}
		Resource result = null;		
		switch (type) {
		case COMPOUND:	
			result = new CompoundResource(name);
			resources.put(name, result);			
			break;

		case SET:
			//instantiating a ResourceSet without amount creates a ResourceSet with one element			
			result = new ResourceSet(name, 1);
			resources.put(name, result);
			break;
			
		case SIMPLE:
			result = new SimpleResource(name);
			resources.put(name, result);
			break;
			
		case SHARED:	
			result = new SharedResource(name);
			resources.put(name, result);
			break;
		}
		informListenersOfResourceChange(result);
		return result;
	}
	// Compound Resource with List of IResource Objects as content
	public IResource instantiateResource(ResourceType type, String name, List<IResource> elements){
		if (!type.equals(ResourceType.COMPOUND)){
			throw new ParameterException("The resource needs to be of type COMPOUND");
		}
		if (alreadyExists(name)){
			throw new ParameterException("A resource with this name already exists!");
		}
		CompoundResource cr = new CompoundResource(name, elements);
		resources.put(name, cr);
		// When creating a CompoundResource object with this method, the elements that are part of the CompoundResource 
		// must have already been instantiated and hence must already be registered with the ResourceStore, so only the new CompoundResource has to be registered.
		
		return null;
	}
	
	
		
	//Die Idee ist, anhand eines Praefixes die Anzahl der verfuegbaren Ressourcen zu erfahren. 
	//Bisher haben Ressourcen eindeutige Namen nach dem Muster Hammer1, Hammer2, Hammer3 usw, deswegen sollte das funktionieren.
	public int countAvailable(String prefix){
		int result = 0;
		Set<String> keys = resources.keySet();
		for(String key:keys){
			if (key.startsWith(prefix)){
				IResource resource = resources.get(key);
				if (resource.isAvailable()){
					result ++;
				}
			}
		}
		//System.out.println("There are " + result + " resources with prefix " + prefix + " available.");
		return result;
	}
	
	// Method for checking if a resource object with this name already exists
	public boolean alreadyExists(String name){		
		for(String key:resources.keySet()){
			if (key.equals(name)){				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Return all IResources in this store
	 * @return array of all resources
	 */
	public IResource[] getAllResources(){
		IResource[] result = new IResource[resources.values().size()];
		int i = 0;
		for(IResource res:resources.values()){
			result[i]=res;
			i++;
		}
		return result;
	}
	
	public void addResourceStoreListener(ResourceStoreListener listener){
		listeners.add(listener);
	}
	
	private void informListenersOfResourceChange(IResource res){
		//Info Jascha: Methode informiert die grafische Oberfläche, falls eine Resource hinzugefügt wurde. Siehe gui/ResourceStoreGUI
		for (ResourceStoreListener listener:listeners)
			listener.resourceStoreElementAdded(res);
	}
	
	private void informListenersOfResourceRemoval(IResource res){
		//Info Jascha: Methode informiert die grafische Oberfläche, falls eine Resource entfernt wurde
		for (ResourceStoreListener listener:listeners)
			listener.informStoreElementRemoved(res);
	}
}
