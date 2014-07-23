package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;
import java.util.Set;

import de.uni.freiburg.iig.telematik.swat.bernhard.Helpers;

public class Parameter {
	
	private Set<OperandType> mTypes;
	private int mMultiplicity;
	private ArrayList<ParamValue> mValues;
	private String mName;

	public Parameter(Set<OperandType> types, int mul, String name) {
		mTypes = types;
		mMultiplicity = mul;
		mName = name;
		mValues = new ArrayList<ParamValue>();
	}

	public Set<OperandType> getTypes() {
		return mTypes;
	}
	
	public String getValueS() {
		return Helpers.getFirst(mValues).getOperandName();
	}

	public int getMultiplicity() {
		return mMultiplicity;
	}

	public ArrayList<ParamValue> getValue() {
		return mValues;
	}

	public void setValue(ParamValue value) {
		mValues.clear();
		mValues.add(value);
	}
	
	public void setValue(ArrayList<ParamValue> values) {
		mValues = values;
	}
	
	public String getName() {
		return mName;
	} 

}
