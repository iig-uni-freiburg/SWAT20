package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;

// Resource Sets are Ressources that group similar resources of type SIMPLE or HUMAN.
// If an activity uses a resource set it means it can use ANY ONE resource of the resources in the set.
// IDEA: If more than one resource of a set (but not specific resources) are needed, make a compound resource where the set is added several times.
public class ResourceSet extends Resource {
	
	private List<Resource> resources = new LinkedList<>();
	private int size = 0;
	
	public int getSize() {
		return size;
	}

	public List<Resource> getRes() {
		return resources;
	}
	
	public List<String> getResNames(){
		List<String> result = new ArrayList<String>();
		for (Resource r:resources){
			result.add(r.getName());
		}
		return result;
	}

	public ResourceSet(String name, int amount) {
		this.name = name+"-Set";
		this.type=ResourceType.SET;
		this.size = amount;
		for (int i = 0;i<amount;i++){
			resources.add(new SimpleResource(name+"-"+i, true));
		}
	}
	
	public void addResource(IResource input){
		if (input instanceof SimpleResource) {
			SimpleResource sr = (SimpleResource)input;
			sr.updateAssociatedSets(UpdateType.INCREASE);
			resources.add(sr);
		}
		else if (input instanceof HumanResource){
			HumanResource hr = (HumanResource)input;
			hr.updateAssociatedSets(UpdateType.INCREASE);
			resources.add(hr);
		}
		else {
			throw new ParameterException("Can only put resources of type SIMPLE and HUMAN into resource sets");
		}
		size = resources.size();

	}
	
	public void removeResourceFromSet(Resource item){
		SimpleResource sr = (SimpleResource)item;		
		if (sr.getAssociatedResourceSets() < 1){
			throw new ParameterException("Can't remove resource from set because it's not part of one");
		}		
		if (sr.getAssociatedResourceSets() >= 1)
			{
				sr.updateAssociatedSets(UpdateType.DECREASE);
				resources.remove(item);
				size--;
			}		
		}
	
	public void removeResource(Resource item){
		resources.remove(item);
		size--;
	}

	//A ResourceSet is available if ONE of the contained resources is available!
	@Override
	public boolean isAvailable() {	
		  if (isDisabled) return false;
		 return isOneAvailable();		 
		/**
		  if(isDisabled)		 
			return false;		
		for (Resource r:resources){
			if (!r.isAvailable()){
				return false;
			}
		}
		return true;
		*/
	}

	//Using a ResourceSet means using ONE of the contained resources
	@Override
	public void use() {
		
		getRandomAvailable().use();
		
		/**
		for (Resource r:resources){
			r.use();
		}
		*/

	}
	
	//To be able to tell which Resource of a resource set has to be used/unused 
	//we have to "pierce" through the set to the contained simple/human resources
	public String getResourceNameToUse(){		
		return getRandomAvailable().getName();		
	}
	
	public Resource getResourceToUse(){		
		return getRandomAvailable();		
	}

	@Override
	public void unUse() {		
		//System.out.println("Someone tried to directly unUse() a ResourceSet"); 
		/**
		for (Resource r:resources){
			r.unUse();
		}
		*/
	}

	@Override
	public String getDetailString() {
		String s= super.getDetailString()+" (";
		int count=0;
		for(Resource res:resources){
			s=s+" "+res.getName();
			if (count%10==0)
				s+="\r\n";
			count++;
		}
		s+=" )";
		return s;
	}

	@Override
	public void reset() {
		for (Resource r:resources){
			r.reset();
		}
	}
	
	public boolean isOneAvailable() {		
		//only needed to update old ResourceStore entries with the new size field
		updateSize();		
		for(Resource r:resources){
			if (r.isAvailable()){
				return true;
			}
		}
		return false;
	}
	
	public Resource getRandomAvailable(){
		for(Resource r:resources){
			if(r.isAvailable()){
				return r;
			}
		}
		throw new ParameterException("There is no resource available in this ResourceSet!");
	}

	public boolean checkDuplicateAvailability(int dp) {		
		//only needed to update old ResourceStore entries with the new size field
		updateSize();		
		List<Resource> result = new ArrayList<Resource>();
		for (Resource r:resources){
			if(r.isAvailable()){
				if (!result.contains(r)){
					result.add(r);
				}
			}
			if (result.size()>=dp){
				return true;
			}			
		}
		return false;		
	}
	
	public void updateSize(){
		size = resources.size();
	}

	public boolean checkIfResourceIsPart(String resourceName) {
		for(Resource r:resources){
			if (r.getName().equals(resourceName)){
				return true;
			}
		}
		return false;		
	}
}
