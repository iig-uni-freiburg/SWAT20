package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNTransition;


public class CWNConverter extends PrismConverter {
	
	
	private AbstractCWN<?,?,?,?,?,?> mNet;

	public CWNConverter(AbstractCWN<?,?,?,?,?,?> net) {
		super(net);
		mNet = net;
	}

	@SuppressWarnings("static-access")
	@Override
	protected StringBuilder createPlaceVars() {
		
		StringBuilder allPlacesBuilder = new StringBuilder();
		
		for (AbstractCWNPlace<?> p : mNet.getPlaces()) {
			
			StringBuilder placeBuilder = new StringBuilder();

			placeBuilder.append("//PlaceName: " + p.getName() +" (PlaceLabel: " + p.getLabel() +")" + "\n");
			
			if (!mNet.getSourcePlaces().contains(p)) {
			
				if (mNet.getDrainPlaces().contains(p)) {
					placeBuilder.append("//drainPlace" + "\n");
				}
				
				LinkedList<AbstractCWNFlowRelation<?,?>> inRelations = 
						new LinkedList<AbstractCWNFlowRelation<?,?>>(p.getIncomingRelations());
				Set<String> possibleColors = new HashSet<String>();
				
				for (AbstractCWNFlowRelation<?,?> inRel : inRelations) {
					possibleColors.addAll(inRel.getConstraint().support());
				}
				
				for(String color : possibleColors){
					placeBuilder.append(p.getName() +"_" + color + " : ");
					if (p.getColorCapacity(color) != -1) {
						placeBuilder.append("[0.." + p.getColorCapacity(color) + "] ");
					} else {
						placeBuilder.append("int ");
						bounded = false;
					}
					
					if (mNet.getInitialMarking().get(p.getName()) != null) {
						placeBuilder.append("init " + mNet.getInitialMarking().get(
								p.getName()).multiplicity(color) + ";" + "\n");
					} else {
						placeBuilder.append("init 0;" + "\n");
					}
				}
			} else {
				placeBuilder.append("//SourcePlace" + "\n");
				placeBuilder.append(p.getName() + "_" + mNet.CONTROL_FLOW_TOKEN_COLOR + 
						" : [0..1] init 1;" + "\n");
			}
			
			allPlacesBuilder.append(placeBuilder.append("\n"));
			
		}
				
		return allPlacesBuilder;
	}

	@Override
	protected StringBuilder createTransitions() {
		
		StringBuilder completeTransitionBuilder = new StringBuilder();
		
		for(AbstractCWNTransition<?> t : mNet.getTransitions()) {
			
			StringBuilder enablednessBuilder = new StringBuilder();
			StringBuilder firingEffectBuilder = new StringBuilder();
			
			for (Object inRelObject : t.getIncomingRelations()) {
				AbstractCWNFlowRelation<?, ?> inRel = (AbstractCWNFlowRelation<?,?>) inRelObject;
				AbstractCWNPlace<?> inputPlace = (AbstractCWNPlace<?>) inRel.getPlace();
				Multiset<String> inConstraint = inRel.getConstraint();
				TreeSet<String> colors = new TreeSet<String>(inRel.getConstraint().support());
				
				for(String color : colors){				
					String prismVariableName = inputPlace.getName() + "_" + color;
					enablednessBuilder.append(prismVariableName + ">" + (inConstraint.multiplicity(color)-1) + " & "); 				
					firingEffectBuilder.append("("+ prismVariableName +"'=" + prismVariableName + "-" + inConstraint.multiplicity(color) + ") & ");				
				}						
			}
			
			@SuppressWarnings("unchecked")
			Iterator<AbstractCWNFlowRelation<?,?>> outFlowIter = 
					(Iterator<AbstractCWNFlowRelation<?,?>>) t.getOutgoingRelations().iterator();
			
			while(outFlowIter.hasNext()) {			
				
				AbstractCWNFlowRelation<?,?> outRel = outFlowIter.next();
				AbstractCWNPlace<?> outPlace = outRel.getPlace();
				Multiset<String> outConstraint = outRel.getConstraint();
				TreeSet<String> colors = new TreeSet<String>(outRel.getConstraint().support());
				Iterator<String> colorIter = colors.iterator();
				
				while(colorIter.hasNext()) {
					
					String color = colorIter.next();
					int neededSpace = (outPlace.getColorCapacity(color) - outConstraint.multiplicity(color)) +1;
					String prismVariableName = outPlace.getName() + "_" + color;				
					
					if (outPlace.getColorCapacity(color) != -1) {
						if(colorIter.hasNext() || outFlowIter.hasNext()) {
							enablednessBuilder.append(prismVariableName + "<" + neededSpace + " & ");
						} else {
							enablednessBuilder.append(prismVariableName + "<" + neededSpace);
						}
					}
					
					firingEffectBuilder.append("(" + prismVariableName + "'=" + prismVariableName + 
							"+" + outConstraint.multiplicity(color) + ")" + " & ");				
				}						
			}
			
			firingEffectBuilder.append("(" + t.getName()+"'=1"+ ") & " );
			firingEffectBuilder.append("(" + t.getName()+"_last'=1"+ ") & ");
			
			 @SuppressWarnings("unchecked")
			Iterator<AbstractCWNTransition<?>> tIterator = 
					 (Iterator<AbstractCWNTransition<?>>) mNet.getTransitions().iterator();	
			 
			AbstractCWNTransition<?> currentTransition = null;
	        
			while(tIterator.hasNext()) {
				
				currentTransition = tIterator.next();
				
				if (!currentTransition.equals(t)) {
					if(tIterator.hasNext()){
						firingEffectBuilder.append("(" + currentTransition.getName()+"_last'=0"+ ") & ");
					} else {
						firingEffectBuilder.append("(" + currentTransition.getName()+"_last'=0"+ ");");
					}
				}
			}
			
			if (firingEffectBuilder.lastIndexOf("&") == firingEffectBuilder.length() - 2) {
				firingEffectBuilder.delete(firingEffectBuilder.length() - 3, firingEffectBuilder.length());
				firingEffectBuilder.append(";");
			}
			
			for (AbstractCWNPlace<?> p : mNet.getDrainPlaces()) {
				enablednessBuilder.append(" & " + p.getName() + "_black < 1");
			}

			//Combine Part 1) and Part 2)
			StringBuilder transitionBuilder = new StringBuilder();
			transitionBuilder.append("[] ");
			transitionBuilder.append(enablednessBuilder);
			transitionBuilder.append(" -> ");
			transitionBuilder.append(firingEffectBuilder);
			transitionBuilder.append("\n\n");
			completeTransitionBuilder.append(transitionBuilder);
			
		}
				
		return completeTransitionBuilder;
	}

}
