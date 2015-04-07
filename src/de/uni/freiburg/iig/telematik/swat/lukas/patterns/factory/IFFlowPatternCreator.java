package de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.misc.SetUtils;
import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Token;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Activity;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.BottomPattern;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Causal;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.PBNI;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.ReadUp;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.UsageConflict;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.WriteDown;


/**
 * The IFFlowPatternCreator is a helper class which creates information flow compliance rules.
 * The information flow compliance rules have to satisfied by an ifnet which is a design time model
 * of a business process. 
 *
 * @author Lukas Sättler
 * @version 1.0
 */

public class IFFlowPatternCreator {
	
	private IFNet mNet;
	
	public IFFlowPatternCreator(IFNet net) {
		mNet = net;
	}

	
	/**
	 * Returns a list compliance rules describing a requirement on the information flow of a
	 * business process modelled by an ifnet. 
	 *
	 * @param  patternName the name of the pattern that is used to create the compliance rules
	 * @return the list of compliance rules describing the requirement on the ifnet
	 */
	public ArrayList<CompliancePattern> createPatterns(String patternName) {
		
		ArrayList<CompliancePattern> patterns = new ArrayList<CompliancePattern>();
		Labeling label = mNet.getAnalysisContext().getLabeling();
		
		if (patternName.equals(ReadUp.NAME)) {
			
			Collection<RegularIFNetTransition> transitions = mNet.getRegularTransitions();
			// find read-up patterns
			for (RegularIFNetTransition transition : transitions) {
				if (SecurityLevel.LOW == label.getActivityClassification(transition.getName())) {
					Set<String> colors = transition.getConsumedColors();
					Map<String, Set<AccessMode>> accessModes = transition.getAccessModes();
					for (String color : colors) {
						if (SecurityLevel.HIGH == label.getAttributeClassification(color) && 
								accessModes != null && 
								accessModes.get(color) != null && 
								accessModes.get(color).contains(AccessMode.READ)) {
							patterns.add(new ReadUp(new Activity(transition.getName()), new Token(color)));
						}
					}
				}
			}
		} else if (patternName.equals(WriteDown.NAME)) {
			
			Collection<RegularIFNetTransition> transitions = mNet.getRegularTransitions();
			
			
			// find read-up patterns
			for (RegularIFNetTransition transition : transitions) {
				if (SecurityLevel.HIGH == label.getActivityClassification(transition.getName())) {
					Set<String> colors = transition.getConsumedColors();
					Map<String, Set<AccessMode>> accessModes = transition.getAccessModes();
					for (String color : colors) {
						if (SecurityLevel.LOW == label.getAttributeClassification(color) && 
								accessModes != null && 
								accessModes.get(color) != null &&
								accessModes.get(color).contains(AccessMode.WRITE)) {
							patterns.add(new WriteDown(new Activity(transition.getName()), new Token(color)));
						}
					}
				}
			}
		} else if (patternName.equals(PBNI.NAME)) {
			
			createCausalPatterns(patterns, label);
			createUsageConflictPatterns(patterns, label);
		} 
		
		if (patterns.size() == 0) {
			patterns.add(new BottomPattern());
		}
		
		return patterns;
	}


	private void createCausalPatterns(ArrayList<CompliancePattern> patterns,
			Labeling label) {
		Collection<AbstractIFNetTransition<IFNetFlowRelation>> transitions = mNet.getTransitions();
		// iterate over all ifnet transitions
		for (AbstractIFNetTransition<IFNetFlowRelation> highTransition : transitions) {
			// check whether security level is high
			if (SecurityLevel.HIGH == label.getActivityClassification(highTransition.getName())) {
				List<IFNetFlowRelation> outputRelations = highTransition.getOutgoingRelations();
				for (IFNetFlowRelation rel : outputRelations) {
					IFNetPlace outputPlace = rel.getPlace();
					List<IFNetFlowRelation> outputRelationsOfPlace = 
							outputPlace.getOutgoingRelations();
					for (IFNetFlowRelation relOfPlace : outputRelationsOfPlace) {
						AbstractIFNetTransition<IFNetFlowRelation> lowTransition = relOfPlace.getTransition();
						// check whether transition has security level is low
						if (SecurityLevel.LOW == label.getActivityClassification(lowTransition.getName())) {
							patterns.add(new Causal(new Activity(highTransition.getName()),
									outputPlace, new Activity(lowTransition.getName())));
						}
					}
				}
				
			}
		}
	}


