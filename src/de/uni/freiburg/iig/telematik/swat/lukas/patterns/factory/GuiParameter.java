package de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory;

import java.util.ArrayList;
import java.util.Set;



/**
 * A GuiParameter specifies the value and the type of pattern parameter which was entered in SWAT
 * GUI (patter dialog). Example: guiParameter={values={R1}, types={Role}, multiplicity=1}, a GuiParameter may be a 
 * list of activities, example: guiParameter={values={a1, a2}, types={Activity, Activity}, multiplicity=1}
 *
 * @author Lukas Sättler
 * @version 1.0
 */

public class GuiParameter {
	
	private Set<GuiParamType> mTypes;
	private int mMultiplicity;
	private ArrayList<GuiParamValue> mValues;
	private String mName;

	public GuiParameter(Set<GuiParamType> types, int mul, String name) {
		mTypes = types;
		mMultiplicity = mul;
		mName = name;
		mValues = new ArrayList<GuiParamValue>();
	}

	public Set<GuiParamType> getTypes() {
		return mTypes;
	}

	public int getMultiplicity() {
		return mMultiplicity;
	}

	public ArrayList<GuiParamValue> getValue() {
		return mValues;
	}

	public void setValue(ArrayList<GuiParamValue> values) {
		mValues = values;
	}
	
	public String getName() {
		return mName;
	}

	public String toString() {
		String s="";
		for(GuiParamValue v:mValues) {
			s+=v.toString();
		}
		return s;
	}
}
