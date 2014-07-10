package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * A Class representing every pattern that can be checked. Patterns are uniquely
 * identified by their name eg Q preceds P
 * @author bernhard
 *
 */
public class PatternDatabase  {
	private static PatternDatabase instance;
	private Map<String, Pattern> patternDB=null;
	/**
	 * return the ParameterList for a specific pattern
	 * @param patternName
	 * @return
	 */
	public static PatternDatabase getInstance() {
		if(instance == null) {
			instance=new PatternDatabase();
		}
		return instance;
	}
	public Pattern getPattern(String patternName) {
		return patternDB.get(patternName);
	}
	public List<String> getPatternList() {
		return new ArrayList<String>( patternDB.keySet());
	}

	/**
	 * return the patternList for a given pt net
	 * @param netType
	 * @return
	 */
	public List<String> getPatternListForNetType(String netType) {
		return new ArrayList<String>( patternDB.keySet());
	}
	public PatternDatabase() {
		init();
	}
	/**
	 * initialize the pattern Database
	 */
	private void init() {
		// TODO Auto-generated method stub
		patternDB=new HashMap<String, Pattern>();
		// dataflow patterns
		patternDB.put("Missing Data", new Pattern("Missing Data", Arrays.asList(new PatternParameter("Data","data",""))));
		patternDB.put("Strongly Redundant Data", new Pattern("Strongly Redundant Data", Arrays.asList(new PatternParameter("Data","data",""))));
		patternDB.put("Weakly Redundant Data", new Pattern("Weakly Redundant Data", Arrays.asList(new PatternParameter("Data","data",""))));
		patternDB.put("Strongly Lost Data", new Pattern("Strongly Lost Data", Arrays.asList(new PatternParameter("Data","data",""))));
		patternDB.put("Weakly Lost Data", new Pattern("Weakly Lost Data", Arrays.asList(new PatternParameter("Data","data",""))));
		patternDB.put("Inconsistent Data", new Pattern("Inconsistent Data", Arrays.asList(new PatternParameter("Data","data",""))));
		patternDB.put("Never Destroyed", new Pattern("Never Destroyed Data", Arrays.asList(new PatternParameter("Data","data",""))));
		patternDB.put("Twice Destroyed", new Pattern("Twice Destroyed Data", Arrays.asList(new PatternParameter("Data","data",""))));
		patternDB.put("Not Deleted on Time", new Pattern("Not Deleted on Time Data", Arrays.asList(new PatternParameter("Data","data",""))));
		
		// control flow patterns
		patternDB.put("Q Precedes P", new Pattern("Q Precedes P", Arrays.asList(new PatternParameter("Q","activity",""), new PatternParameter("P","activity",""))));
		patternDB.put("P LeadsTo Q", new Pattern("P LeadsTo Q", Arrays.asList(new PatternParameter("P","activity",""), new PatternParameter("Q","activity",""))));
		patternDB.put("P XleadsTo Q", new Pattern("P XleadsTo Q", Arrays.asList(new PatternParameter("P","activity",""), new PatternParameter("Q","activity",""))));
		patternDB.put("P PleadsTo Q", new Pattern("P PleadsTo Q", Arrays.asList(new PatternParameter("P","activity",""), new PatternParameter("Q","activity",""))));
	}
}
