package de.uni.freiburg.iig.telematik.swat.lukas;

public class Transition extends NetElementExpression {
	
	private String mTransition;

	public Transition(String name) {
		mTransition = "(" + name + "_last=1)";
	}
	
	public String toString() {
		return mTransition;	
	}

}
