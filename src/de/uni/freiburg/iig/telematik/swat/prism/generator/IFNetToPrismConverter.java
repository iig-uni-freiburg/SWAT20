package de.uni.freiburg.iig.telematik.swat.prism.generator;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.DeclassificationTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetTestUtil;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;

public class IFNetToPrismConverter {

	//The IFNet which should be exported to prism
	private IFNet iFNet;
	
	//The version of the converter
	private final String VERSION = "0.2";
	
	//The name of the variable used to reset the _last variables
	private final String RESET_VARIABLE = "reset_last";
	
	public IFNetToPrismConverter(IFNet IFNet) {
		this.iFNet = IFNet;
	}
	
	
	/**
	 * This method converts an IFNet place to its prism representation. Example:
	 * P1 can contain 1xGreen, 2xRed, 5xBlack tokens The initial marking p1 is
	 * 0xGreen, 1xRed, 2xBlack tokens global p1_Green : [0..1] init 0; global
	 * p1_Red : [0..2] init 1; global p1_Black : [0..5] init 2;
	 * 
	 * @throws ParameterException
	 */
	

	private StringBuilder IFNetPlaceToPrism(IFNetPlace p) throws ParameterException {
		
		//The string builder is going to contain the string
		//representing the place in prism 
		StringBuilder placeBuilder = new StringBuilder();
		//Add a prism comment first
		placeBuilder.append("//PlaceName: " + p.getName() +" (PlaceLabel: " + p.getLabel() +")" + "\n");
		
		//Check whether the current place is the source place of the IFNet
		//since the sourceplace is handled differntly (it has always the initial
		//Marking 1x black and no further capacities).
		if (!iFNet.getSourcePlaces().contains(p)) {
		
		//add an additional comment if the place ist a drainPlace
			if (iFNet.getDrainPlaces().contains(p)) {
			placeBuilder.append("//drainPlace" + "\n");
		}
			
		//collect the colors that can be contained in the place
			LinkedList<IFNetFlowRelation> inRelations = new LinkedList<IFNetFlowRelation>(p.getIncomingRelations());
		Set<String> possibleColors = new HashSet<String>();
			for (IFNetFlowRelation inRel : inRelations) {
			possibleColors.addAll(inRel.getConstraint().support());			
		}

	
		
		//iterate over each color and build the line for the variable of that color
		for(String color : possibleColors){
			//add the global tag and the variable representing 
			//the amount of the current color e.g. "global p1_Green :"
			placeBuilder.append("global " + p.getName() +"_" + color + " : ");
			
			//Add the part which represents the color capacity of the place
			//for this particular color e.g. "[0..1]"
			placeBuilder.append("[0.." + p.getColorCapacity(color) + "] ");
			
			//find out how many tokens of this color are contained in p
			//in the initial marking and add the initial marking part
			//to the string builder e.g. init 0;
				if (iFNet.getInitialMarking().get(p.getName()) != null) {
				//The token color is contained in the initial marking in the place
					placeBuilder.append("init " + iFNet.getInitialMarking().get(p.getName()).multiplicity(color) + ";" + "\n");
			}else{
				//The token color is NOT contained in the initial marking in the place
				placeBuilder.append("init 0;" + "\n");
			}
			
			
		}
		}else{
			//The current place is a sourceplace ==> always 1xControlflowToken
			//global p1_Green : [0..1] init 0;
			placeBuilder.append("//SourcePlace" + "\n");
			placeBuilder.append("global " + p.getName() + "_" + iFNet.CONTROL_FLOW_TOKEN_COLOR + " : [0..1] init 1;" + "\n");
		}
		
		return placeBuilder.append("\n");
	}
	
	
	
