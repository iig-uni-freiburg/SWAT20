package de.uni.freiburg.iig.telematik.swat.lukas;

public class Corequisite extends CompositePattern {

	public Corequisite(Operand t1, Operand t2) {
		super("(F" + t1.toString() + " & F" + t2.toString() + ") | "
				+ "(F(!" + t1.toString() + ") & F(!" + t2.toString() + "))");
		mOperands.add(t1);
		mOperands.add(t2);
	}

}
