package de.uni.freiburg.iig.telematik.swat.prism.generator;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;

public class CausalPlaceLTLGenerator {

	//The IFNet for whose causal places the LTL formulas should be generated.
	private IFNet iFNet;
	
	//How it works:
	//1) iterate over all places
	//2) check for each place whether there is at least one H-Transition in the preset and one L-Transition in the post set
	//3) for each combination of one such H and one such L check whether H produces a token in the investigated place which L needs
	//4) generate the LTL formula and a comment for each H and L pair where 3) holds
	
	public CausalPlaceLTLGenerator(IFNet IFNet) {
		this.iFNet = IFNet;
	}

	
	/**
	 *This method generates the LTL formulas for all multi causal places
	 * @throws ParameterException 
	*/
	protected StringBuilder generateMultiCausalPlaceLTL() throws ParameterException {
		
		StringBuilder ltlFormulaBuilder = new StringBuilder();
		
		// Iterate over all Places
		for (IFNetPlace currentPlace : iFNet.getPlaces()) {

			// Get the high transitions in the preset of the place and the low
			// transitions in the postset.
			List<List<AbstractIFNetTransition>> presetHighAndPostsetLowTransitions = getPresetHighAndPostsetLowTransitions(currentPlace);
	
			// Remove low transitions not part in a potential multi causal place
			removeNonColorDependingLowTransitions(presetHighAndPostsetLowTransitions,currentPlace);
					
			//Iterate over all potential multi causal patterns in order to generate a comment and a formula for each			
			List<AbstractIFNetTransition> highTransitions = presetHighAndPostsetLowTransitions.get(0);
			List<AbstractIFNetTransition> lowTransitions = presetHighAndPostsetLowTransitions.get(1);
								
			//If there can be a potential multi causal pattern
			if(!(highTransitions.isEmpty() || lowTransitions.isEmpty()) &&  highTransitions.size()>1){
			
				Iterator<AbstractIFNetTransition> lowIter = lowTransitions.iterator();
				while(lowIter.hasNext()){										
					
					AbstractIFNetTransition currentLowTransition = lowIter.next();
					
						//find out which high transitions share token colors with the !current! low transition
					List<AbstractIFNetTransition> highTransitionsSharingColorsWithLowTrans = new LinkedList<AbstractIFNetTransition>(
							highTransitions);
		
						//find the tokens consumed from the place by the low transition
					IFNetFlowRelation toLowTrans = currentPlace.getRelationTo(currentLowTransition);
						Multiset<String> lowConsumedTokens = toLowTrans.getConstraint();
						
					
						//Check for each high transition whether it produces these tokens in the place
					for (AbstractIFNetTransition highTrans : highTransitions) {
						IFNetFlowRelation fromHighTrans = currentPlace.getRelationFrom(highTrans);
							Multiset<String> highProducedTokens = fromHighTrans.getConstraint();		
								
							if(highProducedTokens.intersection(lowConsumedTokens).isEmpty()){
								highTransitionsSharingColorsWithLowTrans.remove(highTrans);								
							}
							
						} 
							
					if(highTransitionsSharingColorsWithLowTrans.size()>1){
						
					//Create the comment for multi causal place and append it to the StringBuilder
					ltlFormulaBuilder.append(createMultiCausalComment(highTransitionsSharingColorsWithLowTrans, currentPlace, currentLowTransition));
					
					//create the LTL formula for a multi causal place and append it to the string builder.
					ltlFormulaBuilder.append(createMultiCausalLTL(highTransitionsSharingColorsWithLowTrans, currentLowTransition));
					}
				}				
			}	
		}
		
		return ltlFormulaBuilder;
	}
	
	
	/**
	 *This method generates the LTL formula for multi causal places for the given transitions.
	 * @throws ParameterException 
	 * 
	*/
	private StringBuilder createMultiCausalLTL(List<AbstractIFNetTransition> highTransitions, AbstractIFNetTransition lowTransition)
			throws ParameterException {

		//Example formula:
		//P=? [ F(t1_last >0 & td=0 & td2=0) ] / P=? [ F(t1_last >0) ]
	
		//Generate the part representing that low is enabled
		StringBuilder lowEnabledBuilder = new StringBuilder();
		//for(IFNetFlowRelation inRel : lowTransition.getIncomingRelations()){
		
			//if(inRel.getName()== null){
				lowEnabledBuilder.append(lowTransition.getName() + "_last >0 "); // + (inConstraint.multiplicity(color)-1) + " & ");			
			//}else{
			//	lowEnabledBuilder.append(lowTransition.getName() + "_last >0");// + (inConstraint.multiplicity(color)-1));
			//}

		//}
			
		//Generate the part representing the high transitions
		StringBuilder highFiredBuilder = new StringBuilder();
		Iterator<AbstractIFNetTransition> highTransIter = highTransitions.iterator();
		while(highTransIter.hasNext()){
			
			AbstractIFNetTransition currentHighTrans = highTransIter.next();
			if(highTransIter.hasNext()){
				highFiredBuilder.append(currentHighTrans.getName() + "=0 & ");
			}else{
				highFiredBuilder.append(currentHighTrans.getName() + "=0");
			}
		}
		
		//Generate the actual LTL formula now ...
		StringBuilder multiCausalLTLBuilder = new StringBuilder();
		multiCausalLTLBuilder.append("P=? [ F(" + lowEnabledBuilder.toString() + " & " + highFiredBuilder.toString() + ") ] / P=? [ F(" + lowEnabledBuilder.toString() + ") ]" + "\n" + "\n");
		return multiCausalLTLBuilder;		
	}

	
	/**
	 *This method generates a comment for a multi causal potential place 
	 * @throws ParameterException 
	 * 
	*/
	private StringBuilder createMultiCausalComment(List<AbstractIFNetTransition> highTransitions, IFNetPlace potentialMultiCausalPlace,
			AbstractIFNetTransition lowTransition) throws ParameterException {

		StringBuilder commentBuilder = new StringBuilder();
		commentBuilder.append("//Place " + potentialMultiCausalPlace + " is a potential multi causal place." + "\n");
		
		commentBuilder.append("//The involved high transitions are :" + "\n");		
		for (AbstractIFNetTransition highTrans : highTransitions) {
			commentBuilder.append("//"+ highTrans + " (DeclassificationTransition = "+ highTrans.isDeclassificator() + ")." + "\n");
		}	
		
		commentBuilder.append("//The involved low transition is " + lowTransition + "." + "\n");
			
		//Compute the shared color
		Multiset<String> highProducedColors = new Multiset<String>();
		for (AbstractIFNetTransition highTrans : highTransitions) {
			highProducedColors = highProducedColors.sum(potentialMultiCausalPlace.getRelationFrom(highTrans).getConstraint());	
		}
						
		IFNetFlowRelation fromPlace = potentialMultiCausalPlace.getRelationTo(lowTransition);
		commentBuilder.append("//The following token colors are involved: " + highProducedColors.intersection(fromPlace.getConstraint()) + "." + "\n");	
		commentBuilder.append("//Possible results for multi causal places:"+ "\n");
		commentBuilder.append("//[0;0]: At least one High always fired if low can fires."+ "\n");
		commentBuilder.append("//(0;1): Some High sometimes fired if low can fire"+ "\n");
		commentBuilder.append("//[1;1]: No High ever fired if low can fire"+ "\n");
	
		return commentBuilder;
		
	}
	

