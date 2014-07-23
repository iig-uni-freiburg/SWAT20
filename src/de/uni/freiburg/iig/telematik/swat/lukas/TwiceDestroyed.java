package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.Collection;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;

public class TwiceDestroyed extends DataflowPattern {
	
	public static final String NAME = "Twice Destroyed D";
	
	public TwiceDestroyed(Token t, Collection<RegularIFNetTransition> collection) {
		super("A data element D is destroyed twice without having been created in between.");
		
		String writeToken, deleteToken, formula;
		writeToken = getTransitionsAccessingToken(collection, t, AccessMode.WRITE);
		deleteToken = getTransitionsAccessingToken(collection, t, AccessMode.DELETE);
		
		formula = "F(" + deleteToken + " & (X(!" + writeToken 
				+ " U (" + deleteToken + " & !" + writeToken + "))))";
		setPattern(formula, true);
		mOperands.add(t);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

}
