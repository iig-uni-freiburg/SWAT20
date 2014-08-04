package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public abstract class ResourcePattern extends CompliancePattern {
	
	private static HashMap<String, String> mPatternDescription;

	public ResourcePattern() {
		super(false);
	}

	public static final String NAME = "Resource Pattern";
	
	public static ArrayList<String> getPatternNames() {
		
		return new ArrayList<String>(Arrays.asList(PerformedBy.NAME, SegregatedFrom.NAME,
				USegregatedFrom.NAME, BoundedWith.NAME, MSegregated.NAME, RBoundedWith.NAME));
		
	}
	
	 public static HashMap<String, String> getPatternDescription() {
			
			if (mPatternDescription == null) {
				mPatternDescription = new HashMap<String, String>(); 
				mPatternDescription.put(MissingData.NAME, MissingData.DESC);
				mPatternDescription.put(WeaklyRedData.NAME, WeaklyRedData.DESC);
				mPatternDescription.put(WeaklyLostData.NAME, WeaklyLostData.DESC);
				mPatternDescription.put(InconsistentData.NAME, InconsistentData.DESC);
				mPatternDescription.put(TwiceDestroyed.NAME, TwiceDestroyed.DESC);
				mPatternDescription.put(NotDeletedOnTime.NAME, NotDeletedOnTime.DESC); 
			}
			
			return mPatternDescription;
	}
	
	@Override
	public boolean isAntiPattern() {
		return false;
	}

}
