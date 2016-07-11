package de.uni.freiburg.iig.telematik.swat.jascha;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.TimeRessourceContext;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;
import de.uni.freiburg.iig.telematik.sewol.parser.LogParser;
import de.uni.freiburg.iig.telematik.sewol.parser.LogParsingFormat;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.logs.SwatLogType;
import de.uni.freiburg.iig.telematik.swat.plugin.sciff.LogParserAdapter;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;

public class AwesomeResourceContext implements IResourceContext{
	
	String name;
	String resourceStoreName;
	
	//beinhaltet Liste mit Ressourcen-Objekten. Ressourcen-Objekt kann entweder selbst eine Liste haben oder eine einzelne Resource darstellen
	
	//AENDERUNG: Die Map darf keine Iresource Objekte haben. Beim Speichern und Laden, geht sonst die Zuweisung der Objekte zwischen 
	//Resource-Store und der resources-Liste verloren. Stattdesssen: Mit <String> arbeiten. GetResourceMethode in ResourceStore
	Map<String,List<String>> resources = new HashMap<>(); //<Activity,Resources> Combined with OR
	
	//Objekt, das eine Hashmap mit allen existierenden Ressourcen enthaelt.
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
		return resourceStore.getResource(ressourceName).isAvailable();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void blockResources(List<String> resources) {
		//System.out.println("Blocking "+printList(resources));
		for(String resource:resources){
						
			IResource r = getResource(resource);
			if (r.isAvailable()){
				//System.out.println("Setting: Blocking "+resource);
				r.use();
			}
			else {
				//Throw error that r can't be blocked because it's already in use
				throw new ParameterException("The resource "+r.getName()+" can't be blocked because it's already in use");
			} 
		}		
		
	}
	
	public void getResourcesFromFile(File logFile) throws ParserException, Exception{

		getResourcesFromFile(new LogModel(logFile));
	}
	
	public void getResourcesFromFile(LogModel model) throws Exception {
		HumanResourceExtractor hExtractor;
		MaterialExtractor mExtractor;
		ActivityCompoundExtractor acExtractor;
		List<LogTrace<LogEntry>> log = getLog(model);
		
		// get an Object with all distinct originators (= human resources)
		//hExtractor = new HumanResourceExtractor(model);
		hExtractor = new HumanResourceExtractor(log);
		
		// Object with distinct materials from the log
		mExtractor = new MaterialExtractor(log);
		
		//Object with HashSet containing combinations of occuring Activity/user/material triples
		acExtractor = new ActivityCompoundExtractor(log);		
		
		// createResource objects in the resource store
		resourceStore.addHumanResourcesFromExtractor(hExtractor);
		resourceStore.addMaterialsFromExtractor(mExtractor);
		resourceStore.addCompoundsFromExtractor(acExtractor);

		createHumanActivityEntries(log);
		createActivityCompoundEntries(acExtractor);

	}
	
	private void createActivityCompoundEntries(ActivityCompoundExtractor acExtractor) {
		for (Compound compound: acExtractor.getCompoundSet()){
			addResourceUsage(compound.getActivity(), resourceStore.getResource(compound.getHuman()+compound.getMaterial()));
		}
		
	}

	private void createHumanActivityEntries(List<LogTrace<LogEntry>> log){		
		for (LogTrace<LogEntry> trace : log) {
			for (LogEntry logEntry : trace.getEntries()) {
				try {
					addResourceUsage(logEntry.getActivity(), resourceStore.getResource(logEntry.getOriginator()));
				} catch (ParameterException e) {
					// Could not find resource. Go to next resource
				}

			}
		}		
	}
	
