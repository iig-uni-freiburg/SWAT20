package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.Iterator;
import java.util.TreeSet;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;

public class IFNetConverter extends CWNConverter {

	private IFNet mNet;

	public IFNetConverter(IFNet net) {
		super(net);
		mNet = net;
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
			
			Iterator<AbstractIFNetTransition<IFNetFlowRelation>> tIterator = mNet.getTransitions().iterator();	
			 
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
