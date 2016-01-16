package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.LinkedList;
import java.util.List;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;

public class ResourceSet extends Resource {
	
	List<Resource> resources = new LinkedList<>();
	
	public List<Resource> getRes() {
		return resources;
	}

	public ResourceSet(String name, int amount) {
		this.name = name+"-Set";
		this.type=ResourceType.SET;
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
			}		
		}

	@Override
	public boolean isAvailable() {
		
		if(isDisabled)
			return false;
		
		for (Resource r:resources){
			if (!r.isAvailable()){
				return false;
			}
		}
		return true;
	}

	@Override
	public void use() {
		for (Resource r:resources){
			r.use();
		}

	}

	@Override
	public void unUse() {
		for (Resource r:resources){
			r.unUse();
		}

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

}
