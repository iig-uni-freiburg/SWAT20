package de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.prism.modeltranlator;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetPlace;


public class IFNetAdapter extends CPNAdapter {

	private AbstractIFNet<?,?,?,?,?,?> mNet;

	public IFNetAdapter(IFNet net) {
		super(net);
		mNet = net;
	}
	
	@SuppressWarnings("static-access")
	@Override
	protected StringBuilder createPlaceVars() throws PlaceException {
		
		StringBuilder allPlacesBuilder = new StringBuilder();
		
		for (AbstractIFNetPlace<?> p : mNet.getPlaces()) {
			
			StringBuilder placeBuilder = new StringBuilder();

			placeBuilder.append("//PlaceName: " + p.getName() +" (PlaceLabel: " + p.getLabel() +")" + "\n");
			
			if (!mNet.getSourcePlaces().contains(p)) {
			
				if (mNet.getDrainPlaces().contains(p)) {
					placeBuilder.append("//drainPlace" + "\n");
				}
				
				LinkedList<AbstractIFNetFlowRelation<?,?>> inRelations = 
						new LinkedList<AbstractIFNetFlowRelation<?,?>>(p.getIncomingRelations());
				Set<String> possibleColors = new HashSet<String>();
				
				if(mNet.getInitialMarking().get(p.getName()) != null) {
					possibleColors.addAll(mNet.getInitialMarking().get(p.getName()).support());
                }
				
				for (AbstractIFNetFlowRelation<?,?> inRel : inRelations) {
					possibleColors.addAll(inRel.getConstraint().support());
				}
				
				for(String color : possibleColors){
					placeBuilder.append(p.getName() +"_" + color + " : ");
					if (p.getColorCapacity(color) != -1) {
						placeBuilder.append("[0.." + p.getColorCapacity(color) + "] ");
					} else {
						System.out.println("Not a valid CWN! Place " + p.getName() + "is not bounded.");
						throw new PlaceException("Not a valid CWN! Place " + p.getName() + "is not bounded.");
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
