package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.Collection;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;

public class MissingData extends DataflowPattern {
	
	public static final String NAME = "Missing Data";

	public MissingData(Token t, Collection<RegularIFNetTransition> collection) {
		super();
		String writeToken, readToken, deleteToken, formula;
		writeToken = getTransitionsAccessingToken(collection, t, AccessMode.WRITE);
		readToken = getTransitionsAccessingToken(collection, t, AccessMode.READ);
		deleteToken = getTransitionsAccessingToken(collection, t, AccessMode.DELETE);
		
		formula = "((!" + writeToken + ") U (" + readToken + " | "
				 + deleteToken + ")) | (F(" + deleteToken  
				+ " & ((!" + writeToken + ") U (" + readToken +
				" | "+ deleteToken +"))))";
		setPattern(formula, true);
		mOperands.add(t);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

}
