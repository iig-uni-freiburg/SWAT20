package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.Iterator;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;

public class PTNetConverter extends PrismConverter {

	public PTNetConverter(AbstractPTNet<?,?,?,?,?,?> net) {
		super(net);
	}

	@Override
	protected StringBuilder createPlaceVars() {
		
		StringBuilder allPlacesBuilder = new StringBuilder();
		
		for (AbstractPlace<?,?> p : mAbstractNet.getPlaces()) {
			
			StringBuilder placeBuilder = new StringBuilder();

			placeBuilder.append("//PlaceName: " + p.getName() +" (PlaceLabel: " + p.getLabel() +")" + "\n");
		
			if (mAbstractNet.getDrainPlaces().contains(p)) {
				placeBuilder.append("//drainPlace" + "\n");
			}
			
			placeBuilder.append(p.getName() + "_black : ");
			placeBuilder.append("[0.." + p.getCapacity() + "] ");
				
			if (mAbstractNet.getInitialMarking().get(p.getName()) != null) {
				placeBuilder.append("init " + mAbstractNet.getInitialMarking().get(
						p.getName()) + ";" + "\n");
			} else {
				placeBuilder.append("init 0;" + "\n");
			}
		
			allPlacesBuilder.append(placeBuilder.append("\n"));
			
		}
				
		return allPlacesBuilder;

	}

	@Override
	protected StringBuilder createTransitions() {
		
        StringBuilder completeTransitionBuilder = new StringBuilder();
		
		for(AbstractTransition<?,?> t : mAbstractNet.getTransitions()) {
			
			StringBuilder enablednessBuilder = new StringBuilder();
			StringBuilder firingEffectBuilder = new StringBuilder();
			
			for (Object inRelObject : t.getIncomingRelations()) {
				AbstractPTFlowRelation<?, ?> inRel = (AbstractPTFlowRelation<?,?>) inRelObject;
				AbstractPlace<?,?> inputPlace = (AbstractPlace<?,?>) inRel.getPlace();
				int inConstraint = inRel.getConstraint();
				String prismVariableName = inputPlace.getName() + "_black";
				enablednessBuilder.append(prismVariableName + ">" + (inConstraint - 1) + " & "); 				
				firingEffectBuilder.append("("+ prismVariableName +"'=" + prismVariableName +
						"-" + inConstraint + ") & ");				
			}						
			
			
			@SuppressWarnings("unchecked")
			Iterator<AbstractPTFlowRelation<?,?>> outFlowIter = 
					(Iterator<AbstractPTFlowRelation<?,?>>) t.getOutgoingRelations().iterator();
			
			while(outFlowIter.hasNext()) {			
				
				AbstractPTFlowRelation<?,?> outRel = outFlowIter.next();
				AbstractPTPlace<?> outPlace = outRel.getPlace();
				int outConstraint = outRel.getConstraint();
				int neededSpace = (outPlace.getCapacity() - outConstraint) + 1;
				String prismVariableName = outPlace.getName() + "_black";				
				
				if(outFlowIter.hasNext()) {
					enablednessBuilder.append(prismVariableName + "<" + neededSpace + " & ");
				} else {
					enablednessBuilder.append(prismVariableName + "<" + neededSpace);
				}
				
				firingEffectBuilder.append("(" + prismVariableName + "'=" + prismVariableName + 
							"+" + outConstraint + ")" + " & ");										
			}
			
			firingEffectBuilder.append("(" + t.getName()+"'=1"+ ") & " );
			firingEffectBuilder.append("(" + t.getName()+"_last'=1"+ ") & ");
			
			 @SuppressWarnings("unchecked")
			Iterator<AbstractPTTransition<?>> tIterator = 
					 (Iterator<AbstractPTTransition<?>>) mAbstractNet.getTransitions().iterator();	
			 
			 AbstractPTTransition<?> currentTransition = null;
	        
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
			
			for (AbstractPlace<?,?> p : mAbstractNet.getDrainPlaces()) {
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