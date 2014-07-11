package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;
import java.util.Set;

public class Parameter {
	
	private Set<OperandType> mTypes;
	private int mMultiplicity;
	private ArrayList<ParamValue> mValues;

	public Parameter(Set<OperandType> types, int mul) {
		mTypes = types;
		mMultiplicity = mul;
	}

	public Set<OperandType> getTypes() {
		return mTypes;
	}

	public int getMultiplicity() {
		return mMultiplicity;
	}

	public ArrayList<ParamValue> getValue() {
		return mValues;
	}

	public void setValue(ParamValue value) {
		mValues.add(value);
	}
	
	public void setValue(ArrayList<ParamValue> values) {
		mValues = values;
	}

}
