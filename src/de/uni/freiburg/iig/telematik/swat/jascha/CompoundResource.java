package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	public List<String> getAllLinkedResourceNames(){
		List<String> resultList = new ArrayList<String>();
		Set<String> resultSet = new HashSet<String>();
		for (IResource r:resources){
			if (r instanceof SimpleResource || r instanceof HumanResource || r instanceof SharedResource){
				resultSet.add(r.getName());
			} 
			else if (r instanceof ResourceSet){
				resultSet.addAll(((ResourceSet)r).getResNames());
			}
			else if (r instanceof CompoundResource){
				resultSet.addAll(((CompoundResource)r).getAllLinkedResourceNames());
			}
		}
		for (String s:resultSet){
			resultList.add(s);
		}
		
		return resultList;
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

			if (r instanceof ResourceSet){
				addSetToResources((ResourceSet)r);
					}
			else if (r instanceof CompoundResource){
				List<String> thisCompoundList = getAllLinkedResourceNames();
				List<String> newCompoundList = ((CompoundResource) r).getAllLinkedResourceNames();
				if(checkForIntersection(thisCompoundList, newCompoundList)){
					System.out.println("The resource " + r.getName() + " overlaps with " + name + " so it can't be added!");
				}
				else {
					//the resources don't overlap, so it can be added
					resources.add(r);
				}
			}
			else {
				if (!checkForDuplicates(r)) {
						//Resource is not a duplicate, add it to compound
						resources.add(r);	
					}
					//the resource is already part of the CompoundResource, don't add it again.
				}
			//Debug
			System.out.println("The resources in this compound resource are");
			for (IResource res:resources){
				System.out.println(res.getName());
				}		
	}
	
	private boolean checkForIntersection(List<String> thisCompoundList, List<String> newCompoundList) {
		for (String outer:thisCompoundList){
			for (String inner:newCompoundList){
				if (inner.equals(outer)){
					return true;
				}
			}
		}
		return false;
	}

	public boolean addResource(IResource r, boolean successNecessary){
		if (successNecessary){
			int lengthOld = resources.size();
			System.out.println("LengthOld is "+lengthOld);
			addResource(r);
			System.out.println("New length is "+ resources.size());
			if (lengthOld < resources.size()){
				return true;
			}
			return false;
		}
		//should never get here;
		System.out.println("Shouldn't get to here, use addResource(IResource r, true) only");
		return false;

	}
	
	private boolean checkForDuplicates(IResource r) {
		String inputName = r.getName();
		
		if (!resourceSets.isEmpty() && (r instanceof SimpleResource || r instanceof HumanResource)){
			//Take r and check if its present in one of the sets in resourceSets
			for (String resourceSetName:resourceSets.keySet()){
				for(IResource compoundElement:resources){
					if (resourceSetName.equals(compoundElement.getName())){
						if(((ResourceSet)compoundElement).checkIfResourceIsPart(inputName))
							System.out.println("The resource "+ inputName +
									" is already in this CompoundResource (as part of " + resourceSetName +")");
							return true;
					}
				}
			}
		}
		
		for (IResource res:resources){
			if (inputName.equals(res.getName())){
				//there is a duplicate resource which is not a ResourceSet
				return true;
			}
			
		}
		return false;
	}

	public void addSetToResources(ResourceSet r){
		
		if (checkIfPartOfSetIsAlreadyInside(r)){
			System.out.println("A resource which is part of this Set is already in the CompoundResource.\nPlease remove it first.");
			return;
		}
		
		String resourceName = r.getName();
		if((!resourceSets.containsKey(resourceName)) || resourceSets.get(resourceName) == 0){
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
	
	private boolean checkIfPartOfSetIsAlreadyInside(ResourceSet r) {
		List<IResource> simpleHumanList = new ArrayList<IResource>();
		for (IResource res:resources){
			if (res instanceof SimpleResource || res instanceof HumanResource){
				simpleHumanList.add(res);
			}
		}
		for (IResource possibleCandidate:simpleHumanList){
			if (r.checkIfResourceIsPart(possibleCandidate.getName())){
				return true;
			}
		}
		return false;
	}

	public void removeResource(String res){
		IResource match=null;
		for (IResource r:resources)
			if (r.getName().equals(res)){
					match = r;
					String matchName = match.getName();
					if(resourceSets.containsKey(matchName)){					
						resourceSets.replace(matchName, resourceSets.get(matchName)-1);
						System.out.println("Removed " + matchName + " from " + this.name +
								" Compound, duplicate value is: "+resourceSets.get(matchName));
				}
					removeResource(match);
					//return now so the set gets removed only once.
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
