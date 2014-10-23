package de.uni.freiburg.iig.telematik.swat.lukas.operands;

public class Token extends Operand {

	private String mColor;

	public Token(String color) {
		mColor = color;
	}
	
	public String toString() {
		return mColor;
	}

	@Override
	public String getName() {
		return mColor;
	}
}
