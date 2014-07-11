package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.Collection;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;

public class WeaklyLostData extends DataflowPattern {
	
	public static final String NAME = "Weakly Lost Data";
	
	public WeaklyLostData(Token t, Collection<RegularIFNetTransition> collection) {
		super();
		String writeToken, readToken, deleteToken, formula;
		
		// determine transitions which write, read or delete the token
		writeToken = getTransitionsAccessingToken(collection, t, AccessMode.WRITE);
		readToken = getTransitionsAccessingToken(collection, t, AccessMode.READ);
		deleteToken = getTransitionsAccessingToken(collection, t, AccessMode.DELETE);
		
		formula = "F(" + writeToken + " & (X(!(" + readToken+ " | " 
				+ deleteToken + ") U (" + writeToken +" & " + "!" + readToken + "))))";
		setPattern(formula, true);
		mOperands.add(t);
	}

}
