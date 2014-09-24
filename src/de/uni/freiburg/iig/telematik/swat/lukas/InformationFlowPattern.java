package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;
import java.util.HashMap;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;

public abstract class InformationFlowPattern extends CompliancePattern {

	protected ArrayList<RegularIFNetTransition> mPatternTransitions;
	
	private static HashMap<String, String> mPatternDescription;
	
	public InformationFlowPattern(String ltlFormula) {
		super(ltlFormula, true);
		mPatternTransitions = new ArrayList<RegularIFNetTransition>();
	}
	
	public InformationFlowPattern() {
		super(true);
		mPatternTransitions = new ArrayList<RegularIFNetTransition>();
	}
	
	public ArrayList<RegularIFNetTransition> getPatternTransitions() {
		return mPatternTransitions;
	}
	
	 public static HashMap<String, String> getPatternDescription() {
			
			if (mPatternDescription == null) {
				mPatternDescription = new HashMap<String, String>();
				mPatternDescription.put(ReadUp.NAME, ReadUp.DESC);
				mPatternDescription.put(WriteDown.NAME, WriteDown.DESC);
				mPatternDescription.put(PBNI.NAME, PBNI.DESC); 
			}
			
			return mPatternDescription;
		}

}
