package de.uni.freiburg.iig.telematik.swat.lukas.patterns;

import java.util.Collection;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Token;

public class WeaklyLostData extends DataflowAntiPattern {
	
	public static final String NAME = "Weakly Lost Data D";
	
	public static final String DESC = "A data elemend D is written and never read afterwards.";
	
	public WeaklyLostData(Token t, Collection<RegularIFNetTransition> collection) {
		
		super();
		String writeToken, readToken, deleteToken, formula;
		
		// determine transitions which write, read or delete the token
		writeToken = getTransitionsAccessingToken(collection, t, AccessMode.WRITE);
		readToken = getTransitionsAccessingToken(collection, t, AccessMode.READ);
		deleteToken = getTransitionsAccessingToken(collection, t, AccessMode.DELETE);
		
		formula = "F(" + writeToken + " & (X(!(" + readToken+ " | " 
				+ deleteToken + ") U (" + writeToken +" & " + "!" + readToken + "))))";
		setPrismProperty(formula, true);
		mOperands.add(t);
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
