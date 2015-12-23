package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.TimeRessourceContext;

public class AwesomeResourceContext implements IResourceContext{
	
	String name;
	String resourceStoreName;
	
	//beinhaltet Liste mit Ressourcen-Objekten. Ressourcen-Objekt kann entweder selbst eine Liste haben oder eine einzelne Resource darstellen
	
	//TODO: Die Map darf keine Iresource Objekte haben. Beim Speichern und Laden, geht sonst die Zuweisung der Objekte zwischen 
	//Resource-Store und der resources-Liste verloren. Stattdesssen: Mit <String> arbeiten. GetResourceMethode in ResourceStore
	Map<String,List<IResource>> resources = new HashMap<>(); //<Aktivity,Resources>
	
	//Objekt, das eine Hashmap mit allen existierenden Ressourcen enth�lt.
	@XStreamOmitField
	ResourceStore resourceStore = new ResourceStore();
	
	
	public AwesomeResourceContext() {
		// TODO Auto-generated constructor stub
	}
	
	public ResourceStore getResourceStore(){
		return resourceStore;
	}
	
	public void setResourceStore(ResourceStore resourceStore) {
		this.resourceStore = resourceStore;
		resourceStoreName=resourceStore.getName();
	}
	
	public String getResourceStoreName(){
		return resourceStoreName;
	}

	@Override
	public boolean isAvailable(String ressourceName) {
		for (IResource o:resources.get(ressourceName)){
			if(!o.isAvailable()) return false;
		}
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	// Sollte man hier noch �berpr�fen, ob Ressourcen �berhaupt geblockt werden k�nnen? Also if (resource.isAvailable() == true) ?
	@Override
	public void blockResources(List<String> resources) {
		//System.out.println("Blocking "+printList(resources));
		for(String resource:resources){ //this can be done way faster by holding a seperate hashmap of resources!
						
			IResource r = getResource(resource);
			if (r.isAvailable()){
				//System.out.println("Setting: Blocking "+resource);
				r.use();
			}
			else {
				//Throw error that r can't be blocked because it's already in use
			} 
		}
		
		/* Alternative mit ResourceStore
		 * for(String resource:resources){
			IResource r = resourceStore.getResource(resource);
			if (r.isAvailable()){
				r.use();
			}
			else {
				//Throw error that r can't be blocked because it's already in use
			}
		} */
		
		
	}
	// Wof�r ist das gedacht?
	private String printList(List<String> resources2) {
		String result ="";
		for(String s:resources2)
			result+=s+" ";
		return result;
	}

	@Override
	public void unBlockResources(List<String> resources) {
		if(resources==null||resources.isEmpty()) return;
		//System.out.println("Freeing "+printList(resources));
		for(String resource:resources){ //this can be done way faster by holding a seperate hashmap of resources!
			//System.out.println("Unblocking "+resource);
			getResource(resource).unUse();
		}
		
		/* Alternative mit ResourceStore
		for(String resource:resources){
			resourceStore.getResource(resource).unUse();
		}
		*/
		
	}
	
	protected IResource getResource(String resource){
		//this can be done way faster by holding a seperate hashmap of resources!
		for(List<IResource> res:resources.values()){
			for(IResource r:res){
				if(r.getName().equals(resource))
					return r;
			}
		}
		
		return new SimpleResource("dummy");
		
		/* Alternative mit Liste aller Ressourcen im ResourceStore
		 *  
		 * return resourceStore.getResource(resource);
		 */
		

	}

	@Override
	public List<List<String>> getAllowedResourcesFor(String activity) {
		//TODO: diese Methode
		return null;

	}
	
	public List<IResource> getKnownResourcesFor(String activity) {
		List<IResource> possibleResources = resources.get(activity);
		if (possibleResources == null)
			return new LinkedList<>();
		LinkedList<IResource> result = new LinkedList<>();
		for (IResource res : possibleResources) {
			result.add(res);
			if (res instanceof ResourceSet) {
				for (IResource resSet : ((ResourceSet) res).resources) {
					result.add(res);
				}
			}
		}
		return result;
	}

	@Override
	public List<String> getRandomAllowedResourcesFor(String activity, boolean blockResources) {
		if(!resources.containsKey(activity)) {
			//return dummy resource
			ArrayList<String> dummy = new ArrayList<>(1);
			dummy.add("dummy");
			return dummy;
		}
		List<IResource> possibleResources = resources.get(activity);
		LinkedList<String> result = new LinkedList<>();
		for (IResource possibleResource : possibleResources) {
			if (possibleResource.isAvailable()) {
				result.add(possibleResource.getName());
				if(blockResources){
					//System.out.println("Blocking "+possibleResource.getName());
					possibleResource.use();
				}
				//System.out.println("Blocking "+printList(result));
				return result;
			}
		}
		return null;
	}

	@Override
	public IResource getResourceObject(String resourceName) {
		return getResource(resourceName);
	}
	
	public void addResourceUsage(String activity, IResource resource){
		//TODO: nur Resourcen aus dem ResourceStore nutzen
		//Prüfen, ob Resourcen im ResourceStore vorhanden
		if(resources.containsKey(activity)){
			resources.get(activity).add(resource); //TODO: check if resource is already inside the list!
		} 
		else {
			ArrayList<IResource> list = new ArrayList<>();
			list.add(resource);
			resources.put(activity, list);
		}
	}

	@Override
	public boolean containsBlockedResources() {
		for(List<IResource> resourceList:resources.values()){
			for(IResource resource:resourceList){
				if(!resource.isAvailable())
					return true;
			}
		}
		return false;
	}

	@Override
	public void reset() {
		for(List<IResource> resourceList:resources.values())
			for(IResource res:resourceList)
				res.reset();
	}
	
	public String toString(){
		StringBuilder b= new StringBuilder();
		for(String s:resources.keySet()){
			b.append(s+": ");
			for(IResource res: resources.get(s)){
				b.append(res.getName()+"("+res.isAvailable()+")");
			}
			b.append("\r\n");
		}
		return b.toString();
	}

	@Override
	public void setName(String name) {
		this.name=name;
		
	}
	
	public void clearUsageFor(String activity){
		if(resources.containsKey(activity)){
			if(resources.get(activity)!=null){
				resources.get(activity).clear();
			}
		}
	}
	
	public Set<String> getContainingActivities(){
		return resources.keySet();
	}
	
	public AwesomeResourceContext clone(){
		AwesomeResourceContext clone = (AwesomeResourceContext) new XStream().fromXML(new XStream().toXML(this));
		clone.setResourceStore(getResourceStore());
		return clone;
	}

}
