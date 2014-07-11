package de.uni.freiburg.iig.telematik.swat.lukas;

public class Token extends Operand {

	private String mColor;

	public Token(String color) {
		mColor = color;
	}
	
	public String toString() {
		return mColor;
	}
}
