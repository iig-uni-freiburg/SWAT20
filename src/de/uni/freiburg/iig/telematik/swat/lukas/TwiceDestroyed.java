package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.Collection;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;

public class TwiceDestroyed extends DataflowPattern {
	
	public static final String NAME = "Twice Destroyed";
	
	public TwiceDestroyed(Token t, Collection<RegularIFNetTransition> collection) {
		super();
		
		String writeToken, deleteToken, formula;
		writeToken = getTransitionsAccessingToken(collection, t, AccessMode.WRITE);
		deleteToken = getTransitionsAccessingToken(collection, t, AccessMode.DELETE);
		
		formula = "F(" + deleteToken + " & (X(!" + writeToken 
				+ " U (" + deleteToken + " & !" + writeToken + "))))";
		setPattern(formula, true);
		mOperands.add(t);
	}

}
