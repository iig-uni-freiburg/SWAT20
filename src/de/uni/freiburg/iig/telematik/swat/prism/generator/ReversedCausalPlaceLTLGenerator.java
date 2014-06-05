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



public class ReversedCausalPlaceLTLGenerator {

	//The IFNet for whose reversed causal places the LTL formulas should be generated.
	private IFNet IFNet;

	//How it works:
	//1) iterate over all places
	//2) check for each place whether there is at least one L-Transition in the preset and one H-Transition in the post set
	//3) for each combination of one such L and one such H check whether L produces a token in the investigated place which H needs
	//4) generate the LTL formula and a comment for each L and H pair where 3) holds



	public ReversedCausalPlaceLTLGenerator(IFNet IFNet) {
		this.IFNet = IFNet;
	}




	/**
	 *This method generates the LTL formulas for all multi reversed causal places
	 * @throws ParameterException 
	*/
	protected StringBuilder generateMultiReversedCausalPlaceLTL() throws ParameterException {

		StringBuilder ltlFormulaBuilder = new StringBuilder();

		// Iterate over all Places
		for (IFNetPlace currentPlace : IFNet.getPlaces()) {

			// Get the low transitions in the preset of the place and the high
			// transitions in the postset.
			List<List<AbstractIFNetTransition>> presetLowAndPostsetHighTransitions = getPresetLowAndPostsetHighTransitions(currentPlace);

			// Remove high transitions not part in a potential multi reversed causal place
			removeNonColorDependingHighTransitions(presetLowAndPostsetHighTransitions,currentPlace);

			//Iterate over all potential multi reversed causal patterns in order to generate a comment and a formula for each
			List<AbstractIFNetTransition> lowTransitions = presetLowAndPostsetHighTransitions.get(0);
			List<AbstractIFNetTransition> highTransitions = presetLowAndPostsetHighTransitions.get(1);

			//If there can be a potential multi reversed causal pattern
			if(!(lowTransitions.isEmpty() || highTransitions.isEmpty()) &&  highTransitions.size()>1){

				Iterator<AbstractIFNetTransition> lowIter = lowTransitions.iterator();
				while(lowIter.hasNext()){

					AbstractIFNetTransition currentLowTransition = lowIter.next();

					//find out which low transitions share token colors with the !current! high transition
					List<AbstractIFNetTransition> highTransitionsSharingColorsWithLowTrans = new LinkedList<AbstractIFNetTransition>(
							highTransitions);

					//find the tokens consumed from the place by the low transition
					IFNetFlowRelation fromLowTrans = currentPlace.getRelationFrom(currentLowTransition);
					Multiset<String> lowProducedTokens = fromLowTrans.getConstraint();

					for (AbstractIFNetTransition highTrans : highTransitions) {
						//find the tokens consumed from the place by the high transition
						//Check for each High transition whether it consumed
						IFNetFlowRelation toHighTrans = currentPlace.getRelationTo(highTrans);
						Multiset<String> highConsumedTokens = toHighTrans.getConstraint();

						if(lowProducedTokens.intersection(highConsumedTokens).isEmpty()){
							highTransitionsSharingColorsWithLowTrans.remove(highTrans);
						}
					}


					if(highTransitionsSharingColorsWithLowTrans.size()>1){
						//Create the comment for multi reversed causal place and append it to the StringBuilder
						ltlFormulaBuilder.append(createReversedMultiCausalComment(currentLowTransition,currentPlace, highTransitionsSharingColorsWithLowTrans));

						//create the LTL formula for a multi reversed causal place and append it to the string builder.
						ltlFormulaBuilder.append(createReversedMultiCausalLTL(currentLowTransition,highTransitionsSharingColorsWithLowTrans));
					}
				}
			}
		}
		
		return ltlFormulaBuilder;
	}


