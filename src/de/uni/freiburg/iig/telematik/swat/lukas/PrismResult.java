package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PrismResult {

	HashMap<String, PatternResult> mResults;
	
	public PrismResult(ArrayList<CompliancePattern> patterns, String resultStr) {
		mResults = new HashMap<String, PatternResult>();
		fillResultMap(patterns, resultStr);
	}

	private void fillResultMap(ArrayList<CompliancePattern> patterns, String resultStr) {
		
		String[] strs = resultStr.split("---------------------------------------------------------------------");
		ArrayList<String> resultStrs = new ArrayList<String>();
		
		for (String str : strs) {
			if(str.contains("Model checking:")) {
				resultStrs.add(str);
			}
		} 
		
		for (int i = 0; i < patterns.size(); i++) {
			CompliancePattern cp = patterns.get(i);
			String name = cp.getName();
			boolean isAntiPattern = cp.isAntiPattern();
			String resString = resultStrs.get(i);
			double probability = getProb(resString);
			boolean isFulfilled = isFulfilled(resString, isAntiPattern);
			ArrayList<ArrayList<String>> violatingPaths = getViolatingExamplePaths();
			PatternResult pr = new PatternResult(probability, isFulfilled, violatingPaths);
			mResults.put(name, pr);
		}
		
		
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

	private ArrayList<ArrayList<String>> getViolatingExamplePaths() {
		
		ArrayList<ArrayList<String>> paths = new ArrayList<ArrayList<String>>();
		paths.add(new ArrayList<String>(Arrays.asList("t1", "t2", "t3", "t4", "t5")));
		paths.add(new ArrayList<String>(Arrays.asList("t1", "t2", "t7")));
		return paths;
	}
	
	public HashMap<String, PatternResult> getResults() {
		return mResults;
	}
	
	public PatternResult getPatternResult(String patternName) {
		return mResults.get(patternName);
	}
	
	/*public static void main(String [ ] args) {
		
		CompliancePattern leadsTo = new LeadsTo(new Transition("t1"), new Transition("t2"));
		ArrayList<CompliancePattern> cps = new ArrayList<CompliancePattern>();
		cps.add(leadsTo);
		String str = IOUtils.readFile(System.getProperty("user.dir") + File.separator + "results", Charset.defaultCharset());
		PrismResult pr = new PrismResult(cps, str);
		PatternResult pRes = pr.getPatternResult(leadsTo.getName());
		System.out.println(pRes.getProbability());
		System.out.println(pRes.getViolatingPath(0));
		System.out.println(pRes.getViolatingPath(1));
		System.out.println(pRes.isFulfilled());
	}*/

}
