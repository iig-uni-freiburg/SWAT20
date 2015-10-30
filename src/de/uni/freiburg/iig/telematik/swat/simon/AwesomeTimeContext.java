package de.uni.freiburg.iig.telematik.swat.simon;

import java.util.List;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.TimeRessourceContext;

public class AwesomeTimeContext implements TimeRessourceContext<ITimeBehaviour>{

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAvailable(String ressourceName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean behaviorIsKnown(String activity, String... ressource) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ITimeBehaviour getTimeFor(String activity, List<String> resources) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addRessource(String activity, String... ressources) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<List<String>> getAllowedRessourcesFor(String activity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getRandomAllowedRessourcesFor(String activity, boolean blockRessources) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeRessourceUsage(String activity, String... ressources) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
