package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AtomicPattern extends CompliancePattern {
	
	public static final String NAME = "Atomic Pattern";

	public AtomicPattern(String formula) {
		super(formula, false);
	}
	
	public AtomicPattern(){}
	
	public static ArrayList<String> getPatternNames() {
		
		return new ArrayList<String>(Arrays.asList(Precedes.NAME, ChainPrecedes.NAME,
				LeadsTo.NAME, ChainLeadsTo.NAME, XLeadsTo.NAME, Else.NAME, Absent.NAME, Universal.NAME, Exists.NAME));
		
	}
	
}
