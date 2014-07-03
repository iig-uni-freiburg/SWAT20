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
	static Map<String, List<String>> patternDB=null;
	/**
	 * return the ParameterList for a specific pattern
	 * @param patternName
	 * @return
	 */
	public static List<String> getParameterForPattern(String patternName) {
		if (patternDB == null) {
			init();
		}
		return patternDB.get(patternName);
	}
	public static List<String> getPatternList() {
		if (patternDB == null) {
			init();
		}
		return new ArrayList<String>( patternDB.keySet());
	}

	/**
	 * return the patternList for a given pt net
	 * @param netType
	 * @return
	 */
	public static List<String> getPatternListForNetType(String netType) {
		if (patternDB == null) {
			init();
		}
		return new ArrayList<String>( patternDB.keySet());
	}
	/**
	 * initialize the pattern Database
	 */
	private static void init() {
		// TODO Auto-generated method stub
		patternDB=new HashMap<String, List<String>>();
		// dataflow patterns
		patternDB.put("Missing Data", Arrays.asList( "Data" ));
		patternDB.put("Strongly Redundant Data", Arrays.asList( "Data" ));
		patternDB.put("Weakly Redundant Data", Arrays.asList( "Data" ));
		patternDB.put("Strongly Lost Data", Arrays.asList( "Data" ));
		patternDB.put("Weakly Lost Data", Arrays.asList( "Data" ));
		patternDB.put("Inconsistent Data", Arrays.asList( "Data" ));
		patternDB.put("Never Destroyed", Arrays.asList( "Data" ));
		patternDB.put("Twice Destroyed", Arrays.asList( "Data" ));
		patternDB.put("Not Deleted on Time", Arrays.asList( "Data" ));
		
		// control flow patterns
		patternDB.put("Q Precedes P", Arrays.asList( "Activity", "Activity" ));
		patternDB.put("P LeadsTo Q", Arrays.asList( "Activity", "Activity"));
		patternDB.put("P XleadsTo Q", Arrays.asList( "Activity", "Activity"));
		patternDB.put("P PleadsTo Q", Arrays.asList( "Activity", "Activity"));
	}
}