	private void createUsageConflictPatterns(
			ArrayList<CompliancePattern> patterns, Labeling label) {
		Collection<AbstractIFNetTransition<IFNetFlowRelation>> transitions = mNet.getTransitions();
		HashMap<AbstractIFNetTransition<IFNetFlowRelation>, HashSet<IFNetPlace>> lowTransToInputPlace = 
				new HashMap<AbstractIFNetTransition<IFNetFlowRelation>, HashSet<IFNetPlace>>();
		HashMap<AbstractIFNetTransition<IFNetFlowRelation>, HashSet<IFNetPlace>> highTransToInputPlace = 
				new HashMap<AbstractIFNetTransition<IFNetFlowRelation>, HashSet<IFNetPlace>>();
		
		// find all low transitions
		for (AbstractIFNetTransition<IFNetFlowRelation> transition : transitions) {
			if (SecurityLevel.LOW == label.getActivityClassification(transition.getName())) {
				HashSet<IFNetPlace> inputPlaces = new HashSet<IFNetPlace>(); 
				for (IFNetFlowRelation incommingRel : transition.getIncomingRelations()) {
					inputPlaces.add(incommingRel.getPlace());
				}
				lowTransToInputPlace.put(transition, inputPlaces);
			}
		}
		
		// find all high transitions
		for (AbstractIFNetTransition<IFNetFlowRelation> transition : transitions) {
			if (SecurityLevel.HIGH == label.getActivityClassification(transition.getName())) {
				HashSet<IFNetPlace> inputPlaces = new HashSet<IFNetPlace>(); 
				for (IFNetFlowRelation incommingRel : transition.getIncomingRelations()) {
					inputPlaces.add(incommingRel.getPlace());
				}
				highTransToInputPlace.put(transition, inputPlaces);
			}
		}
		
		// find conflict places between of high and low transitions
		for (Map.Entry<AbstractIFNetTransition<IFNetFlowRelation>,
				HashSet<IFNetPlace>> highTransEntry : highTransToInputPlace.entrySet()) {
			
			AbstractIFNetTransition<IFNetFlowRelation> highTransition = highTransEntry.getKey();
			Set<IFNetPlace> highTransInputPlaces = highTransEntry.getValue();
			
			for (Map.Entry<AbstractIFNetTransition<IFNetFlowRelation>,
					HashSet<IFNetPlace>> lowTransEntry : lowTransToInputPlace.entrySet()) {
				
				AbstractIFNetTransition<IFNetFlowRelation> lowTransition = lowTransEntry.getKey();
				Set<IFNetPlace> lowTransInputPlaces = lowTransEntry.getValue();
				ArrayList<Set<IFNetPlace>> intersectionSets = new ArrayList<Set<IFNetPlace>>();
				intersectionSets.add(highTransInputPlaces);
				intersectionSets.add(lowTransInputPlaces);
				Set<IFNetPlace> intersection = SetUtils.intersection(intersectionSets);

				// check whether transitions consume the same color 
				if (intersection.size() > 0) {
					for (IFNetPlace p : intersection) {
						IFNetFlowRelation relHigh = highTransition.getRelationFrom(p);
						IFNetFlowRelation relLow = lowTransition.getRelationFrom(p);
						Multiset<String> commonConsumedColors = relHigh.getConstraint().intersection(relLow.getConstraint());
						if (commonConsumedColors.size() > 0) {
							patterns.add(new UsageConflict(highTransition,
									lowTransition, p, commonConsumedColors));
						}
					}
				}
				
			}
			
		}
	}
	
}
