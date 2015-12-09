package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;

public class ResourceStore {
	
	protected static Map<String,IResource> resources;
	
	public ResourceStore(){
		resources = new HashMap<>();
	}
	
	public void addResource(IResource item){
		resources.put(item.getName(), item);
	}
	
	public void removeResource(IResource item){
		resources.remove(item.getName());
	}

	public IResource getResource(String name){
		return resources.get(name);
	}
	
	public IResource instantiateResource(ResourceType type, String name, int amount){
		if(!type.equals(ResourceType.SET))
			throw new ParameterException("can only instantiate Resource Set with amount");
		ResourceSet rs = new ResourceSet(name, amount);
		resources.put(name, rs);
		
		for(IResource res:rs.getRes()) resources.put(res.getName(), res);
		return rs;
	}
	
		
	//Die Idee ist, anhand eines Pr�fixes die Anzahl der verf�gbaren Ressourcen zu erfahren. 
	//Bisher haben Ressourcen eindeutige Namen nach dem Muster Hammer1, Hammer2, Hammer3 usw, deswegen sollte das funktionieren.
	public int countAvailable(String prefix){
		int result = 0;
		Set<String> keys = resources.keySet();
		for(String key:keys){
			if (key.startsWith(prefix)){
				IResource resource = resources.get(key);
				if (resource.isAvailable()){
					result ++;
				}
			}
		}
		//System.out.println("There are " + result + " resources with prefix " + prefix + " available.");
		return result;
	}
}
