package de.uni.freiburg.iig.telematik.swat.lukas.patterns;

import java.util.Collection;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Token;

public class NeverDestroyed extends DataflowAntiPattern {
	
	public static final String NAME = "Never Destroyed D";
	
	public static final String DESC = "A data element D is created, but not destroyed afterwards.";
	
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
		
		setPrismProperty(formula, true);
		
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESC;
	}


}