	/**
	 * This method creates one Prism Variables for each IFNet transitions. The
	 * variable stores whether the transition already fired or not. Example:
	 * global t1 : [0..1];
	 * 
	 * @throws ParameterException
	 */	
	private StringBuilder createIFNetTransitionVariable(AbstractIFNetTransition t) throws ParameterException {
	
		//The string builder is going to contain the string
		//representing the transition variables in prism 
		StringBuilder transitionVarBuilder = new StringBuilder();
		transitionVarBuilder.append("//TransitionName: " + t.getName() + " (TransitionLabel: "  + t.getLabel() + ")" + "\n");
		
		//add as prism comment whether the transition is declassification or regular transition
		if (t instanceof RegularIFNetTransition) {
			transitionVarBuilder.append("//Regular Transition" + "\n");
		}
		if(t instanceof DeclassificationTransition){
			transitionVarBuilder.append("//Declassification Transition" + "\n");
		}
		
		transitionVarBuilder.append("global " +t.getName() + " : [0..1] init 0;" + "\n");
		transitionVarBuilder.append("global " +t.getName() + "_last"+" : [0..1] init 0;" + "\n"+ "\n");
		
		return transitionVarBuilder;
	}
	
	

	/**
	 *This method creates the comment right before a transition in the prism model.
	 *Example:
	 *Transition: tOut with clearence HIGH
	 *Assigned Subject: sh2 with clearence HIGH
     *From place p2: 1xblack, 1xgreen
     *From place p3: 1xblack, 1xblue
     *To place pOut: 1xblack
	 * @throws ParameterException 
	*/	
	private StringBuilder IFNetTransitionToPrismComment(AbstractIFNetTransition t) throws ParameterException {
	
		
		//The string builder containing the comments right before each transition
		StringBuilder transitionCommentBuilder = new StringBuilder();
		
		//Add some general comments first
		String transitionClassification = iFNet.getAnalysisContext().getLabeling().getActivityClassification(t.getName()).toString();
		String subjectDescriptor = iFNet.getAnalysisContext().getSubjectDescriptor(t.getName());
		String subjectClearence = iFNet.getAnalysisContext().getLabeling().getSubjectClearance(subjectDescriptor).toString();
		transitionCommentBuilder.append("//Transition: " + t.getName() + " with clearence "+ transitionClassification +"\n");
		transitionCommentBuilder.append("//Assigned Subject: " + subjectDescriptor + " with clearence "+ subjectClearence +  "\n");
		
		//get a sorted list of inplaces and a map of corresponding in relations
		LinkedList<String> inPlacesList = new LinkedList<String>();
		HashMap<String, IFNetFlowRelation> inPlaceNameFlowRelationMap = new HashMap<String, IFNetFlowRelation>();
		for (Object inRel : t.getIncomingRelations()) {
			inPlacesList.add(((AbstractFlowRelation<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, Multiset<String>>) inRel)
					.getPlace().getName());
			inPlaceNameFlowRelationMap.put(
					((AbstractFlowRelation<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, Multiset<String>>) inRel).getPlace()
							.getName(), (IFNetFlowRelation) inRel);
		}
		Collections.sort(inPlacesList);
		
		//start to create the input side
		//process the inPlaces in alphabetical order to create better readable comments
		for(String  inPLace : inPlacesList){
			
			IFNetFlowRelation inRel = inPlaceNameFlowRelationMap.get(inPLace);
			
			//get inplace connected to the transition via inrel
			//and the tokens needed in that place
			IFNetPlace inPlace = inRel.getPlace();
			Multiset<String> inConstraint = inRel.getConstraint();
			
			//Create comments how many tokens are needed from that place
			//e.g.: //From P1: 1xRed, 1xGreen, 1xBlack
			transitionCommentBuilder.append("//From place " + inPlace.getName() + ": ");						
			Iterator<String> colorIter = new TreeSet<String>(inConstraint.support()).iterator();			 
			String currentColor = null;
			while(colorIter.hasNext()){
				currentColor=colorIter.next();
				if(colorIter.hasNext()){
					//Comma at the end but no newline
					transitionCommentBuilder.append(inConstraint.multiplicity(currentColor) + "x" + currentColor + ", " );
				}else{
					//newline at the end but no comma
					transitionCommentBuilder.append(inConstraint.multiplicity(currentColor) + "x" + currentColor + "\n" );					
				}
			}	
		}
		
		
		//get a sorted list of outplaces and a map of corresponding out relations
		LinkedList<String> outPlacesList = new LinkedList<String>();
		HashMap<String, IFNetFlowRelation> outPlaceNameFlowRelationMap = new HashMap<String, IFNetFlowRelation>();
		for (Object outRelObject : t.getOutgoingRelations()) {
			IFNetFlowRelation outRel = (IFNetFlowRelation) outRelObject;
			outPlacesList.add(outRel.getPlace().getName());
			outPlaceNameFlowRelationMap.put(outRel.getPlace().getName(), outRel);
		}
		Collections.sort(outPlacesList);
		
		//start to create the output side
		//process the outPlaces in alphabetical order to create better readable comments
		for(String  outPLace : outPlacesList){
					
			IFNetFlowRelation outRel = outPlaceNameFlowRelationMap.get(outPLace);
			
			//get outplace connected to the transition via outRel
			//and the tokens created in that place
			IFNetPlace outPlace = outRel.getPlace();
			Multiset<String> outConstraint = outRel.getConstraint();
			 
			//Create comments how many tokens are created in that place
			//e.g.: //TO P2: 1xRed, 1xGreen, 1xBlack
			transitionCommentBuilder.append("//To place " + outPlace.getName() + ": ");						
			Iterator<String> colorIter = new TreeSet<String>(outConstraint.support()).iterator();			 
			String currentColor = null;
			while(colorIter.hasNext()){
				currentColor=colorIter.next();
				if(colorIter.hasNext()){
					//Comma at the end but no newline
					transitionCommentBuilder.append(outConstraint.multiplicity(currentColor) + "x" + currentColor + ", " );
				}else{
					//newline at the end but no comma
					transitionCommentBuilder.append(outConstraint.multiplicity(currentColor) + "x" + currentColor + "\n" );					
				}
			}	
		}
		
		
		return transitionCommentBuilder;
	}
	
	
	
