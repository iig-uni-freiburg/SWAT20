package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import de.invation.code.toval.misc.NamedComponent;
import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.ResourceStoreListener;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;

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
		
	public void addHumanResourcesFromExtractor(HumanResourceExtractor extractor){
		
			List<String> humanResources = extractor.getHumanResources();
			for (String name: humanResources){
				instantiateResource(ResourceType.HUMAN, name);
			}
	}
	
	public void addMaterialsFromExtractor(MaterialExtractor extractor){
			List<String> materials = extractor.getMaterialNames();
			for (String name: materials){
				instantiateResource(ResourceType.SIMPLE, name);
			}
	}
	
	public void addHumanResourcesFromModel(LogModel model){
		HumanResourceExtractor extractor;
		try {
			extractor = new HumanResourceExtractor(model);
			List<String> humanResources = extractor.getHumanResources();
			for (String name: humanResources){
				instantiateResource(ResourceType.HUMAN, name);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void removeResource(IResource input) {
		Resource item = (Resource) input;
		ResourceType type = item.getType();
		boolean successfulRemoval = false;
		switch (type) {
		case SET:
			//Remove the ResourcSet but leave the contents untouched
			updateReferences(input);
			resources.remove(input.getName());
			successfulRemoval = true;
			break;
			
		case HUMAN:
		case SIMPLE:
			//Check if the humane/simple resource is part of a set or a compound, remove accordingly
			updateReferencesForSimple(input);
			resources.remove(input.getName());
			successfulRemoval = true;
			break;

		case COMPOUND:
			//Just remove the compound resource, leave the contents of it untouched
			resources.remove(input.getName());
			updateReferences(input);
			successfulRemoval = true;
			break;
			
		case SHARED:
			//Check if the shared resource is part of a compound, remove accordingly
			updateReferences(input);
			resources.remove(input.getName());
			successfulRemoval = true;
			break;
			
		default:
			System.out.println("Missing method to remove resources of type: "+type.toString());
			successfulRemoval = false;
			break;
		}
		if (successfulRemoval){
			//The Resource was removed successful, so we can inform the listener about it
			informListenersOfResourceRemoval(item);
		}
	}
	
	public void changeResource(IResource old, IResource newResource) throws Exception{
		//if(!old.getName().equals(newResource.getName()))
		//	throw new Exception("Resources must have the same name");
		for(IResource res:resources.values()){
			if(res instanceof CompoundResource){
				((CompoundResource)res).changeResource(old, newResource);
			}
		}
		resources.remove(old);
		informListenersOfResourceRemoval(old);
		resources.put(newResource.getName(), newResource);
		informListenersOfResourceChange(newResource);
	}
	
	//This method also works for removal of CompoundResource, ResourceSet and SharedResource
	private void updateReferences(IResource input) {
		List<IResource> compounds = new ArrayList<IResource>();
		for (IResource r:this.getAllResources()){
			if (r instanceof CompoundResource){
				compounds.add(r);
			}
		}
		for (IResource compound:compounds){
			updateReferenceInCompound((CompoundResource)compound, input);
		}		
	}

	private void updateReferencesForSimple(IResource input) {
		//Make 2 lists, one containing compounds, one containing Sets
		//for each list check each element if input is part of it. if so, update the contents of the compound/set
		List<Resource> compounds = new ArrayList<Resource>();
		List<Resource> sets = new ArrayList<Resource>();
		for (IResource r:this.getAllResources()){
			Resource res = (Resource)r;
			if (res.getType().equals(ResourceType.COMPOUND)){
				compounds.add(res);
			}
			else if (res.getType().equals(ResourceType.SET)){
				sets.add(res);
			}
			else {
				//not a SET or COMPOUND, don't add it anywhere
			}
			for (Resource set:sets){
				//create method to remove resources from sets
				updateReferenceInSet((ResourceSet)set, input);
			}
			for (Resource compound:compounds){
				//create method to remove resources from Compounds
				updateReferenceInCompound((CompoundResource)compound, input);
			}			
		}
	}

	private void updateReferenceInCompound(CompoundResource compound, IResource input) {
		//compound.removeResource(input.getName());
		if(compound.getResources().contains(input)){
			compound.removeResource(input);
		}		
	}

	private void updateReferenceInSet(ResourceSet set, IResource input) {
		if(set.checkIfResourceIsPart(input.getName())){
			set.removeResource((Resource)input);
		}		
	}

	private void removeResourceSet(IResource item) {
		ResourceSet rs = (ResourceSet) item;
		for (Resource res:rs.getRes()){
			SimpleResource sr = (SimpleResource)res;
			
			if (sr.getAssociatedResourceSets() < 1){
				//Something went wrong, shouldn't get here
			}
			// If the simpleResource is associated with only this set it is removed
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
		if (resources.containsKey(name)){
			return resources.get(name);
		}
		else {
			return null;
		}
	}
	
	// Instantiation of resources of type ResourceSet with a given amount
	public IResource instantiateResource(ResourceType type, String name, int amount){
		if(!type.equals(ResourceType.SET))
			throw new ParameterException("Can only instantiate a Resource Set with amount");
		if (contains(name+"-Set")){
			throw new ParameterException("A resource with this name already exists!");
		}
		ResourceSet rs = new ResourceSet(name, amount);
		resources.put(rs.getName(), rs);		
		informListenersOfResourceChange(rs);
		for(IResource res:rs.getRes()) {
			resources.put(res.getName(), res);
			informListenersOfResourceChange(res);
		}
		
		return rs;
	}
	
	// Instantiating all types of Resources when they are given a type and a name only
	public IResource instantiateResource(ResourceType type, String name){
		if (contains(name)){
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
			//We don't inform the listener of a resource change because it's done in the following call of instantiateResource
			//So result=null remains, else the listener would add it twice
			instantiateResource(type, name, 1);
			break;
		case HUMAN:
			result = new HumanResource(name);
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
		default:
			System.out.println("Missing constructor for this type: " + type.toString());
			break;
		}
		if (result instanceof Resource){
			informListenersOfResourceChange(result);
			}		
		return result;
	}
	
	// Compound Resource with List of IResource Objects as content
	public IResource instantiateResource(ResourceType type, String name, List<IResource> elements){
		if (!type.equals(ResourceType.COMPOUND)){
			throw new ParameterException("The resource needs to be of type COMPOUND");
		}
		if (!contains(name)){
			CompoundResource cr = new CompoundResource(name, elements);
			resources.put(name, cr);
			//throw new ParameterException("A resource with this name already exists!");
		}

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
	public boolean contains(String name){		
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
		
	public IResource[] getAllResourcesSortedByName(){
		IResource[] result = new IResource[resources.values().size()];
		String[] names = new String[resources.values().size()];
		int i = 0;
		for(IResource res:resources.values()){
				names[i]=res.getName();
				i++;
		}
		
		try{
			Arrays.sort(names);
		} catch (NullPointerException e) {
			System.out.println("There is a problem with the sorting of resources");
			return getAllResources();
		}
		int j = 0;
		for (String name:names){
			result[j] = resources.get(name);
			j++;
		}
		System.out.println("Sorting of resources ended well");
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

	public void addCompoundsFromExtractor(ActivityCompoundExtractor acExtractor) {
		String user;
		String material;
		LinkedList<IResource> components; 
		for (Compound element:acExtractor.getCompoundSet()){
			user = element.getHuman();
			material = element.getMaterial();
			components = new LinkedList<IResource>();
			components.add(this.getResource(user));
			components.add(this.getResource(material));			
			instantiateResource(ResourceType.COMPOUND, user+material, components);
			
		}
		
	}
	
	public void renameResource(String oldName, String newName){
		IResource res = getResource(oldName);
		resources.remove(oldName);
		informListenersOfResourceRemoval(res);
		res.setName(newName);
		resources.put(newName, res);
		informListenersOfResourceChange(res);
		try {
			SwatComponents.getInstance().getResourceStoreContainer().storeComponent(getName());
		} catch (ProjectComponentException e) {
			Workbench.errorMessage("Could not save resource store "+getName(), e, true);
		}
	}
}
