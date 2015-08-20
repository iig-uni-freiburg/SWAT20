package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

public class Parameter {

	private final String mName;
	private final HashMap<String, ArrayList<String>> mRangeMap;
	private ParameterValue mValue;
	private String[] mTimePoints={"start","end","complete"};
	private String mSelectedTimePoint="complete";
	
	/**
	 * Constructs a Parameter with a name and the parameter types.
	 *
	 * @param types the types of the parameter 
	 * @param name the name of the parameter
	 */
	public Parameter(ArrayList<String> types, String name) {
		mName = name;
		mRangeMap = new HashMap<>();
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
				throw new RuntimeException(e);
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
		ArrayList<String> result = new ArrayList<>(mRangeMap.get(type));
		result.add(0, ""); //Because first, pattern parameter are not initialized
		return result;
		//return mRangeMap.get(type); //if initialized
	}
	
	public String[] getTimePoints(){
		return mTimePoints;
	}
	
	public void setTimePoints(String[] possibleTimePoints){
		mTimePoints=possibleTimePoints;
	}
	
	public void setSelectedTimePoint(String timePoint){
		boolean contains = false;
		for(String s:mTimePoints){
			if(s.equalsIgnoreCase(timePoint)){ contains=true;break;}}
		
		if (!contains)
			throw new RuntimeException("TimePoint "+timePoint+" is not available in process log!");
		
		mSelectedTimePoint=timePoint;
	}
	
	public String getSelectedTimePoint(){
		return mSelectedTimePoint;
	}

	
	public ArrayList<String> getParameterDomain() {
		return new ArrayList<>(mRangeMap.keySet());
	}

	public void setValue(String parameterTypeStr) {
		ArrayList<String> range = mRangeMap.get(parameterTypeStr);
		if (!parameterTypeStr.equals(mValue.getType())) //this if statement is the reason for new patterns to have empty parameters, however it fixes the load pattern problem
			mValue.setValue((range.size() > 0) ? range.get(0) : "-");
		mValue.setType(parameterTypeStr);
		
	}
	
}
