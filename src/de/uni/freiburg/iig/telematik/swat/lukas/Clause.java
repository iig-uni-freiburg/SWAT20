package de.uni.freiburg.iig.telematik.swat.lukas;

public class Clause extends Statepredicate {
	
	private Statepredicate mSp1;
	
	private Statepredicate mSp2;
	
	public Clause(Statepredicate sp1, Statepredicate sp2) {
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

}
