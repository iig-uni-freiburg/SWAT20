package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class PrismResult implements Iterable<Map.Entry<CompliancePattern, PatternResult>> {

	HashMap<CompliancePattern, PatternResult> mResults;
	
	public PrismResult(ArrayList<CompliancePattern> patterns, String resultStr) {
		
		mResults = new HashMap<CompliancePattern, PatternResult>();
		
		ArrayList<String> resultStrs = getResultsStringForPattern(resultStr);

		for (int i = 0; i < patterns.size(); i++) {
			CompliancePattern cp = patterns.get(i);
			boolean isAntiPattern = cp.isAntiPattern();
			String resString = resultStrs.get(i);
			double probability = getProb(resString);
			boolean isFulfilled = isFulfilled(resString, isAntiPattern);
			PatternResult pr = new PatternResult(probability, isFulfilled);
			mResults.put(cp, pr);	
		}
		
	}

	public PrismResult(ArrayList<CompliancePattern> patterns, String resultStr,
			String states) {
		
		mResults = new HashMap<CompliancePattern, PatternResult>();
			
		ArrayList<String> resultStrs = getResultsStringForPattern(resultStr);
		
		for (int i=0, j=0; i<resultStrs.size(); i++, j++) {
			String res = resultStrs.get(i);
			if (res.contains("CTL")) {
				j--;
			}
			CompliancePattern cp = patterns.get(j);
			PatternResult pr = mResults.get(cp);
			if (pr == null) {
				double probability = getProb(res);
				boolean isFulfilled = isFulfilled(res, cp.isAntiPattern());
				pr = new PatternResult(probability, isFulfilled);
				mResults.put(cp, pr);
			} else {
				ArrayList<String> path = getViolatingPath(res, states);
				pr.setViolatingPath(path);
				mResults.put(cp, pr);
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
			if (variableNames[i].contains("_last")) {
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
			for (int i=0; i<variableValues.length; i++) {
				String transitionName = indexTransitionMap.get(i);
				if (transitionName != null && variableValues[i].equals("1")) {
					transitionPath.add(transitionName);
				}
			}
		}
		
		return transitionPath;
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
		return Double.parseDouble(prob);
	}
	
	/*public HashMap<CompliancePattern, PatternResult> getResults() {
		return mResults;
	}
	
	public HashMap<String, ArrayList<PatternResult>> getResults() {
		return mResults;
	}*/
	
	public PatternResult getPatternResult(CompliancePattern pattern) {
		
		return mResults.get(pattern);
		
	}

	@Override
	public Iterator<Entry<CompliancePattern, PatternResult>> iterator() {
		return mResults.entrySet().iterator();
	}
	
	public void addPatternResult(ResourcePattern p, PatternResult res) {
		mResults.put(p, res);
	}
	
	/*public static void main(String [ ] args) {
		
		CompliancePattern leadsTo = new LeadsTo(new Transition("t1"), new Transition("t2"));
		CompliancePattern leadsTo1 = new LeadsTo(new Transition("t2"), new Transition("t2"));
		ArrayList<CompliancePattern> cps = new ArrayList<CompliancePattern>();
		cps.add(leadsTo);
		cps.add(leadsTo1);
		String str = IOUtils.readFile(System.getProperty("user.dir") + File.separator + "results");
		PrismResult pr = new PrismResult(cps, str);
		PatternResult pRes = pr.getPatternResult(leadsTo.getName());
		System.out.println(pRes.getProbability());
		System.out.println(pRes.getViolatingPath(0));
		System.out.println(pRes.getViolatingPath(1));
		System.out.println(pRes.isFulfilled());
	}*/

}