	/**
	 * This method removes all low transitions from the second sublist, which do
	 * not consume a tokens with a color in the given place which is produced by
	 * a high transition in the first sublist.
	 * 
	 * @return List<List<AbstractIFNetTransition>> The transitions which
	 *         cosume/produce common token colors in the given place.
	 * @throws ParameterException
	 */
	private void removeNonColorDependingLowTransitions(List<List<AbstractIFNetTransition>> highLowTransitions, IFNetPlace place)
			throws ParameterException {
		
		//Get the list with high transitions
		List<AbstractIFNetTransition> highTransitions = highLowTransitions.get(0);
		//Get the list with low transitions
		List<AbstractIFNetTransition> lowTransitions = highLowTransitions.get(1);
		//A list going to contain all low transitions which a part of a multi causal pattern
		List<AbstractIFNetTransition> multicCausaLowTransitions = new LinkedList<AbstractIFNetTransition>(lowTransitions);
		//find all low transitions which are part of a multi causal pattern
		
		for (AbstractIFNetTransition lowTrans : lowTransitions) {
			
			IFNetFlowRelation toLowTrans = place.getRelationTo(lowTrans);
			Multiset<String> lowConsumedTokens = toLowTrans.getConstraint();
		
			for (AbstractIFNetTransition highTrans : highTransitions) {
				IFNetFlowRelation fromHighTrans = place.getRelationFrom(highTrans);
				Multiset<String> highProducedTokens = fromHighTrans.getConstraint();		
					
				if(highProducedTokens.intersection(lowConsumedTokens).isEmpty()){
					multicCausaLowTransitions.remove(lowTrans);
					break;
				}
				
			}
		}
		
		//Set the found low transitions
		highLowTransitions.set(1, multicCausaLowTransitions);
		
		
	}
	
	
	/**
	 *This method generates the LTL formulas for all mono causal places
	 * @throws ParameterException 
	*/
	protected StringBuilder generateMonoCausalPlaceLTL() throws ParameterException {
		
		StringBuilder ltlFormulaBuilder = new StringBuilder();

		// Iterate over all Places
		for (IFNetPlace currentPlace : iFNet.getPlaces()) {

			// Get the high transitions in the preset of the place and the low
			// transitions in the postset.
			List<List<AbstractIFNetTransition>> presetHighAndPostsetLowTransitions = getPresetHighAndPostsetLowTransitions(currentPlace);
	
			// Check for each combination of high and low transition whether
			// they produce/consume the same token color in the place
			List<List<AbstractIFNetTransition>> potentialMonoCausalPatterns = getTransitionsPairsWithCommonColorInPlace(
					presetHighAndPostsetLowTransitions, currentPlace);
			
			//Iterate over all potential mono causal patterns in order to generate a comment and a formula for each
			Iterator<List<AbstractIFNetTransition>> potentialMonoCausalPatternsIter = potentialMonoCausalPatterns.iterator();
			List<AbstractIFNetTransition> highTransitions = potentialMonoCausalPatternsIter.next();
			List<AbstractIFNetTransition> lowTransitions = potentialMonoCausalPatternsIter.next();
			
			//If there really is a potential mono causal pattern
			if(!(highTransitions.isEmpty() || lowTransitions.isEmpty())){
				
				Iterator<AbstractIFNetTransition> highIter = highTransitions.iterator();
				Iterator<AbstractIFNetTransition> lowIter = lowTransitions.iterator();
				
				while(highIter.hasNext() && lowIter.hasNext()){
					AbstractIFNetTransition currentHighTransition = highIter.next();
					AbstractIFNetTransition currentLowTransition = lowIter.next();
					
					//Create the comment and append it to the StringBuilder
					ltlFormulaBuilder.append(createMonoCausalComment(currentHighTransition, currentPlace, currentLowTransition));
					
					//create the LTL formula and append it to the string builder.
					ltlFormulaBuilder.append(createMonoCausalLTL(currentHighTransition, currentLowTransition));	
				}				
			}	
		}
		
		return ltlFormulaBuilder;
	}
	
	
	/**
	 *This method generates the LTL formula for mono causal places for the given transitions.
	 * @throws ParameterException 
	 * 
	*/
	private StringBuilder createMonoCausalLTL(AbstractIFNetTransition highTransition, AbstractIFNetTransition lowTransition)
			throws ParameterException {

		//Example formula:
		//P=? [ F(t1_last >0 & td=0) ] / P=? [ F(t1_last >0) ]
		//
		//Part representing low enabledness:
		//t1_last >0)
		
		//Generate the part representing that low is enabled
		StringBuilder lowEnabledBuilder = new StringBuilder();		
		lowEnabledBuilder.append(lowTransition.getName() + "_last >0");
		
		//Generate the actual LTL formula now ...
		StringBuilder monoCausalLTLBuilder = new StringBuilder();
		monoCausalLTLBuilder.append("P=? [ F(" + lowEnabledBuilder.toString() + " & " + highTransition.getName() + "=0) ] / P=? [ F(" + lowEnabledBuilder.toString() + ") ]" + "\n" + "\n");
		
		return monoCausalLTLBuilder;	
	}
	
	
	
