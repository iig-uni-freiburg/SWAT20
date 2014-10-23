package de.uni.freiburg.iig.telematik.swat.lukas.operands;

public class Transition extends NetElementExpression {
	
	private String mTransition;
	private String mName;

	public Transition(String name) {
		//mTransition = "(" + name + "_last=1)";
		mTransition = "(" + name + "=1)";
		mName = name;
	}
	
	public String toString() {
		return mTransition;	
	}
	
	@Override
	public String getNegation() {
		return "(" + mName + "_last=0" + ")";
	}

	@Override
	public String getName() {
		return mName;
	}

}