	private List<LogTrace<LogEntry>> getLog(LogModel model) throws Exception{
		switch (model.getType()) {
		case MXML:
		case XES:
			LogParserAdapter adapter = (LogParserAdapter) model.getLogReader();
			return adapter.getOriginalLog().getFirstParsedLog();
		default:
			throw new Exception("Can only parse XES or MXML logs");
		}
	}
	
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
		for(String resource:resources){
			//System.out.println("Unblocking "+resource);
			if(resourceStore.getResource(resource)!=null)
				getResource(resource).unUse();
		}
		
	}
	
	protected IResource getResource(String resource) {
		IResource result = resourceStore.getResource(resource);
		if (result==null) throw new NullPointerException("Resource "+resource+" not in Resource Store!");
		return result;
	}

	
	public List<String> getAllowedResourcesFor(String activity) {
		List<String> result = new ArrayList<String>();
		result.addAll(resources.get(activity));
		return result;
	}
	
	public List<IResource> getKnownResourcesFor(String activity) {
		List<String> possibleResources = resources.get(activity);
		if (possibleResources == null)
			return new LinkedList<>();
		LinkedList<IResource> result = new LinkedList<>();
		for (String s : possibleResources) {
			IResource res = getResource(s);
			result.add(res);
			if (res instanceof ResourceSet) {
				for (IResource resSet : ((ResourceSet) res).resources) {
					result.add(resSet);
				}
			}
		}
		return result;
	}

	@Override
	public List<String> getRandomAvailableResourceSetFor(String activity, boolean blockResources) {
		if(!resources.containsKey(activity)) {
			//return dummy resource
			ArrayList<String> dummy = new ArrayList<>(1);
			dummy.add("dummy");
			return dummy;
		}
		List<IResource> possibleResources = getResourceList(activity);
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
	
	private List<IResource> getResourceList(String activity){
		List<String> resourceList = resources.get(activity);
		ArrayList<IResource> result= new ArrayList<>();
		for(String resourceString:resourceList){
			result.add(getResource(resourceString));
		}
		return result;
	}

	@Override
	public IResource getResourceObject(String resourceName) {
		return getResource(resourceName);
	}
	
	public void addResourceUsage(String activity, IResource resource){
		String resourceName = resource.getName();
		if(resourceStore.resources.containsKey(resourceName)){
				
			if(resources.containsKey(activity)){
				if(resources.get(activity).contains(resourceName)){
					//throw new ParameterException("Can't add " + resourceName + " because it's already inside the list");
				} 	else {
					resources.get(activity).add(resourceName);
				}
			
			} 
		else {
			ArrayList<String> list = new ArrayList<>();
			list.add(resourceName);
			resources.put(activity, list);
			}
		}
		else throw new ParameterException("The resource "+resourceName+" can't be added to the activity because it's not in the store");
	}
	@Override
	public boolean containsBlockedResources() {
		for(List<String> resourceList:resources.values()){
			for(String resource:resourceList){
				if(!isAvailable(resource))
					return true;
			}
		}
		return false;
	}

	@Override
	public void reset() {
		for(List<String> resourceList:resources.values())
			for(String res:resourceList){
				getResource(res).reset();
			}
	}
	
	public String toString(){
		StringBuilder b= new StringBuilder();
		for(String s:resources.keySet()){
			b.append(s+": ");
			for(String res: resources.get(s)){
				b.append(res+"("+isAvailable(res)+")");
			}
			b.append("\r\n");
		}
		return b.toString();
	}

	@Override
	public void setName(String name) {
		this.name=name;
		
	}
	
	public void clearUsageFor(String activity) {
		resources.remove(activity);
	}
	
	public Set<String> getContainingActivities(){
		return resources.keySet();
	}
	
	public AwesomeResourceContext clone(){
		AwesomeResourceContext clone = (AwesomeResourceContext) new XStream().fromXML(new XStream().toXML(this));
		clone.setResourceStore(getResourceStore());
		return clone;
	}
	
	public Set<String> getAllActivities(){
		Set<String> result = resources.keySet();
		return result;
	}
	
	public void renameResource(String oldName, String newName){
		resourceStore.renameResource(oldName, newName);
		
		
		//List<String> oldRes = resources.remove(oldName);
		//resources.put(newName, oldRes);
		
		//update resource references
		for(List<String> resource: resources.values()){
			if (resource!=null && resource.contains(oldName)){
				resource.remove(oldName);
				resource.add(newName);
			}
		}
		try {
			SwatComponents.getInstance().getResourceContainer().storeComponent(getName());
		} catch (ProjectComponentException e) {
			Workbench.errorMessage("Could not save resource context "+getName(), e, true);
		}
	}

	@Override
	public boolean needsResources(String name) {
		return resources.containsKey(name);
	}

}