	/**
	 *This method generates the LTL formula for multi reversed causal places for the given transitions.
	 * @throws ParameterException 
	 * 
	*/
	private StringBuilder createReversedMultiCausalLTL(AbstractIFNetTransition lowTransition, List<AbstractIFNetTransition> highTransitions)
			throws ParameterException {

		//Example formula:
		//P=? [ F (t1_last>0 & (F (tr1_last>0 | tr2_last>0))) ] / P=? [ F (t1_last>0) ]

		//Generate the part representing that low  fired
		StringBuilder lowFiredBuilder = new StringBuilder();
		//for(IFNetFlowRelation outRel : lowTransition.getIncomingRelations()){

		//		if(outRel.getName()== null){
		//			lowFiredBuilder.append(lowTransition.getName() + "_last >0 &");// + (inConstraint.multiplicity(color)-1) +" & ");
		//		}else{
					lowFiredBuilder.append(lowTransition.getName() + "_last >0");// + (inConstraint.multiplicity(color)-1));
		//		}

		//	}


		//Generate the part representing the high transitions are enabled 
		StringBuilder highEnabledBuilder = new StringBuilder();
		Iterator<AbstractIFNetTransition> highTransIter = highTransitions.iterator();
		while(highTransIter.hasNext()){
			AbstractIFNetTransition currentHighTrans = highTransIter.next();
			if(highTransIter.hasNext()){
				highEnabledBuilder.append(currentHighTrans.getName() + "_last >0 | ");
			}else{
				highEnabledBuilder.append(currentHighTrans.getName() + "_last >0");
			}
		}

		//Generate the actual LTL formula now ...
		StringBuilder multiReversedCausalLTLBuilder = new StringBuilder();
		multiReversedCausalLTLBuilder.append("P=? [ F (" + lowFiredBuilder.toString() + " & (F (" + highEnabledBuilder.toString() + "))) ] / P=? [ F(" + lowFiredBuilder.toString() + ") ]" + "\n" + "\n");

		return multiReversedCausalLTLBuilder;

	}



	/**
	 *This method generates a comment for a multi reversed causal potential place 
	 * @throws ParameterException 
	 * 
	*/
	private StringBuilder createReversedMultiCausalComment(AbstractIFNetTransition lowTransition,
			IFNetPlace potentialMultiReversedCausalPlace, List<AbstractIFNetTransition> highTransitions) throws ParameterException {

		StringBuilder commentBuilder = new StringBuilder();
		commentBuilder.append("//Place " + potentialMultiReversedCausalPlace + " is a potential multi reversed causal place." + "\n");
		commentBuilder.append("//The involved low transition is " + lowTransition + "." + "\n");

		commentBuilder.append("//The involved high transitions are :" + "\n");
		for (AbstractIFNetTransition highTrans : highTransitions) {
			commentBuilder.append("//"+ highTrans + " (DeclassificationTransition = "+ highTrans.isDeclassificator() + ")." + "\n");
		}

		//Compute the shared color
		Multiset<String> lowProducedColors = new Multiset<String>();
		lowProducedColors = lowProducedColors.sum(potentialMultiReversedCausalPlace.getRelationFrom(lowTransition).getConstraint());
		for (AbstractIFNetTransition highTrans : highTransitions) {
			IFNetFlowRelation fromPlace = potentialMultiReversedCausalPlace.getRelationTo(highTrans);
			commentBuilder.append("//The following token colors are involved: " + lowProducedColors.intersection(fromPlace.getConstraint()) + "." + "\n");
		}

		commentBuilder.append("//Possible results for multi reversed causal places:"+ "\n");
		commentBuilder.append("//[0;0]: No involved High does ever fire after Low."+ "\n");
		commentBuilder.append("//(0;1): At least one involved High does sometimes fire after Low."+ "\n");
		commentBuilder.append("//[1;1]: At least one involved High does always fire after Low."+ "\n");



		return commentBuilder;

	}


	/**
	 * This method removes all high transitions from the second sublist, which
	 * do not consume a tokens with a color in the given place which is produced
	 * by a low transition in the first sublist.
	 * 
	 * @return List<List<AbstractIFNetTransition>> The transitions which
	 *         cosume/produce common token colors in the given place.
	 * @throws ParameterException
	 */
	private void removeNonColorDependingHighTransitions(List<List<AbstractIFNetTransition>> lowHighTransitions, IFNetPlace place)
			throws ParameterException {

		//Get the list with low transitions
		List<AbstractIFNetTransition> lowTransitions = lowHighTransitions.get(0);
		//Get the list with high transitions
		List<AbstractIFNetTransition> highTransitions = lowHighTransitions.get(1);

		//A list going to contain all high transitions which a part of a multi reversed causal pattern
		List<AbstractIFNetTransition> multicReversedCausaHighTransitions = new LinkedList<AbstractIFNetTransition>(highTransitions);


		//find all low transitions which are part of a multi reversed causal pattern
		for (AbstractIFNetTransition highTrans : highTransitions) {
			IFNetFlowRelation toHighTrans = place.getRelationTo(highTrans);
			Multiset<String> highConsumedTokens = toHighTrans.getConstraint();

			for (AbstractIFNetTransition lowTrans : lowTransitions) {
				IFNetFlowRelation fromLowTrans = place.getRelationFrom(lowTrans);
				Multiset<String> lowProducedTokens = fromLowTrans.getConstraint();

				if(lowProducedTokens.intersection(highConsumedTokens).isEmpty()){
					multicReversedCausaHighTransitions.remove(highTrans);
					break;
				}

			}
		}

		//Set the found high transitions
		lowHighTransitions.set(1, multicReversedCausaHighTransitions);


	}


	//###############



