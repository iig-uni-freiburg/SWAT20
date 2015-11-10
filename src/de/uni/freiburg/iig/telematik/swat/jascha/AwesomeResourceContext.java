package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.TimeRessourceContext;

public class AwesomeResourceContext implements IResourceContext{
	
	//beinhaltet Liste mit Ressourcen-Objekten. Ressourcen-Objekt kann entweder selbst eine Liste haben oder eine einzelne Resource darstellen
	Map<String,List<IResource>> resources = new HashMap<>();


	@Override
	public boolean isAvailable(String ressourceName) {
		for (IResource o:resources.get(ressourceName)){
			if(!o.isAvailable()) return false;
		}
		return true;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void blockResources(List<String> resources) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unBlockResources(List<String> resources) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<List<String>> getAllowedResourcesFor(String activity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getRandomAllowedResourcesFor(String activity, boolean blockResources) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IResource getResourceObject(String resourceName) {
		// TODO Auto-generated method stub
		return null;
	}

}
