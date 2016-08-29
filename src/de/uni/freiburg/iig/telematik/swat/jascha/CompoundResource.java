package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;

public class CompoundResource extends Resource {

	List<IResource> resources = new ArrayList<>();
	List<String> namesOfDuplicateSets = new ArrayList<>();
	Map<String, Integer> resourceSets = new HashMap<>(); // <Name, number of occurrences>
	List<Resource> usedSetResources = new ArrayList<>();
	
	public CompoundResource(String name, List<IResource> elements){
		super(name);
		this.resources=elements;
		type = ResourceType.COMPOUND;
	}
	
	public CompoundResource(String name){
		super(name);
		type = ResourceType.COMPOUND;
	}
	
	//Konstruktor, bei dem die Ressource gleich in ein ResourceStore eingetragen wird.
	public CompoundResource(String name, ResourceStore resourceStore){
		super(name);
		resourceStore.addResource(this);
		type = ResourceType.COMPOUND;
	}

	@Override
	public boolean isAvailable() {
		
		System.out.println("Checking availability for Resource: " + this.name);
		
		if(isDisabled)
			return false;
		
		if (!resourceSets.isEmpty()){
			//System.out.println("There are ResourceSets in this CompoundResource!");
			boolean duplicates = false;
			//System.out.print(resourceSets.toString());
			for (int i: resourceSets.values()){
				//System.out.println(i);
				if (i > 1){
					//System.out.println("There is a duplicate set!");
					duplicates = true;
				}
			}
			if (duplicates){
				return checkAvailabilityWithDuplicateSets();
			}
		} else {
		for (IResource r : resources) {
			//System.out.println("No ResourceSets in this CompoundResource");
			if (!r.isAvailable()){
				return false;
				}	
				
			}
		}
		return true;
	}
	
	private boolean checkAvailabilityWithDuplicateSets() {
		
			//System.out.println("Checking for availability with duplicates");
			for (IResource r:resources){
				if(((Resource)r).getType() == ResourceType.SET){
					//System.out.println("Its a set!");
					int dp = resourceSets.get(r.getName());
					if (dp > 1){
						//System.out.println("There are more than one");
						if(!(((ResourceSet)r).checkAvailabiltyWithDuplicates(dp))){
							System.out.println("The DupliSet is not available");
							return false;
						}
						
					} else if (!r.isAvailable()) {
						System.out.println("It wasnt a MultiSet but it's unavailable");
						return false;
					}
				} 
				else if(!r.isAvailable()){
					System.out.println("No set, but unavailable");
					return false;
				}
			}			
		System.out.println("Everything's fine, all Res are available");
		return true;
		
	}

	public List<IResource> getConsistingResources(){
		return resources;
	}

	public String getName() {
//		StringBuilder b = new StringBuilder();
//		for (IResource r : resources) {
//			b.append(r.getName());
//			b.append(", ");
//		}
//		String result = b.toString();
//		if(result!=null && result.length()>1)result = result.substring(0, result.length()-1); //remove ", ";
//		if(result==null || result.isEmpty())
//			return super.getName();
//		return super.getName()+": "+result;
		return super.getName();
	}
	
	
	public void addResource(IResource r){
		
		if (((Resource)r).getType() == ResourceType.SET){
			addSetToResources((ResourceSet)r);
			}
			else {
				resources.add(r);		
			}
		//Debug
		System.out.println("The resources in this compound resource are");
		for (IResource res:resources){
			System.out.println(res.getName());
			}		
	}
	
	public void addSetToResources(ResourceSet r){
		String resourceName = r.getName();
		if((!resourceSets.containsKey(resourceName)) || resourceSets.get(resourceName) == 0){
			//System.out.println("Created Set entry in Compound");
			resourceSets.put(resourceName, 1);
			resources.add(r);
		} 
		else	{
			for (String res:resourceSets.keySet()){
				if (res.equals(resourceName)){
					int i = resourceSets.get(resourceName);
					System.out.println(r.getSize());
					if (r.getSize() <= i){
						System.out.println("The ResourceSet is not big enough to be added to a CompoundResource more than " +i+" times!");
						return;
					} else {
						//System.out.println("Old Value is: " + resourceSets.get(resourceName));
						resourceSets.replace(resourceName, i+1);
						//System.out.println("New Value is: " + resourceSets.get(resourceName))
						System.out.println("The ResourceSet is now "+(i+1)+" times in this CompoundResource");
						resources.add(r);
					}

					
				}
			}	
			
		}
	}
	
	public void removeResource(String res){
		IResource match=null;
		for (IResource r:resources)
			if (r.getName().equals(res)){
					match = r;
		
					if(resourceSets.containsKey(match.getName())){					
						resourceSets.replace(match.getName(), resourceSets.get(match.getName())-1);
						System.out.println("Removed Set from Compound, duplicate value is: "+resourceSets.get(match.getName()));
				}
					removeResource(match);
					return;
			}
	}
	
	public void removeResource(IResource r){
		resources.remove(r);
	}

	@Override
	public void use() {		
		for (IResource r: resources){
			
			//if the Resource is a set, save which resource of the set is used
			if (((Resource)r).getType() == ResourceType.SET){				
				Resource resourceToUse =((ResourceSet)r).getRandomAvailable();
				resourceToUse.use();
				usedSetResources.add(resourceToUse);
				//namesOfUsedSetResources.add(resourceToUse.getName());
				
			} else {
				r.use();	
			}
			
		}
	}

	@Override
	public void unUse() {
		//throw new RuntimeException("Not yet implemented");		
		for (IResource r: resources){			
			r.unUse();
		}	
		for (Resource res: usedSetResources){
			res.unUse();
		}
	}

	@Override
	public void reset() {
		for(IResource r:resources)
			r.reset();
		usedSetResources.clear();
	}
	
	public String toString(){
		return "(" + name + ")";
	}

}
