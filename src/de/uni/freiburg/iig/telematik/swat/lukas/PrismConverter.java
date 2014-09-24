package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.Calendar;
import java.util.Iterator;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.DeclassificationTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;

public abstract class PrismConverter {
	
	private final String VERSION = "1.0";
	
	protected AbstractPetriNet<?,?,?,?,?,?,?> mAbstractNet;
	
	protected boolean bounded = true;
	
	public boolean isBoundedNet() {
		return bounded;
	}
	
	protected PrismConverter(AbstractPetriNet<?,?,?,?,?,?,?> net) {
		mAbstractNet = net;
	}
	
	public StringBuilder convert() {
		
		StringBuilder placeVars = createPlaceVars();
		StringBuilder transitionVars = createTransitionVars();
		StringBuilder transitions = createTransitions();
		return composeModel(transitionVars, placeVars, transitions);
	}
	
	public AbstractPetriNet<?,?,?,?,?,?,?> getNet() {
		return mAbstractNet;
	}
	
	private StringBuilder createTransitionVars() throws ParameterException {
		
		StringBuilder transitionVarBuilder = new StringBuilder();
		
		for (AbstractTransition<?,?> t : mAbstractNet.getTransitions()) {
			transitionVarBuilder.append("//TransitionName: " + t.getName() + " "
					+ "(TransitionLabel: "  + t.getLabel() + ")" + "\n");
			
			if(t instanceof DeclassificationTransition){
				transitionVarBuilder.append("//Declassification Transition" + "\n");
			} else {
				transitionVarBuilder.append("//Regular Transition" + "\n");
			}
			
			transitionVarBuilder.append(t.getName() + " : [0..1] init 0;" + "\n");
			transitionVarBuilder.append(t.getName() + "_last"+" : [0..1] init 0;" + "\n"+ "\n");
		}
		
		return transitionVarBuilder;
	}
	
	protected StringBuilder composeModel(StringBuilder transitionVars,
			StringBuilder placeVars, StringBuilder transitions) {
				
		StringBuilder prismModelBuilder = new StringBuilder();
		prismModelBuilder.append("//generated at: " + Calendar.getInstance().getTime()+ "\n");
		prismModelBuilder.append("//by user: " + System.getProperty("user.name") + "\n");
		prismModelBuilder.append("//with converter Version: " + VERSION + "\n");
		prismModelBuilder.append("\n");
		prismModelBuilder.append("//Nondeterminism is resolved probabilistically in this model!"+ "\n");
		prismModelBuilder.append("dtmc"+ "\n");
		prismModelBuilder.append("\n");
	
		
		prismModelBuilder.append("module IFNet" + "\n");
		prismModelBuilder.append("\n");
		
		// append place variables
		prismModelBuilder.append(placeVars);
		
		//append transition variables
		prismModelBuilder.append(transitionVars);
		
		// append transitions
		prismModelBuilder.append(transitions);
		
		if (mAbstractNet.getDrainPlaces().size() == 1) {
			prismModelBuilder.append(createTerminationLoops());
		} else {
			System.out.println("Warning! The current net isn't sound. Computations can be inaccurate.");
		}
							
		prismModelBuilder.append("endmodule");
		prismModelBuilder.append("\n");	
						
		return prismModelBuilder;		
	}
	
    private StringBuilder createTerminationLoops() {
		
		StringBuilder termLoopBuilder = new StringBuilder();
		String enableCondition = ""; 
		@SuppressWarnings("unchecked")
		Iterator<AbstractPlace<?,?>> placeIter = 
				(Iterator<AbstractPlace<?, ?>>) mAbstractNet.getDrainPlaces().iterator();
		
		while(placeIter.hasNext()){
			AbstractPlace<?,?> curPlace = placeIter.next();
			if (placeIter.hasNext()) {
				enableCondition += curPlace.getName() + "_black>=1 |";
			} else {
				enableCondition += curPlace.getName() + "_black>=1";
			}
		}
		
		String loopEffect = "";
		@SuppressWarnings("unchecked")
		Iterator<AbstractTransition<?,?>> transIter = (Iterator<AbstractTransition<?, ?>>) 
				mAbstractNet.getTransitions().iterator();
		
		while(transIter.hasNext()){
			AbstractTransition<?,?> t = transIter.next();
			if (transIter.hasNext()) {
				loopEffect += "(" + t.getName() + "_last'= 0) &";
			} else {
				loopEffect += " (" + t.getName() + "_last'= 0)";
			}
		}
		
		termLoopBuilder.append("[] ");	
		termLoopBuilder.append(enableCondition);
		termLoopBuilder.append(" -> ");
		termLoopBuilder.append(loopEffect);
		termLoopBuilder.append(";\n\n");
		
		return termLoopBuilder;
		
	}

	
	protected abstract StringBuilder createPlaceVars();
	
	protected abstract StringBuilder createTransitions();
	
	public static void main(String [] args) {
		
		PTNet net = new PTNet(); 
		net.addPlace("p1");
		net.getPlace("p1").setCapacity(1);
		net.addPlace("p2");
		net.getPlace("p2").setCapacity(1);
		net.addPlace("p3");
		net.getPlace("p3").setCapacity(1);
		
		net.addTransition("t1");	
		net.addFlowRelationPT("p1", "t1");
		net.addFlowRelationTP("t1", "p2");
		
		net.addTransition("t2");	
		net.addFlowRelationPT("p2", "t2");
		net.addFlowRelationTP("t2", "p3");
		
		PTNetConverter c = new PTNetConverter(net);
		StringBuilder sb = c.convert();
		System.out.println(sb.toString());
		
	}


}
