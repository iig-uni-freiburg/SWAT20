package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import de.invation.code.toval.misc.NamedComponent;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.ResourceStoreListener;

public class ResourceStore implements NamedComponent{
	
	protected Map<String,IResource> resources; //Resourcename, Resource
	
	@XStreamOmitField
	private LinkedList<ResourceStoreListener> listeners;

	private String name;
	
	public ResourceStore(){
		resources = new HashMap<>();
	}
	
	public ResourceStore(String name){
		this();
		setName(name);
	}
	
	public void addResource(Resource item){
		resources.put(item.getName(), item);
		informListenersOfResourceChange(item);
	}
	
	public void removeResource(IResource input) {
		Resource item = (Resource) input;
		ResourceType type = item.getType();
		switch (type) {
		case SET:
			removeResourceSet(item);
			break;
			
		case SIMPLE:
			SimpleResource simpleResource = (SimpleResource) item;
			if(simpleResource.getAssociatedResourceSets() > 0){
				removeResourceFromResourceSets(item);
				}
			break;

		case COMPOUND:
			//TODO: Was tun bei compound resources? Sollten deren Einzelteile auch entfernt werden - und andersrum,
			//muessen dann nicht alle Ressourcen überprüft werden, ob sie Teil einer CompoundResource sind und dann entsprechend geupdatet werden?
			System.out.println("Trying to remove compound ...");
			break;
			
		case SHARED:
			//do nothing
			System.out.println("Trying to remove shared...");
			break;
		}
		
		resources.remove(item.getName());
		informListenersOfResourceRemoval(item);
	}

	private void removeResourceSet(IResource item) {
		ResourceSet rs = (ResourceSet) item;
		for (Resource res:rs.resources){
			SimpleResource sr = (SimpleResource)res;
			
			if (sr.getAssociatedResourceSets() < 1){
				//Error
			}
			// If the simpleResource is associated with only this set it is be removed
			if (sr.getAssociatedResourceSets() == 1){
				resources.remove(sr.getName());
				informListenersOfResourceRemoval(sr);
			}
			// If the simpleResource is part of more sets than just the one to be removed, then the number of associated ResourceSets is decreased,
			//but the SimpleResource stays registered with the ResourceStore 
			if(sr.getAssociatedResourceSets() > 1){
				sr.updateAssociatedSets(UpdateType.DECREASE);
			}

		}
	}
	
	private void removeResourceFromResourceSets(IResource input){
		
		SimpleResource item = (SimpleResource) input;
		int associatedSets = item.getAssociatedResourceSets();
		
		// Falls SimpleResources Teil von mehreren Sets sein koennen, muessen mehrere ResourceSets geupdatet werden.
		List<IResource> listOfSets = new LinkedList<IResource>();
		
		for (int i = associatedSets; i > 0; i--) {
			for(IResource storeResourceList:resources.values()){
				Resource storeResource = (Resource) storeResourceList;
				if(storeResource.getType().equals(ResourceType.SET)){	
					ResourceSet resourceSet = (ResourceSet) storeResource;
					List<Resource> list = resourceSet.getRes();
					for(IResource sr:list){
						if(sr.getName().equals(item.getName())){
							listOfSets.add(storeResource);
							break;
						}
						
					}
				}
			} 
			
		}		
		for (IResource res:listOfSets){
			//entfernen der Ressource aus allen ResourceSets
			ResourceSet resSet = (ResourceSet) res;					
			resSet.removeResourceFromSet(item);
		}
	}

	public IResource getResource(String name){
		return resources.get(name);
	}
	
	// Instantiation of resources of type ResourceSet with a given amount
	public IResource instantiateResource(ResourceType type, String name, int amount){
		if(!type.equals(ResourceType.SET))
			throw new ParameterException("Can only instantiate a Resource Set with amount");
		if (alreadyExists(name)){
			throw new ParameterException("A resource with this name already exists!");
		}
		ResourceSet rs = new ResourceSet(name, amount);
		resources.put(name, rs);		
		informListenersOfResourceChange(rs);
		for(IResource res:rs.getRes()) {
			resources.put(res.getName(), res);
			informListenersOfResourceChange(res);
		}
		
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
		//System.out.println("There are " + result + " resources with prefix \"" + prefix + "\" available.");
		return result;
	}
	
	// Method for checking if a resource object with this name already exists
	public boolean alreadyExists(String name){		
		return resources.containsKey(name);
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
		testListenersList();
		listeners.add(listener);
	}
	
	public void removeResourceStoreListener(ResourceStoreListener listener){
		testListenersList();
		listeners.remove(listener);
	}
	
	private void informListenersOfResourceChange(IResource res){
		//Info Jascha: Methode informiert die grafische Oberflaeche, falls eine Resource hinzugefuegt wurde. Siehe gui/ResourceStoreGUI
		testListenersList();
		for (ResourceStoreListener listener:listeners)
			listener.resourceStoreElementAdded(res);
	}
	
	private void informListenersOfResourceRemoval(IResource res){
		//Info Jascha: Methode informiert die grafische Oberflaeche, falls eine Resource entfernt wurde
		testListenersList();
		for (ResourceStoreListener listener:listeners)
			listener.informStoreElementRemoved(res);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name=name;
		informListenerOfNameChange(name);
		
	}

	private void informListenerOfNameChange(String name2) {
		testListenersList();
		for (ResourceStoreListener listener:listeners)
			listener.nameChanged(name2);
		
	}
	
	public ResourceStore clone(){
		return (ResourceStore) new XStream().fromXML(new XStream().toXML(this));
	}
	
	private void testListenersList(){
		if(listeners==null)
			listeners=new LinkedList<>();
	}
}
