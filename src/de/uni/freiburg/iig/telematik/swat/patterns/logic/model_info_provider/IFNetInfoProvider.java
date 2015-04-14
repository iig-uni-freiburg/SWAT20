package de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;

public class IFNetInfoProvider implements ModelInfoProvider {
	
	private ArrayList<String> mTransitions;
	private HashMap<String, ArrayList<String>> mPlacesToColorMap;
	private ArrayList<String> mPlaces;
	private ArrayList<String> mSubjects;

	public IFNetInfoProvider(IFNet net) {
		
		mTransitions = new ArrayList<String>();
		Collection<AbstractIFNetTransition<IFNetFlowRelation>> transitions = net.getTransitions();
		for (AbstractIFNetTransition<IFNetFlowRelation> transition : transitions) {
			mTransitions.add(transition.getName() + "(" + transition.getLabel() + ")");
		}
		
		mPlaces = new ArrayList<String>();
		mPlacesToColorMap = new HashMap<String, ArrayList<String>>();
		
		Collection<IFNetPlace> places = net.getPlaces();
		for (IFNetPlace p : places) {			
			mPlaces.add(p.getName() + "(" + p.getLabel() + ")");
			ArrayList<String> possibleColors = new ArrayList<String>();
			if(net.getInitialMarking().get(p.getName()) != null) {
				possibleColors.addAll(net.getInitialMarking().get(p.getName()).support());
			}
			LinkedList<AbstractIFNetFlowRelation<?,?>> inRelations = 
					new LinkedList<AbstractIFNetFlowRelation<?,?>>(p.getIncomingRelations());
			for (AbstractIFNetFlowRelation<?,?> inRel : inRelations) {
				possibleColors.addAll(inRel.getConstraint().support());
			}
			mPlacesToColorMap.put(p.getName() + "(" + p.getLabel() + ")", possibleColors);
		}
		
		mSubjects = new ArrayList<String>(net.getSubjectDescriptors());
		
		
		
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

	public ArrayList<String> getSubjects() {
		return mSubjects;
	}
	


}
