package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.Collection;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;

public class NeverDestroyed extends DataflowPattern {
	
	public static final String NAME = "Never Destroyed";
	
	public NeverDestroyed(Token t, Collection<RegularIFNetTransition> ts,
			Collection<IFNetPlace> outPlaces) {

		super();
		mOperands.add(t);
		// determine transitions which write or delete the token
		String writeToken, deleteToken, formula, terminationCondition;
		writeToken = getTransitionsAccessingToken(ts, t, AccessMode.WRITE);
		deleteToken = getTransitionsAccessingToken(ts, t, AccessMode.DELETE);
		
		// create the termination condition
		terminationCondition = getTerminationCond(outPlaces);
		
		formula = "F(" + writeToken + " & (X(!(" + deleteToken + " | " + writeToken + ") U "
				+ terminationCondition + ")))";
		
		setPattern(formula, true);
		
	}
	
	@Override
	public String getName() {
		return NAME;
	}


}
