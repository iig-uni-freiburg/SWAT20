package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;
import java.util.Set;

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

	public int getMultiplicity() {
		return mMultiplicity;
	}

	public ArrayList<ParamValue> getValue() {
		return mValues;
	}

	public void setValue(ArrayList<ParamValue> values) {
		mValues = values;
	}
	
	public String getName() {
		return mName;
	}

	public String toString() {
		String s="";
		for(ParamValue v:mValues) {
			s+=v.toString();
		}
		return s;
	}
}
