package de.uni.freiburg.iig.telematik.swat.lukas.operands;


public class Clause extends StateExpression {
	
	private StateExpression mSp1;
	
	private StateExpression mSp2;
	
	public Clause(StateExpression sp1, StateExpression sp2) {
		mSp1 = sp1;
		mSp2 = sp2;
	}
	
	@Override
	public String toString() {
		return "(" + mSp1.toString() +" & " + mSp2.toString() + ")";
	}

	@Override
	public String getNegation() {
		return mSp1.getNegation() + " | " + mSp2.getNegation();
	}

	@Override
	public String getName() {
		return null;
	}

}