	/**
	 *This method generates a comment for an mono causal potential place 
	 * @throws ParameterException 
	 * 
	*/
	private StringBuilder createMonoCausalComment(AbstractIFNetTransition highTransition, IFNetPlace potentialMonoCausalPlace,
			AbstractIFNetTransition lowTransition) throws ParameterException {

		StringBuilder commentBuilder = new StringBuilder();
		commentBuilder.append("//Place " + potentialMonoCausalPlace + " is a potential mono causal place." + "\n");
		commentBuilder.append("//The involved high transitions is " + highTransition + " (DeclassificationTransition = "+ highTransition.isDeclassificator() + ")." + "\n");
		commentBuilder.append("//The involved low transitions is " + lowTransition + "." + "\n");
		
		 
		
		//Compute the shared color
		IFNetFlowRelation toPlace = potentialMonoCausalPlace.getRelationFrom(highTransition);
		IFNetFlowRelation fromPlace = potentialMonoCausalPlace.getRelationTo(lowTransition);
		commentBuilder.append("//The following token colors are involved: " + toPlace.getConstraint().intersection(fromPlace.getConstraint()) + "." + "\n");
		
		commentBuilder.append("//Possible results for mono causal places:"+ "\n");
		commentBuilder.append("//[0;0]: High always fired if low can fires."+ "\n");
		commentBuilder.append("//(0;1): High sometimes fired if low can fire"+ "\n");
		commentBuilder.append("//[1;1]: High never fired if low can fire"+ "\n");
	
		return commentBuilder;
		
	}
	
	
	
