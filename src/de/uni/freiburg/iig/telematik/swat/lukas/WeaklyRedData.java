package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.Collection;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;

public class WeaklyRedData extends DataflowPattern {
	
	public static final String NAME = "Weakly Redundant Data";

	public WeaklyRedData(Token t, Collection<RegularIFNetTransition> ts,
			Collection<IFNetPlace> outputPlaces) {
		
		super();
		String writeToken, readToken, deleteToken, formula, terminationCondition;

		// determine transitions which write, read or delete the token 
		writeToken = getTransitionsAccessingToken(ts, t, AccessMode.WRITE);
		readToken = getTransitionsAccessingToken(ts, t, AccessMode.READ);
		deleteToken = getTransitionsAccessingToken(ts, t, AccessMode.DELETE);
		
		// create the termination condition
		terminationCondition = getTerminationCond(outputPlaces);
		
		formula = "F(" + writeToken + " & (X((!" + readToken 
				+ ") U (" + terminationCondition + "| (" 
				+ deleteToken + " & " + "(!" + readToken + ")" + ")))))";
		setPattern(formula, true);
		mOperands.add(t);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

}