	/**
	 *This method generates the LTL formulas for all mono reversed causal places
	 * @throws ParameterException 
	*/
	protected StringBuilder generateMonoReversedCausalPlaceLTL() throws ParameterException {

		StringBuilder ltlFormulaBuilder = new StringBuilder();

		// Iterate over all Places
		for (IFNetPlace currentPlace : IFNet.getPlaces()) {

			// Get the low transitions in the preset of the place and the high
			// transitions in the postset.
			List<List<AbstractIFNetTransition>> presetLowAndPostsetHighTransitions = getPresetLowAndPostsetHighTransitions(currentPlace);


			// Check for each combination of low and low transition whether
			// they produce/consume the same token color in the place
			List<List<AbstractIFNetTransition>> potentialMonoReversedCausalPatterns = getTransitionsPairsWithCommonColorInPlace(
					presetLowAndPostsetHighTransitions, currentPlace);

			//Iterate over all potential mono reversed causal patterns in order to generate a comment and a formula for each
			Iterator<List<AbstractIFNetTransition>> potentialMonoReversedCausalPatternsIter = potentialMonoReversedCausalPatterns
					.iterator();
			List<AbstractIFNetTransition> lowTransitions = potentialMonoReversedCausalPatternsIter.next();
			List<AbstractIFNetTransition> highTransitions = potentialMonoReversedCausalPatternsIter.next();

			//If there really is a potential mono reversed causal pattern
			if(!(lowTransitions.isEmpty() || highTransitions.isEmpty())){

				Iterator<AbstractIFNetTransition> lowIter = lowTransitions.iterator();
				Iterator<AbstractIFNetTransition> highIter = highTransitions.iterator();

				while(lowIter.hasNext() && highIter.hasNext()){
					AbstractIFNetTransition currentLowTransition = lowIter.next();
					AbstractIFNetTransition currentHighTransition = highIter.next();

					//Create the comment and append it to the StringBuilder
					ltlFormulaBuilder.append(createMonoReversedCausalComment(currentLowTransition, currentPlace, currentHighTransition));

					//create the LTL formula and append it to the string builder.
					ltlFormulaBuilder.append(createMonoReversedCausalLTL(currentLowTransition, currentHighTransition));
				}
			}
		}

		return ltlFormulaBuilder;
	}




	/**
	 *This method generates the LTL formula for mono reversed causal places for the given transitions.
	 * @throws ParameterException 
	 * 
	*/
	private StringBuilder createMonoReversedCausalLTL(AbstractIFNetTransition lowTransition, AbstractIFNetTransition highTransition)
			throws ParameterException {

		//Example formula:
		//P=? [ F (t1_last>0 & (F (tr1_last>0))) ] / P=? [ F (t1_last>0) ]
		//
		//Part representing high enabledness:
		//p4_black>0 & p4_yellow>0

		//Generate the part representing that high is enabled
		//iterate over all incomming relations of the high transition
		StringBuilder lowEnabledBuilder = new StringBuilder();
		//for(IFNetFlowRelation inRel : highTransition.getIncomingRelations()){


					lowEnabledBuilder.append(lowTransition.getName() + "_last >0");// + (inConstraint.multiplicity(color)-1) + " & ");

		//}

		//Generate the actual LTL formula now ...
		StringBuilder monoCausalLTLBuilder = new StringBuilder();
		monoCausalLTLBuilder.append("P=? [ F (" + lowEnabledBuilder.toString() + " & (F (" + highTransition.getName() + "_last >0))) ] / P=? [ F(" + lowEnabledBuilder.toString() + ") ]" + "\n" + "\n");
		return monoCausalLTLBuilder;

	}