	/**
	 *This method creates the representation of a transition in prism.
	 *That is it adds the condition for the transition being enabled and
	 *the effect of firing that transition to the prism model.	
	 *Example:
	 *[] p1_Green>0 & p1_Red>0 & p1_Black>0 & p2_Red<1 & p2_Black<1  -> (p1_Green'=p1_Green-1) & (p1_Red'=p1_Red-1) & (p1_Black'=p1_Black-1) & (p2_Red'=p2_Red+1) & (p2_Black'=p2_Black+1) & (t1'=1);
	 * @throws ParameterException 
	*/	
	private StringBuilder IFNetTransitionToPrismFiring(AbstractIFNetTransition t) throws ParameterException {
		
		//The representation consists of three parts.
		//1) the enabeldeness Part (on the left of ->)
		//2) the effect of firing the transition (on the right of ->)
		//3) remebering if a transition has already fired (the last three entries on the right of ->)
		//
		//Part 1) consists of three subparts
		//1a) Are there enough tokens in inputplaces?
		//1b) Is there enough space in output places?		
		//
		//Part 2) consists of three subparts
		//2a) Removing tokens from inplaces
		//2b) Adding tokens tp outplaces
		
		
		
		//The stringBuilder going to contain the transition represntation
		//on the left of ->
		//i.e. the part deciding whether a transition is enabled
		StringBuilder enablednessBuilder = new StringBuilder();
		
		//The stringBuilder going to contain the transition represntation
		//on the right of ->
		//i.e. the part deciding whats the effect of firing a transition
		StringBuilder firingEffectBuilder = new StringBuilder();
		
		
		//Start creating enabeldness part  1a) and
		//start creating the firingeffect part 2a)
		
		//iterat over all incomming relations
		for (Object inRelObject : t.getIncomingRelations()) {
			IFNetFlowRelation inRel = (IFNetFlowRelation) inRelObject;
			//An inputplace of t
			IFNetPlace inPlace = inRel.getPlace();
			Multiset<String> inConstraint = inRel.getConstraint();
			TreeSet<String> colors = new TreeSet<String>(inRel.getConstraint().support());

			//add all requirements of the current inplace to the transition representation
			for(String color : colors){				
				String prismVariableName = inPlace.getName() + "_" + color;
				
				enablednessBuilder.append(prismVariableName + ">" + (inConstraint.multiplicity(color)-1) + " & "); 				
				firingEffectBuilder.append("("+ prismVariableName +"'=" + prismVariableName + "-" + inConstraint.multiplicity(color) + ") & ");				
			}						
		}
		
		
		//Start creating the scond enabeldness part i.e. 1b)
		//the second firingeffect part 2b)
		
		//iterat over all outgoing relations
		Iterator<IFNetFlowRelation> outFlowIter = t.getOutgoingRelations().iterator();
		while(outFlowIter.hasNext()){			
			IFNetFlowRelation outRel = outFlowIter.next();
			
			//An outputplace of t
			IFNetPlace outPlace = outRel.getPlace();
			Multiset<String> outConstraint = outRel.getConstraint();
			TreeSet<String> colors = new TreeSet<String>(outRel.getConstraint().support());

			//add all requirements of the current outplace to the transition representation
			Iterator<String> colorIter = colors.iterator();
			while(colorIter.hasNext()){
				String color = colorIter.next();
				//compute the space needed in the outplace
				int neededSpace = (outPlace.getColorCapacity(color) - outConstraint.multiplicity(color)) +1;
				
				String prismVariableName = outPlace.getName() + "_" + color;				
				if(colorIter.hasNext() || outFlowIter.hasNext()){
					enablednessBuilder.append(prismVariableName + "<" + neededSpace + " & ");
				}else{
					enablednessBuilder.append(prismVariableName + "<" + neededSpace);
				}
				
				
				firingEffectBuilder.append("(" + prismVariableName + "'=" + prismVariableName + "+" + outConstraint.multiplicity(color) + ")" + " & ");				
			}						
		}
		
		//Start creating part 3)
		//(t3'=1); ==> remember that t3 has fired somewhere in the past
		firingEffectBuilder.append("(" + t.getName()+"'=1"+ ") & " );
		//(t3_last'=1); ==> remember that t3 has fired as last transition
		firingEffectBuilder.append("(" + t.getName()+"_last'=1"+ ") & ");
		//set the reset variable
		firingEffectBuilder.append("(" + RESET_VARIABLE +"'=1"+ ");" + "\n");
		//disable transitions if resetVariable is set
		enablednessBuilder.append(" & " + RESET_VARIABLE + "<1 ");
		
		
		
		//Combine Part 1) and Part 2)
		StringBuilder completeTransitionBuilder = new StringBuilder();
		completeTransitionBuilder.append("[] ");
		completeTransitionBuilder.append(enablednessBuilder);
		completeTransitionBuilder.append(" -> ");
		completeTransitionBuilder.append(firingEffectBuilder);
		completeTransitionBuilder.append("\n\n");
				
		
		return completeTransitionBuilder;
	}
	
	
	/**
	 *This method generates the part of the PRISM model which represent the variable used to reset tha _last variables
	 * @throws ParameterException 
	*/
	private StringBuilder creatResetVariable() throws ParameterException{
				
		StringBuilder resetVariableBuilder = new StringBuilder();		
		resetVariableBuilder.append("//A variable used to reset the _last variables.\n");
		resetVariableBuilder.append("global " + RESET_VARIABLE + " : [0..1] init 0; \n");
		resetVariableBuilder.append("\n");
		resetVariableBuilder.append("\n");
		
		return resetVariableBuilder;
	}
	
	
	/**
	 *This method generates the part of the PRISM model which resets the _last variables and the resetVariables
	 * @throws ParameterException 
	*/
	private StringBuilder createResetRelation() throws ParameterException{
		//insert transition to reset the _last varaibles and the resetVariable
		
		//Add a comment first
		StringBuilder resetTransitionBuilder = new StringBuilder();
		resetTransitionBuilder.append("//This resets the variable " + RESET_VARIABLE + " and the _last variables." + "\n");
		
		//The lefthand side is always the same
		resetTransitionBuilder.append("[] " + RESET_VARIABLE + ">0 -> ");
		 
		//reset all _last variables
		for (AbstractIFNetTransition t : iFNet.getTransitions()) {
				resetTransitionBuilder.append("(" + t.getName()+"_last'=0"+ ") & ");
		}
		
		//reset the resetVariable
		resetTransitionBuilder.append("(" +RESET_VARIABLE+ "'=0);" + "\n" + "\n");
		
		return resetTransitionBuilder;
		
	}
	
	
	/**
	 * This method converts a IFNet to its prism representation
	 * 
	 * @throws ParameterException
	 */
	public StringBuilder ConvertIFNetToPrism() throws ParameterException {
		
		//The StringBuilder which is going to contain the whole prism model of the IFNet
		StringBuilder prismModelBuilder = new StringBuilder();
		
		//Add a comment about the model first
		if (iFNet.getName().trim().isEmpty()) {
			prismModelBuilder.append("//Prism Model of IFNet: NAME NOT SET ..." + "\n");
		}else{
			prismModelBuilder.append("//Prism Model of IFNet: " + iFNet.getName() + "\n");
		}
		
		
		
		prismModelBuilder.append("//generated at: " + Calendar.getInstance().getTime()+ "\n");
		prismModelBuilder.append("//by user: " + System.getProperty("user.name") + "\n");
		prismModelBuilder.append("//with converter Version: " + VERSION + "\n");
		prismModelBuilder.append("\n");
		prismModelBuilder.append("\n");
		prismModelBuilder.append("//Nondeterminism is resolved probabilistically in this model!"+ "\n");
		prismModelBuilder.append("dtmc"+ "\n");
		prismModelBuilder.append("\n");
		prismModelBuilder.append("\n");
		
		//Start adding place variables
		prismModelBuilder.append("///////////////////////////////"+ "\n");
	    prismModelBuilder.append("//Define variables for places//"+ "\n");
		prismModelBuilder.append("///////////////////////////////"+ "\n");
		
		for (IFNetPlace p : iFNet.getPlaces()) {
			prismModelBuilder.append(this.IFNetPlaceToPrism(p));
		}
		
		
		//Start to handle transitions
		prismModelBuilder.append("////////////////////////////////////"+ "\n");
		prismModelBuilder.append("//Define variables for transitions//"+ "\n");
		prismModelBuilder.append("////////////////////////////////////"+ "\n");
		prismModelBuilder.append("\n");
		prismModelBuilder.append("//Each variable is set to one if the corresponding transition fired." + "\n");
		prismModelBuilder.append("//Variables equal to transition names are set to one if the transition has fired somewhere in the past." + "\n");
		prismModelBuilder.append("//Variables ending with _last are set to one if corresponding transition is the last one that fired." + "\n");
		prismModelBuilder.append("\n");
		prismModelBuilder.append("\n");
		for (AbstractIFNetTransition t : iFNet.getTransitions()) {
			prismModelBuilder.append(this.createIFNetTransitionVariable(t));
		}
		
		//add the variable which is used to set the _last variables back to zero
		prismModelBuilder.append(creatResetVariable());
		
		
		//Start the relations
		prismModelBuilder.append("/////////////////////////" + "\n");
		prismModelBuilder.append("//Define flowrelations //" + "\n");
		prismModelBuilder.append("/////////////////////////" + "\n");
		
		
		//open a prism module
		prismModelBuilder.append("\n");
		prismModelBuilder.append("\n");		
		prismModelBuilder.append("module IFNet" + "\n");
		prismModelBuilder.append("\n");
		for (AbstractIFNetTransition t : iFNet.getTransitions()) {
			prismModelBuilder.append(this.IFNetTransitionToPrismComment(t));
			prismModelBuilder.append(this.IFNetTransitionToPrismFiring(t));
		}
		
	
		//insert transition to reset the _last varaibles and the resetVariable
		prismModelBuilder.append(createResetRelation());
		
		//close the prism module					
		prismModelBuilder.append("endmodule");
		prismModelBuilder.append("\n");	
				
		
		//Model is done now
		return prismModelBuilder;
		
	}
	
	
	public static void main(String[] args) throws ParameterException{
		
		//create a simple IFNet
		IFNet IFNet = IFNetTestUtil.createSimpleSnetWithDeclassification();
		
		//Create convert to prism
		IFNetToPrismConverter converter = new IFNetToPrismConverter(IFNet);
		StringBuilder prismModel = converter.ConvertIFNetToPrism();
		System.out.println(prismModel);
	}
	
	
}
