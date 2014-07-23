package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AtomicPattern extends CompliancePattern {
	
	public static final String NAME = "Atomic Pattern";

	public AtomicPattern(String formula, String text) {
		super(formula, false, text);
	}
	
	public static ArrayList<String> getPatternNames() {
		
		return new ArrayList<String>(Arrays.asList(Precedes.NAME, ChainPrecedes.NAME, PrecedesChain.NAME,
				LeadsTo.NAME, ChainLeadsTo.NAME, LeadsToChain.NAME, 
				XLeadsTo.NAME, Else.NAME, Absent.NAME, Universal.NAME, Exists.NAME));
		
	}
	
	@Override
	public boolean isAntiPattern() {
		return false;
	}
	
}
