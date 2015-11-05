package de.uni.freiburg.iig.telematik.swat.simon;

import java.util.HashMap;
import java.util.List;

import com.sun.javafx.collections.MappingChange.Map;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.TimeRessourceContext;

public class AwesomeTimeContext implements TimeRessourceContext<ITimeBehaviour>{
	
	private HashMap<String, ITimeBehaviour> timeBehaviour = new HashMap<>(); //hier alles rein, was von ITimeBehaviour (bzw. AbstractTimeBehaviour ableitet

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ITimeBehaviour getTimeFor(String activity) {
		
		ITimeBehaviour behave = timeBehaviour.get(activity);
		behave.getNeededTime();
		
		return timeBehaviour.get(activity);
	}

	@Override
	public void removeTimeBehaviourFor(String activity) {
		timeBehaviour.remove(activity);
		
	}

	@Override
	public void addTimeBehaviourFor(String activity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addResource(String activity, List<String> resources) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void blockResources(List<String> resources) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<List<String>> getAllowedResourcesFor(String activity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getRandomAllowedResourcesFor(String activity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAvailable(String ressourceName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean behaviorIsKnown(String activity, List<String> resources) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ITimeBehaviour getTimeFor(String activity, List<String> resources) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeResourceUsage(String activity, List<String> resources) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTimeBehaviourFor(String activity, List<String> resources, ITimeBehaviour behaviour) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTimeBehaviourFor(String activity, List<String> resource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}


}
