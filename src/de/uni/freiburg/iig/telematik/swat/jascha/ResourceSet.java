package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.LinkedList;
import java.util.List;

import de.invation.code.toval.validate.ParameterException;

public class ResourceSet extends Resource {
	
	List<Resource> resources = new LinkedList<>();
	
	public List<Resource> getRes() {
		return resources;
	}

	public ResourceSet(String name, int amount) {
		this.name = name;
		this.type=ResourceType.SET;
		for (int i = 0;i<amount;i++){
			resources.add(new SimpleResource(name+"-"+i));
		}
	}

	@Override
	public boolean isAvailable() {
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
