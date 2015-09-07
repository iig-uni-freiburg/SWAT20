package de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.prism;

import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Pattern;

import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;

public class TransitionToIDMapper {
	
	private static HashMap<String, Integer> mTransToIDMap = new HashMap<String, Integer>();
	
	public static void createMap(AbstractPetriNet<?,?,?,?,?> net) {
		
			mTransToIDMap = new HashMap<String, Integer>();
			Collection<AbstractTransition<?,?>> transitions = 
					(Collection<AbstractTransition<?, ?>>) net.getTransitions();
			int counter = 0;
			for (AbstractTransition<?,?> transition : transitions) {
				if(transition.getName().equals("id4")) 
					System.out.println("found");
				mTransToIDMap.put(transition.getName(), ++counter);
			}
		
	}
	
	public static Integer getID(String transitionName) {
		
		String transitionString=transitionName;
		
		if(transitionName.contains("(")){
			//probably format: transitionID(transitionLabel)
			//split to get ID
			transitionString=transitionName.split(Pattern.quote("("))[0];
		}
		
		if (mTransToIDMap.get(transitionString) == null) { 
			return -1;
		} else {
			return mTransToIDMap.get(transitionString);
		}
	
	}
	
	public static int getTransitionCount() {
		return mTransToIDMap.size();
	}; 
	
	

}
