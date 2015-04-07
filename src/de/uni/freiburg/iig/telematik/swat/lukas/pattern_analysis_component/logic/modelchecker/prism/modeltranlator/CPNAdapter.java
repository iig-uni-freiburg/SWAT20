package de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.prism.modeltranlator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.prism.TransitionToIDMapper;

public class CPNAdapter extends PrismModelAdapter {

	private AbstractCPN<?, ?, ?, ?, ?, ?> mNet;

	public CPNAdapter(AbstractCPN<?, ?, ?, ?, ?, ?> net) {
		super(net);
		mNet = net;
	}

	@SuppressWarnings("static-access")
	@Override
	protected StringBuilder createPlaceVars() {
		
		StringBuilder allPlacesBuilder = new StringBuilder();
		
		for (AbstractCPNPlace<?> p : mNet.getPlaces()) {
			
			StringBuilder placeBuilder = new StringBuilder();

			placeBuilder.append("//PlaceName: " + p.getName() +" (PlaceLabel: " + p.getLabel() +")" + "\n");
			
			if (!mNet.getSourcePlaces().contains(p)) {
			
				if (mNet.getDrainPlaces().contains(p)) {
					placeBuilder.append("//drainPlace" + "\n");
				}
				
				LinkedList<AbstractCPNFlowRelation<?,?>> inRelations = 
						new LinkedList<AbstractCPNFlowRelation<?,?>>(p.getIncomingRelations());
				Set<String> possibleColors = new HashSet<String>();
				
				if(mNet.getInitialMarking().get(p.getName()) != null) {
					possibleColors.addAll(mNet.getInitialMarking().get(p.getName()).support());
                }
				
				for (AbstractCPNFlowRelation<?,?> inRel : inRelations) {
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
				Set<String> possibleColors = new HashSet<String>();
				Multiset<String> possibleColorMultiSet = mNet.getInitialMarking().get(p.getName()); 
				possibleColors.addAll(possibleColorMultiSet.support());
							
				for (String color : possibleColors) {
					placeBuilder.append(p.getName() + "_" + color + 
							" : [0.." + p.getColorCapacity(color) + "] init " + 
							possibleColorMultiSet.multiplicity(color) + ";" + "\n");
				}
			}
			
			allPlacesBuilder.append(placeBuilder.append("\n"));
			
		}
				
		return allPlacesBuilder;
	}


	@Override
	protected StringBuilder createTransitions() {
		
		StringBuilder completeTransitionBuilder = new StringBuilder();
		
		for(AbstractCPNTransition<?> t : mNet.getTransitions()) {
			
			StringBuilder enablednessBuilder = new StringBuilder();
			StringBuilder firingEffectBuilder = new StringBuilder();
			
			for (Object inRelObject : t.getIncomingRelations()) {
				AbstractCPNFlowRelation<?, ?> inRel = (AbstractCPNFlowRelation<?,?>) inRelObject;
				AbstractCPNPlace<?> inputPlace = (AbstractCPNPlace<?>) inRel.getPlace();
				Multiset<String> inConstraint = inRel.getConstraint();
				TreeSet<String> colors = new TreeSet<String>(inRel.getConstraint().support());
				
				for(String color : colors){				
					String prismVariableName = inputPlace.getName() + "_" + color;
					enablednessBuilder.append(prismVariableName + ">" + (inConstraint.multiplicity(color)-1) + " & "); 				
					firingEffectBuilder.append("("+ prismVariableName +"'=" + prismVariableName + "-" + inConstraint.multiplicity(color) + ") & ");				
				}						
			}
			
			@SuppressWarnings("unchecked")
			Iterator<AbstractCPNFlowRelation<?,?>> outFlowIter = 
					(Iterator<AbstractCPNFlowRelation<?,?>>) t.getOutgoingRelations().iterator();
			
			while(outFlowIter.hasNext()) {			
				
				AbstractCPNFlowRelation<?,?> outRel = outFlowIter.next();
				AbstractCPNPlace<?> outPlace = outRel.getPlace();
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
			
			if (enablednessBuilder.toString().endsWith("& ")) {
				enablednessBuilder.delete(enablednessBuilder.length() - 2, enablednessBuilder.length());
			}
			int transitionId = TransitionToIDMapper.getID(t.getName());
			firingEffectBuilder.append("(" + transitionVarName + "'= " + transitionId + ") & " );
			firingEffectBuilder.append("(" + t.getName()+"_fired'=1"+ ") & ");
			
			if (firingEffectBuilder.lastIndexOf("&") == firingEffectBuilder.length() - 2) {
				firingEffectBuilder.delete(firingEffectBuilder.length() - 3, firingEffectBuilder.length());
				firingEffectBuilder.append(";");
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