	/**
	 *This method generates a comment for an mono reversed causal potential place 
	 * @throws ParameterException 
	 * 
	*/
	private StringBuilder createMonoReversedCausalComment(AbstractIFNetTransition lowTransition,
			IFNetPlace potentialMonoReversedCausalPlace, AbstractIFNetTransition highTransition) throws ParameterException {

		StringBuilder commentBuilder = new StringBuilder();
		commentBuilder.append("//Place " + potentialMonoReversedCausalPlace + " is a potential mono reversed causal place." + "\n");
		commentBuilder.append("//The involved low transition is " + lowTransition + "." + "\n");
		commentBuilder.append("//The involved high transition is " + highTransition + " (DeclassificationTransition = "+ highTransition.isDeclassificator() + ")." + "\n");

		 

		//Compute the shared color
		IFNetFlowRelation toPlace = potentialMonoReversedCausalPlace.getRelationFrom(lowTransition);
		IFNetFlowRelation fromPlace = potentialMonoReversedCausalPlace.getRelationTo(highTransition);
		commentBuilder.append("//The following token colors are involved: " + toPlace.getConstraint().intersection(fromPlace.getConstraint()) + "." + "\n");

		commentBuilder.append("//Possible results for mono reversed causal places:"+ "\n");
		commentBuilder.append("//[0;0]: High never fire after Low"+ "\n");
		commentBuilder.append("//(0;1): High does sometimes fire after Low"+ "\n");
		commentBuilder.append("//[1;1]: High does always fire after Low."+ "\n");

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
					// Check for each combination of low and high transition whether
					// they produce/consume the same token color in the place
					// Note: only do this if there is at least on low and one high
					// transitions
		Iterator<List<AbstractIFNetTransition>> prePostTransIter = transitionPairs.iterator();
					//The list going to contain all pairs of transitions which are part of a potential mono reversed causal place
		List<List<AbstractIFNetTransition>> potentialMonoCausalPatterns = new LinkedList<List<AbstractIFNetTransition>>();
		List<AbstractIFNetTransition> potentialMonoCausalHighTransitions = new LinkedList<AbstractIFNetTransition>();
		List<AbstractIFNetTransition> potentialMonoCausalLowTransitions = new LinkedList<AbstractIFNetTransition>();
					potentialMonoCausalPatterns.add(potentialMonoCausalHighTransitions);
					potentialMonoCausalPatterns.add(potentialMonoCausalLowTransitions);

					if (!((prePostTransIter.next().isEmpty()) || (prePostTransIter.next().isEmpty()))) {

			ListCombinationGenerator<AbstractIFNetTransition> lcg = new ListCombinationGenerator<AbstractIFNetTransition>(transitionPairs);
						while (lcg.hasMoreCombinations()) {

							//Get one combination of low transition in the preset and high transition in the postset of the connecting place
				AbstractIFNetTransition[] currentHighLowCombination = lcg.getNextCombination().toArray(new AbstractIFNetTransition[0]);

				IFNetFlowRelation toPlace = place.getRelationFrom(currentHighLowCombination[0]);
				IFNetFlowRelation fromPlace = place.getRelationTo(currentHighLowCombination[1]);


							if(!toPlace.getConstraint().intersection(fromPlace.getConstraint()).isEmpty()){
								//It is a potential mono reversed causal place
								potentialMonoCausalHighTransitions.add(currentHighLowCombination[0]);
								potentialMonoCausalLowTransitions.add(currentHighLowCombination[1]);
							}


						}


					}

					return potentialMonoCausalPatterns;
	}



	/**
	 * This method returns transitions in the preset of a place which are
	 * classified as Low and transitions which are in the postset of the same
	 * place and are classified as High.
	 * 
	 * @return List<List<AbstractIFNetTransition>> The first sublist contains
	 *         the transitions in the preset and the second sublist contains the
	 *         transitions in the postset.
	 * @throws ParameterException
	 */
	private List<List<AbstractIFNetTransition>> getPresetLowAndPostsetHighTransitions(IFNetPlace p) throws ParameterException {


		//Check whether at least one transition in the preset is labeled as Low
		//and remember the low transitions in a list
		List<IFNetFlowRelation> inRelations = p.getIncomingRelations();
		//A List going to contain all Low transitions in the preset of the current place
		List<AbstractIFNetTransition> lowTransitions = new LinkedList<AbstractIFNetTransition>();
		for (IFNetFlowRelation inRel : inRelations) {

			//The node must be a transition since it is connected to a place
			AbstractIFNetTransition preTrans = (AbstractIFNetTransition) inRel.getSource();

			//check whether the transition is classified as Low
			if (IFNet.getAnalysisContext().getLabeling().getActivityClassification(preTrans.getName()).equals(SecurityLevel.LOW)) {

				lowTransitions.add(preTrans);
			}
		}


		//Check whether at least one transition in the postset is labeled as High
		//and remember the High transitions in a list
		List<IFNetFlowRelation> outRelations = p.getOutgoingRelations();
		//A List going to contain all high transitions in the postset of the current place
		List<AbstractIFNetTransition> highTransitions = new LinkedList<AbstractIFNetTransition>();
		for (IFNetFlowRelation outRel : outRelations) {

			//The node must be a transition since it is connected to a place
			AbstractIFNetTransition postTrans = (AbstractIFNetTransition) outRel.getTarget();

			//check whether the transition is classified as Low
			if (IFNet.getAnalysisContext().getLabeling().getActivityClassification(postTrans.getName()).equals(SecurityLevel.HIGH)) {
				highTransitions.add(postTrans);
			}
		}


		//create the list of lists which is going to be returnde
		List<List<AbstractIFNetTransition>> presetHighAndPostsetLowTransitions = new LinkedList<List<AbstractIFNetTransition>>();
		presetHighAndPostsetLowTransitions.add(lowTransitions);
		presetHighAndPostsetLowTransitions.add(highTransitions);

		return presetHighAndPostsetLowTransitions;

	}


}
