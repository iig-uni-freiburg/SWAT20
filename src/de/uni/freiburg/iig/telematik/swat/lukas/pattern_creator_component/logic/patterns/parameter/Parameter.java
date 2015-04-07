package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns.parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Parameter {

	private String mName;
	private HashMap<String, ArrayList<String>> mRangeMap;
	private ParameterValue mValue;
	
	/**
	 * Constructs a Parameter with a name and the parameter types.
	 *
	 * @param types the types of the parameter 
	 * @param name the name of the parameter
	 */
	public Parameter(ArrayList<String> types, String name) {
		mName = name;
		mRangeMap = new HashMap<String, ArrayList<String>>();
		for (String type : types) {
			mRangeMap.put(type, new ArrayList<String>());
		}
		mValue = new ParameterValue(types.get(0), "");
	}
	
	public String getName() {
		return mName;
	}
	
	public void setTypeRange(String type, ArrayList<String> range) {
	    
		Set<String> keys = mRangeMap.keySet();
		if (!keys.contains(type)) {
	    	try {
				throw new InvalidTypeException(type + "is not a valid type for parameter " + getName());
			} catch (InvalidTypeException e) {
				e.printStackTrace();
			}
	    }
		mRangeMap.put(type, range);
	}
	
	public void setValue(String type, String value) {
		mValue = new ParameterValue(type, value);
	}
	
	public ParameterValue getValue() {
		return mValue;
	}
	
	public ArrayList<String> getParameterRange(String type) {
		return mRangeMap.get(type);
	}

	
	public ArrayList<String> getParameterDomain() {
		return new ArrayList<String>(mRangeMap.keySet());
	}

	public void setValue(String parameterTypeStr) {
		ArrayList<String> range = mRangeMap.get(parameterTypeStr);
		mValue.setValue((range.size() > 0)? range.get(0) : "-");
		mValue.setType(parameterTypeStr);
		
	}
	
}
