package de.uni.freiburg.iig.telematik.swat.lukas;

public class Exclusive extends CompositePattern {

	public Exclusive(Operand op1, Operand op2) {
		super("(!(F" + op1.toString() + ") | (F(!" + op2.toString() 
				+ "))) & (!(F"  + op2.toString() + ") | (F(!" 
				+ op1.toString() + ")))");
		mOperands.add(op1);
		mOperands.add(op2);
	}

}
