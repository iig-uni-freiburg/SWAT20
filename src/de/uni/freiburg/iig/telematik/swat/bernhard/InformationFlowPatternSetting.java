package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.ArrayList;
import java.util.List;

import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.PatternResult;


public class InformationFlowPatternSetting extends PatternSetting {

	public InformationFlowPatternSetting(String name, List<GuiParameter> parameters) {
		super(name, parameters);
		mResults = new ArrayList<PatternResult>();
	}
	
	private ArrayList<PatternResult> mResults;
	
	@Override
	public PatternResult getResult() {
		return result;
	}
	
	public ArrayList<PatternResult> getResults() {
		return mResults;
	}

	@Override
	public void setResult(PatternResult nextResult) {
		
		if (nextResult == null) {
			result = null;
		} else {
			mResults.add(nextResult);
			if (result == null) {
				result = ((PRISMPatternResult) nextResult).clone();
			} else {
				double prob = ((PRISMPatternResult) result).getProbability();
				boolean satisfied = result.isFulfilled();
				prob = (prob < ((PRISMPatternResult) nextResult).getProbability())? ((PRISMPatternResult) nextResult).getProbability() : prob;
				satisfied = satisfied || nextResult.isFulfilled();
				((PRISMPatternResult) result).setFulfilled(satisfied);
				((PRISMPatternResult) result).setProbability(prob);
			}
		}
	}
	
	@Override
	public void reset() {
		result = null;
		mResults = new ArrayList<PatternResult>();
		
	}
	
}
