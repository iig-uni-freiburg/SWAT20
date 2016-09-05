package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;

public class CompoundResource extends Resource {

	private List<IResource> resources = new ArrayList<>(); //Resources that are part of this CompoundResource
	
	List<String> namesOfDuplicateSets = new ArrayList<>(); //List of ResourceSets that are multiple times in this CompoundResource
	Map<String, Integer> resourceSets = new HashMap<>(); // <Name, number of occurrences>
	List<Resource> usedSetResources = new ArrayList<>(); //Which resources of a set are used by using this CompoundResource
	
	public CompoundResource(String name, List<IResource> elements){
		super(name);
		this.resources=elements;
		type = ResourceType.COMPOUND;
	}
	
	//This one is being used
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
		//System.out.println("Checking availability for Resource: " + this.name);		
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
					break;
				}
			}
			if (duplicates){
				return checkAvailabilityWithDuplicateSets();
			}
		}	//TODO: Stimmt das??	
		for (IResource r : resources) {
			//System.out.println("No ResourceSets in this CompoundResource");
			if (!r.isAvailable()){
				return false;
				}			
		}
		return true;
	}
	
	private boolean checkAvailabilityWithDuplicateSets() {
		for (IResource r : resources) {
			if (r instanceof ResourceSet) {
				int dp = resourceSets.get(r.getName());
				if (dp > 1) {
					if (!(((ResourceSet) r).checkDuplicateAvailability(dp))) {
						return false;
					}
				} else if (!r.isAvailable()) {
					// System.out.println("It wasn't a multiset but it's
					// unavailable");
					return false;
				}
			} else if (!r.isAvailable()) {
				// System.out.println("the resource is no set but unavailable");
				return false;
			}
		}
		// System.out.println("Everything's fine, all resources are available");
		return true;
	}

	public List<IResource> getResources(){
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
	
	public void addResource(IResource r) {
		// If there is a Collision with the resource, it can't be added
		if (r instanceof ResourceSet) {
			addSetToResources((ResourceSet) r);
		}
		else if (CollisionDetection(r)) {
			System.out.println("Can't add " + r.getName() + " because there is a collision.");
			return;
		}

		/**
		 * else if (r instanceof CompoundResource){ List
		 * <String> thisCompoundList = getAllLinkedResourceNames(); List
		 * <String> newCompoundList = ((CompoundResource)
		 * r).getAllLinkedResourceNames();
		 * if(checkForIntersection(thisCompoundList, newCompoundList)){
		 * System.out.println("The resource " + r.getName() + " overlaps with "
		 * + name + " so it can't be added!"); } else { //the resources don't
		 * overlap, so it can be added resources.add(r); } }
		 */
		else {
			// possible duplicates have been already checked
			resources.add(r);
		}

		// Debug
		System.out.println("The resources in this compound resource are");
		for (IResource res : resources) {
			System.out.println(res.getName());
		}
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
		System.out.println("Shouldn't get here, use addResource(IResource, true) only");
		return false;
	}

	public boolean checkForDuplicateInCompound(IResource r){
		List<CompoundResource> compounds = new ArrayList<CompoundResource>();	
		for (IResource res:getResources()){
			if(res instanceof CompoundResource){
				compounds.add((CompoundResource)res);
			}
		}
		for (CompoundResource cr:compounds){
			System.out.println("There are CR in this CR!");
			if (cr.CollisionDetection(r)) return true;
		}		
		return false;
	}
	
	public boolean checkforDuplicateInSets(IResource r){
		String inputName = r.getName();
		//Take r and check if its present in one of the sets in resourceSets
		for (String resourceSetName:resourceSets.keySet()){
			for(IResource compoundElement:resources){
				if (resourceSetName.equals(compoundElement.getName())){
					if(((ResourceSet)compoundElement).checkIfResourceIsPart(inputName)){
						System.out.println("The resource "+ inputName +
								" is already in this CompoundResource (as part of " + resourceSetName +")");
						return true;
					}							
				}
			}
		}
		return false;
	}

	public void addSetToResources(ResourceSet r){	
		String resourceName = r.getName();
		if (resourceSets.containsKey(resourceName) && (resourceSets.get(resourceName) >= 1)){
			System.out.println("The Set is already inside this compound, so do no CollisionDetection");
			int i = resourceSets.get(resourceName);
			if (r.getSize() <= i){
				System.out.println("The ResourceSet is not big enough to be added to a CompoundResource more than "+i+" times!");
				return;
			} else {
				resourceSets.replace(resourceName, i+1);
				resources.add(r);
				return;
			}
		}		
		else if (checkIfPartOfSetIsAlreadyInside(r)){
			System.out.println("A resource which is part of this Set is already in the CompoundResource.\nPlease remove it first.");
			return;
		}		
		if ((!resourceSets.containsKey(resourceName)) || resourceSets.get(resourceName) == 0){
			resourceSets.put(resourceName, 1);
			resources.add(r);
		} 
		else {			
			System.out.println("The ResourceSet is " +resourceSets.get(resourceName)+" times in this compound but we should never get here!");					
		}
	}
	
	private boolean checkIfPartOfSetIsAlreadyInside(ResourceSet r) {		
		List<Resource> simpleList = new ArrayList<Resource>();
		simpleList = r.getRes();
		for (Resource res:simpleList){
			if (CollisionDetection(res)) return true;
		}
		return false;
		/**
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
		*/
	}

	public void removeResource(String res){
		IResource match=null;
		for (IResource r:this.getResources()){
			if (r.getName().equals(res)){
				match = r;
				String matchName = match.getName();
				if(resourceSets.containsKey(matchName)){					
					resourceSets.replace(matchName, resourceSets.get(matchName)-1);
					//System.out.println("Removed " + matchName + " from " + this.name +
					//		" Compound, duplicate value is: "+resourceSets.get(matchName));
					}
				removeResource(match);
				//return now so the set gets removed only once.
				return;		
			}
		}
	}
	
	public void removeResource(IResource r){
		if(r instanceof ResourceSet){
			removeSet(r);
		} else {
			resources.remove(r);
		}
	}

	//Method to remove a ResourceSet completely with all duplicates
	private void removeSet(IResource r) {
		ArrayList<IResource> removeList = new ArrayList<IResource>();
		for (IResource res:resources){
			if (res.equals(r)){
				removeList.add(res);
			}
		}
		resources.removeAll(removeList);
		namesOfDuplicateSets.remove(r.getName());
		resourceSets.remove(r.getName());
		
	}

	@Override
	public void use() {		
		for (IResource r: resources){			
			//if the Resource is a set, save which resource of the set is used
			if (r instanceof ResourceSet){				
				Resource resourceToUse =((ResourceSet)r).getRandomAvailable();
				resourceToUse.use();
				usedSetResources.add(resourceToUse);			
			} else {
				r.use();	
			}			
		}
	}

	@Override
	public void unUse() {
		//Unuses all resources directly and also unuses underlying simple/human resources from ResourceSets
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
	
	public boolean CollisionDetection(IResource r){
		ResourceType type = ((Resource)r).getType();
		switch (type) {		
		case SIMPLE:
			//Test for collision with resources in: Compounds, Sets and in this resource's list
		case HUMAN:	
			//Test for collision with resources in: Compounds, Sets and in this resource's list
			if (getResources().contains(r)) return true;
			if (checkforDuplicateInSets(r)) return true;	
			if (checkForDuplicateInCompound(r)) return true;
			break;
			
		case SHARED:
			//Test for collision with resources in: Compounds and in this resource's list
			if (getResources().contains(r)) return true;
			if (checkForDuplicateInCompound(r)) return true;
			break;
			
		case SET:
			//Test for collision with resources in: Compounds and in this resource's list
			if (getResources().contains(r)) return true;	
			if (checkForDuplicateInCompound(r)) return true;
			//Test contents of ResourceSet for collisions
			if (checkIfPartOfSetIsAlreadyInside((ResourceSet)r)) return true;
			break;
			
		case COMPOUND:
			//Test for collision with resources in: Compounds and in this resource's list
			if (getResources().contains(r)) return true;			
			if (checkForDuplicateInCompound(r)) return true;
			//Test contents of CompoundResource for collisions
			if(checkCompoundContentsForCollison((CompoundResource)r)) return true;
			break;
			
		default:
			System.out.println("Missing method for collision detection for type " + type + ".");
			break;
		}
		System.out.println("No collision for "+r.getName()+ " detected");
		return false;
	}

	private boolean checkCompoundContentsForCollison(CompoundResource r) {		
		for (IResource res:r.getResources()){
			if (CollisionDetection(res)) return true;			
		}		
		return false;
	}
}
