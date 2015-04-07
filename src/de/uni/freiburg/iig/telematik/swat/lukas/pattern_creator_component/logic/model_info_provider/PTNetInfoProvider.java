package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.model_info_provider;

import java.util.ArrayList;
import java.util.Collection;

import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;

public class PTNetInfoProvider implements ModelInfoProvider {
	
	private ArrayList<String> mTransitions;
	private ArrayList<String> mPlaces;

	public PTNetInfoProvider(PTNet net) {
		
		mTransitions = new ArrayList<String>();
		Collection<PTTransition> transitions = net.getTransitions();
		for (PTTransition transition : transitions) {
			mTransitions.add(transition.getName() + "(" + transition.getLabel() + ")");
		}
		
		mPlaces = new ArrayList<String>();
		Collection<PTPlace> places = net.getPlaces();
		for (PTPlace p : places) {
			mPlaces.add(p.getName() + "(" + p.getLabel() + ")");
		}
		
		
		
	}
	
	public ArrayList<String> getTransitions() {
		return mTransitions;
		
	}
	
	public ArrayList<String> getPlaces() {
		return mPlaces;
		
	}

}
