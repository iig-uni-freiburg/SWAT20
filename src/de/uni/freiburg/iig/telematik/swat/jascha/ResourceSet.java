package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;

// Resource Sets are Ressources that group similar resources. 
// If an activity uses a resource set it means it can use ANY ONE resource of the resources in the set.
// IDEA: If more than one resource of a set (but not specific resources) are needed, make a compound resource where the set is added several times.
public class ResourceSet extends Resource {
	
	List<Resource> resources = new LinkedList<>();
	int size = 0;
	
	public int getSize() {
		return size;
	}

	public List<Resource> getRes() {
		return resources;
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
		if (((Resource)input).type == ResourceType.SIMPLE || ((Resource)input).type == ResourceType.HUMAN)
		{
			SimpleResource sr = (SimpleResource)input;
			sr.updateAssociatedSets(UpdateType.INCREASE);
			resources.add(sr);
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

	//A ResourceSet is available if ONE of the contained resources is available!
	@Override
	public boolean isAvailable() {
		
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

	public boolean checkAvailabiltyWithDuplicates(int dp) {
		updateSize();
		List<Resource> result = new ArrayList<Resource>();
		for (Resource r:resources){
			if(r.isAvailable()){
				if (!result.contains(r)){
					result.add(r);
				}
			}
			if (result.size()>=dp){
				System.out.println("There are at least " + dp + " resources from this set available");
				return true;
			}
			
			
		}
		return false;		
		
	}
	
	public void updateSize(){
		size = resources.size();
	}

}
