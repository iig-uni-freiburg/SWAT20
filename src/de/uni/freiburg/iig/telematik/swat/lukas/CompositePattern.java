package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class CompositePattern extends CompliancePattern {
	
	public static final String NAME = "Composite Patterns";
	
	public CompositePattern(String formula, String text) {
		super(formula, false, text);
	}
	

	public static ArrayList<String> getPatternNames() {
		
		return new ArrayList<String>(Arrays.asList(CoExists.NAME,
				CoAbsent.NAME, Exclusive.NAME, Corequisite.NAME, MutexChoice.NAME));
		
	}
	
	@Override
	public boolean isAntiPattern() {
		return false;
	}

}
