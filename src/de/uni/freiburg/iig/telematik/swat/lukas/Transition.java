package de.uni.freiburg.iig.telematik.swat.lukas;

public class Transition implements Operand {
	
	private String mTransitionName;

	public Transition(String name) {
		mTransitionName = "(" + name + "=1)";
	}
	
	public String toString() {
		return mTransitionName;
		
	}

}