	/**
	 * This method computes for the given transitions pairs whether they
	 * consume/produce a common token color in a certain place.
	 * 
	 * @return List<List<AbstractIFNetTransition>> The transitions which both
	 *         cosume/produce a common token color in the given place.
	 * @throws ParameterException
	 */
	private List<List<AbstractIFNetTransition>> getTransitionsPairsWithCommonColorInPlace(
			List<List<AbstractIFNetTransition>> transitionPairs, IFNetPlace place) throws ParameterException {
					// Check for each combination of high and low transition whether
					// they produce/consume the same token color in the place
					// Note: only do this if there is at least on high and one low
					// transitions
		Iterator<List<AbstractIFNetTransition>> prePostTransIter = transitionPairs.iterator();
					//The list going to contain all pairs of transitions which are part of a potential mono causal place
		List<List<AbstractIFNetTransition>> potentialMonoCausalPatterns = new LinkedList<List<AbstractIFNetTransition>>();
		List<AbstractIFNetTransition> potentialMonoCausalHighTransitions = new LinkedList<AbstractIFNetTransition>();
		List<AbstractIFNetTransition> potentialMonoCausalLowTransitions = new LinkedList<AbstractIFNetTransition>();
					potentialMonoCausalPatterns.add(potentialMonoCausalHighTransitions);
					potentialMonoCausalPatterns.add(potentialMonoCausalLowTransitions);
					
					if (!((prePostTransIter.next().isEmpty()) || (prePostTransIter.next().isEmpty()))) {

			ListCombinationGenerator<AbstractIFNetTransition> lcg = new ListCombinationGenerator<AbstractIFNetTransition>(transitionPairs);
						while (lcg.hasMoreCombinations()) {
							
							//Get one combination of high transition in the preset and low transition in the postset of the connecting place
				AbstractIFNetTransition[] currentHighLowCombination = lcg.getNextCombination().toArray(new AbstractIFNetTransition[0]);
							
				IFNetFlowRelation toPlace = place.getRelationFrom(currentHighLowCombination[0]);
				IFNetFlowRelation fromPlace = place.getRelationTo(currentHighLowCombination[1]);

							
							if(!toPlace.getConstraint().intersection(fromPlace.getConstraint()).isEmpty()){
								//It is a potential mono causal place
								potentialMonoCausalHighTransitions.add(currentHighLowCombination[0]);
								potentialMonoCausalLowTransitions.add(currentHighLowCombination[1]);						
							}
							
							
						}
						
						
					}
					
					return potentialMonoCausalPatterns;
	}
	
	
	
	/**
	 * This method returns transitions in the preset of a place which are
	 * classified as High and transitions which are in the postset of the same
	 * place and are classified as Low.
	 * 
	 * @return List<List<AbstractIFNetTransition>> The first sublist contains
	 *         the transitions in the preset and the second sublist contains the
	 *         transitions in the postset.
	 * @throws ParameterException
	 */
	private List<List<AbstractIFNetTransition>> getPresetHighAndPostsetLowTransitions(IFNetPlace p) throws ParameterException {
		
		
		//Check whether at least one transition in the preset is labeled as High 
		//and remember the high transitions in a list
		List<IFNetFlowRelation> inRelations = p.getIncomingRelations();
		//A List going to contain all High transitions in the preset of the current place
		List<AbstractIFNetTransition> highTransitions = new LinkedList<AbstractIFNetTransition>();
		for (IFNetFlowRelation inRel : inRelations) {
			
			//The node must be a transition since it is connected to a place
			AbstractIFNetTransition preTrans = (AbstractIFNetTransition) inRel.getSource();
			
			//check whether the transition is classified as High
			if (iFNet.getAnalysisContext().getLabeling().getActivityClassification(preTrans.getName()).equals(SecurityLevel.HIGH)) {
				
				highTransitions.add(preTrans);
			}																
		}
		
		
		//Check whether at least one transition in the postset is labeled as Low 
		//and remember the low transitions in a list
		List<IFNetFlowRelation> outRelations = p.getOutgoingRelations();
		//A List going to contain all low transitions in the postset of the current place
		List<AbstractIFNetTransition> lowTransitions = new LinkedList<AbstractIFNetTransition>();
		for (IFNetFlowRelation outRel : outRelations) {
			
			//The node must be a transition since it is connected to a place
			AbstractIFNetTransition postTrans = (AbstractIFNetTransition) outRel.getTarget();
			
			//check whether the transition is classified as Low
			if (iFNet.getAnalysisContext().getLabeling().getActivityClassification(postTrans.getName()).equals(SecurityLevel.LOW)) {
				lowTransitions.add(postTrans);
			}																
		}
			
		
		//create the list of lists which is going to be returnde
		List<List<AbstractIFNetTransition>> presetHighAndPostsetLowTransitions = new LinkedList<List<AbstractIFNetTransition>>();
		presetHighAndPostsetLowTransitions.add(highTransitions);
		presetHighAndPostsetLowTransitions.add(lowTransitions);
		
		return presetHighAndPostsetLowTransitions;
		
	}
	

	
}

