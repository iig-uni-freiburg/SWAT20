package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class ResourcePattern extends CompliancePattern {
	
	public ResourcePattern(String text) {
		super(text);
	}

	public static final String NAME = "Resource Pattern";
	
	public static ArrayList<String> getPatternNames() {
		
		return new ArrayList<String>(Arrays.asList(PerformedBy.NAME, SegregatedFrom.NAME,
				USegregatedFrom.NAME, BoundedWith.NAME, MSegregated.NAME, RBoundedWith.NAME));
		
	}
	
	@Override
	public boolean isAntiPattern() {
		return false;
	}

}
