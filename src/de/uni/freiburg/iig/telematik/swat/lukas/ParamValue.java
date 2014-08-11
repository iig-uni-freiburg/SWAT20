package de.uni.freiburg.iig.telematik.swat.lukas;

public class ParamValue {
	
	public void setOperandName(String mOperandName) {
		this.mOperandName = mOperandName;
	}

	private String mOperandName;
	private OperandType mOperandType;

	public ParamValue(String operandStr, OperandType type) {
		mOperandName = operandStr;
		mOperandType = type;
	}
	
	public String getOperandName() {
		return mOperandName;
	}
	
	public OperandType getOperandType() {
		return mOperandType;
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof ParamValue)) {
			return false;
		}
		ParamValue v=(ParamValue) o;
		System.out.println("me: "+mOperandName);
		System.out.println("he: "+v.getOperandName());
		return mOperandName.equals(v.getOperandName()) && mOperandType == v.getOperandType();
	}
}
