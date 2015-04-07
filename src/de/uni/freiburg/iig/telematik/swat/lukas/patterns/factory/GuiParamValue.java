package de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory;

public class GuiParamValue {
	
	public void setOperandName(String mOperandName) {
		this.mOperandName = mOperandName;
	}

	private String mOperandName;
	private GuiParamType mOperandType;

	public GuiParamValue(String operandStr, GuiParamType type) {
		mOperandName = operandStr;
		mOperandType = type;
	}
	
	public String getOperandName() {
		return mOperandName;
	}
	
	public GuiParamType getOperandType() {
		return mOperandType;
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof GuiParamValue)) {
			return false;
		}
		GuiParamValue v=(GuiParamValue) o;
		//System.out.println("me: "+mOperandName);
		//System.out.println("he: "+v.getOperandName());
		return mOperandName.equals(v.getOperandName()) && mOperandType == v.getOperandType();
	}
	public String toString() {
		return mOperandName;
	}
}
