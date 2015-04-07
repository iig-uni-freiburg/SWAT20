package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns.parameter;

public class ParameterValue {
	
	private String mType;
	private String mValue;

	public ParameterValue(String type, String value) {
		mType = type;
		mValue = value;
	}
	
	public String getType() {
		return mType;
	}
	
	public String getValue() {
		return mValue;
	}

	public void setValue(String value) {
		mValue = value;
	}
	
	public void setType(String type) {
		mType = type;
	}

}
