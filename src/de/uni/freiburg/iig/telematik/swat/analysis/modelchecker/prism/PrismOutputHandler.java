package de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.prism;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;

public class PrismOutputHandler {

	HashSet<CompliancePattern> mEvaluatedPatterns;
	
	public PrismOutputHandler(ArrayList<CompliancePattern> patterns, String resultStr) {
		
		ArrayList<String> resultStrs = getResultsStringForPattern(resultStr);
		
		ArrayList<CompliancePattern> instantiatedPatterns = new ArrayList<CompliancePattern>(); 
		for (CompliancePattern p : patterns) {
			if (p.isInstantiated()) {
				instantiatedPatterns.add(p);
			}
		}

		for (int i = 0, ruleStringIndex = 0; i < instantiatedPatterns.size(); i++) {
			String resString="";
			CompliancePattern cp = instantiatedPatterns.get(i);
			boolean isAntiPattern = cp.isAntiPattern();
			while ((resString = resultStrs.get(ruleStringIndex)).contains("CTL"))
					ruleStringIndex++; //skip double evaluated rules (Exists P, Absent P...)
			//String resString = resultStrs.get(ruleStringIndex);
			double probability = getProb(resString);
			boolean isFulfilled = isFulfilled(resString, isAntiPattern);
			cp.setProbability(probability);
			cp.setSatisfied(isFulfilled);
			ruleStringIndex++;
		}
		
	}

	public PrismOutputHandler(ArrayList<CompliancePattern> patterns, String resultStr,
			String states) {
		
		mEvaluatedPatterns = new HashSet<CompliancePattern>();
		
		ArrayList<CompliancePattern> instantiatedPatterns = new ArrayList<CompliancePattern>(); 
		for (CompliancePattern p : patterns) {
			if (p.isInstantiated()) {
				instantiatedPatterns.add(p);
			}
		}
			
		ArrayList<String> resultStrs = getResultsStringForPattern(resultStr);
		
		for (int i=0, j=0; i<resultStrs.size(); i++, j++) {
			String res = resultStrs.get(i);
			if (res.contains("CTL")) {
				j--;
			}
			CompliancePattern cp = instantiatedPatterns.get(j);
			if (!mEvaluatedPatterns.contains(cp)) {
				double probability = getProb(res);
				boolean isFulfilled = isFulfilled(res, cp.isAntiPattern());
				cp.setProbability(probability);
				cp.setSatisfied(isFulfilled);
                                cp.setCounterExample(getViolatingPath(res, states));
				mEvaluatedPatterns.add(cp);
			} else {
				ArrayList<String> path = getViolatingPath(res, states);
				if(path!=null && !path.isEmpty())
					cp.setCounterExample(path);
			}
		}
	}

	private ArrayList<String> getViolatingPath(String resString, String states) {
		
		if (!resString.contains("Counterexample")) {
			return null;
		}
		
		ArrayList<String> transitionPath = new ArrayList<String>();
		HashMap<Integer, String> indexTransitionMap = new HashMap<Integer, String>(); 
		int index = states.indexOf(")");
		String variablesStr = states.substring(0, index);
		variablesStr = variablesStr.substring(1, variablesStr.length());
		String[] variableNames = variablesStr.split(",");
		
		for (int i=0; i<variableNames.length; i++) {
//			if (variableNames[i].contains("_last")) { //original lukas
			if (variableNames[i].contains("fired")) { //will also do
				indexTransitionMap.put(i, variableNames[i]);
			}
		}
		
		index = resString.indexOf("Counterexample");
		int endIndex = resString.lastIndexOf(")");
		resString = resString.substring(index, endIndex + 1);
		index = resString.indexOf("(");
		endIndex = resString.lastIndexOf(")");
		resString = resString.substring(index, endIndex + 1);
		
		String[] nodes = resString.split("\n");
		
		for (int j=0; j<nodes.length; j++) {
			String node = nodes[j];
			node = node.substring(1, node.length() - 1);
			String[] variableValues = node.split(",");
			transitionPath.add(getLastWitnessLine(variableValues));
			for (int i=0; i<variableValues.length; i++) {
				String transitionName = indexTransitionMap.get(i);
				if (transitionName != null && variableValues[i].equals("1")) {
					//transitionPath.add(transitionName);
				}
			}
		}
		//return first null-Entry
		transitionPath.remove(0);
		return transitionPath;
	}
	
	private String getLastWitnessLine(String[] variableValues){
		return TransitionToIDMapper.getTransitionNameFromID(Integer.parseInt(variableValues[variableValues.length-1]));
	}

	private ArrayList<String> getResultsStringForPattern(String resultStr) {
		String[] strs = resultStr.split("---------------------------------------------------------------------");
		ArrayList<String> resultStrs = new ArrayList<String>();
		
		for (String str : strs) {
			if(str.contains("Model checking:")) {
				resultStrs.add(str);
			}
		}
		return resultStrs;
	}


	private boolean isFulfilled(String resString, boolean isAntiPattern) {
		
		if (isAntiPattern) {
			return !resString.contains("Result: 0.0");
		} else {
			return resString.contains("Result: 1.0");
		}

	}
	

	private double getProb(String resString) {
		
		int i = resString.indexOf("Result:");
		String prob = "";
		for (int j= i + 8; j < resString.length(); j++) {
			String c = resString.substring(j, j + 1);
			if (c.matches("\\d|\\.")) {
				prob += resString.charAt(j);
			} else {
				break;
			}
		}
		try{
		return Double.parseDouble(prob);
		} catch (NumberFormatException e){
			return Double.NaN;
		}
	}

}
