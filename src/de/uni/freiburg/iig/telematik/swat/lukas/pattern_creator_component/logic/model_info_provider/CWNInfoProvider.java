package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.model_info_provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;

public class CWNInfoProvider implements ModelInfoProvider {
	
	private ArrayList<String> mTransitions;
	private HashMap<String, ArrayList<String>> mPlacesToColorMap;
	private ArrayList<String> mPlaces;

	public CWNInfoProvider(CPN net) {
		
		mTransitions = new ArrayList<String>();
		Collection<CPNTransition> transitions = net.getTransitions();
		for (CPNTransition transition : transitions) {
			mTransitions.add(transition.getName() + "(" + transition.getLabel() + ")");
		}
		
		mPlaces = new ArrayList<String>();
		mPlacesToColorMap = new HashMap<String, ArrayList<String>>();
		
		Collection<CPNPlace> places = net.getPlaces();
		for (CPNPlace p : places) {
			mPlaces.add(p.getName() + "(" + p.getLabel() + ")");
			ArrayList<String> possibleColors = new ArrayList<String>();
			if(net.getInitialMarking().get(p.getName()) != null) {
				possibleColors.addAll(net.getInitialMarking().get(p.getName()).support());
			}
			LinkedList<AbstractCPNFlowRelation<?,?>> inRelations = 
					new LinkedList<AbstractCPNFlowRelation<?,?>>(p.getIncomingRelations());
			for (AbstractCPNFlowRelation<?,?> inRel : inRelations) {
				possibleColors.addAll(inRel.getConstraint().support());
			}
			mPlacesToColorMap.put(p.getName() + "(" + p.getLabel() + ")", possibleColors);
		}
		
		
		
	}
	
	public ArrayList<String> getTransitions() {
		return mTransitions;
		
	}
	
	public ArrayList<String> getPlaces() {
		return mPlaces;
		
	}
	
	public ArrayList<String>getTokenColors(String place) {
		return mPlacesToColorMap.get(place);
		
	}

}
