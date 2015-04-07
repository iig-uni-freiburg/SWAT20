package de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.prism;

import java.util.Collection;
import java.util.HashMap;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public class TransitionToIDMapper {
	
	private static HashMap<String, Integer> mTransToIDMap = new HashMap<String, Integer>();
	
	public static void createMap(AbstractPetriNet<?,?,?,?,?,?,?> net) {
		
			mTransToIDMap = new HashMap<String, Integer>();
			Collection<AbstractTransition<?,?>> transitions = 
					(Collection<AbstractTransition<?, ?>>) net.getTransitions();
			int counter = 0;
			for (AbstractTransition<?,?> transition : transitions) {
				mTransToIDMap.put(transition.getName(), ++counter);
			}
		
	}
	
	public static Integer getID(String transitionName) {
		
		if (mTransToIDMap.get(transitionName) == null) { 
			return -1;
		} else {
			return mTransToIDMap.get(transitionName);
		}
	
	}
	
	public static int getTransitionCount() {
		return mTransToIDMap.size();
	}; 
	
	

}
