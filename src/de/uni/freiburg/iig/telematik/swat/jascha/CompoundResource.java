package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.ArrayList;
import java.util.List;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;

public class CompoundResource extends Resource {

	List<IResource> resources = new ArrayList<>();

	public CompoundResource() {
		super("Compound");
	}
	
	public CompoundResource(String name, List<IResource> resources){
		super(name);
		this.resources=resources;
	}
	
	public CompoundResource(String name){
		super(name);
	}
	
	public CompoundResource(List<IResource> inputResources){
		for (IResource r: inputResources){
			addResource(r);
		}
		
	}
	
	//Konstruktor, bei dem die Ressource gleich in ein ResourceStore eingetragen wird.
	public CompoundResource(String name, ResourceStore resourceStore){
		super(name);
		resourceStore.addResource(this);
	}

	@Override
	public boolean isAvailable() {
		for (IResource r : resources) {
			if (!r.isAvailable())
				return false;
		}
		return true;
	}
	
	public List<IResource> getConsistingResources(){
		return resources;
	}

	public String getName() {
		StringBuilder b = new StringBuilder();
		for (IResource r : resources) {
			b.append(r.getName());
			b.append(", ");
		}
		String result = b.toString();
		if(result!=null && result.length()>1)result = result.substring(0, result.length()-1); //remove ", ";
		return super.getName()+": "+result;
	}
	
	
	public void addResource(IResource r){
		resources.add(r);
	}
	
	public void removeResource(IResource r){
		resources.remove(r);
	}

	@Override
	public void use() {
		//throw new RuntimeException("Not yet implemented");		
		for (IResource r: resources){
			r.use();
		}
	}

	@Override
	public void unUse() {
		//throw new RuntimeException("Not yet implemented");		
		for (IResource r: resources){
			r.unUse();
		}		
	}

	@Override
	public void reset() {
		for(IResource r:resources)
			r.reset();
	}

}
