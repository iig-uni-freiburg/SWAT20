package de.uni.freiburg.iig.telematik.swat.lukas.patterns;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractRegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Token;

public abstract class DataflowPattern extends CompliancePattern {
	
	public static final String NAME = "Dataflow Pattern";
	
	private static HashMap<String, String> mPatternDescription;
	
	public DataflowPattern() {
		super(true);
	}
	
	public DataflowPattern(String prismLTLProp, String text) {
		super(prismLTLProp, true);
	}
	
	protected String getTransitionsAccessingToken(Collection<RegularIFNetTransition> ts,
			Token token, AccessMode am) {
		
		String accessingTransitions = "";
		int tokenAccessNum = 0;
		for (AbstractRegularIFNetTransition<IFNetFlowRelation> t : ts) {
			Map<String, Set<AccessMode>> amPerTokenColor = t.getAccessModes();
			Set<AccessMode> ams = amPerTokenColor.get(token.toString());
			if (ams != null && ams.contains(am)) {
				tokenAccessNum++;
				accessingTransitions += "(" + t.getName() + "_last=1) | "; 
			}
		}
		
		if (!accessingTransitions.equals("")) {
			accessingTransitions = accessingTransitions.substring(0, accessingTransitions.length() - 3);
			if (tokenAccessNum > 1) {
				accessingTransitions = "(" + accessingTransitions + ")"; 	
			}
			return accessingTransitions;
		} else {
			return "false";
		}
	}
	
	protected String getTerminationCond(Collection<IFNetPlace> outputPlaces) {
		
		String expression = "";
		for (IFNetPlace outputPlace : outputPlaces) {
			expression += "(" + outputPlace.getName() + "_black=1) | "; 
		}
		expression = expression.substring(0, expression.length() - 3);
		if (outputPlaces.size() > 1) {
			expression = "(" + expression + ")";
		}
		
		return expression;
		
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
		return true;
	}

}
