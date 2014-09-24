package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWN;



public class CWNConverter extends CPNConverter {

	private AbstractCWN<?, ?, ?, ?, ?, ?> mNet;


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
				
				if(mNet.getInitialMarking().get(p.getName()) != null) {
					possibleColors.addAll(mNet.getInitialMarking().get(p.getName()).support());
                }
				
				for (AbstractCWNFlowRelation<?,?> inRel : inRelations) {
					possibleColors.addAll(inRel.getConstraint().support());
				}
				
				for(String color : possibleColors){
					placeBuilder.append(p.getName() +"_" + color + " : ");
					if (p.getColorCapacity(color) != -1) {
						placeBuilder.append("[0.." + p.getColorCapacity(color) + "] ");
					} else {
						System.out.println("Not a valid CWN! Place " + p.getName() + "is not bounded.");
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

}
