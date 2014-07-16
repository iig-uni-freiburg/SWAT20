package de.uni.freiburg.iig.telematik.swat.lukas;

public class ChainLeadsTo extends AtomicPattern {
	
	public static final String NAME = "Chain Leads To";
	
	public ChainLeadsTo(NetElementExpression op1, NetElementExpression op2, NetElementExpression op3, boolean sequencePrecedes) {
		
		mOperands.add(op1);
		mOperands.add(op2);
		mOperands.add(op3);
		String formula = "";
		if (sequencePrecedes) {
			// property had to be rewritten compared to the paper, 
			// because prism crashed using original formlistion
			// property: G((S and XF(T)) -> X(F(T and F(P))))
			// property: G((S and XF(T)) -> X(F(T and P)) | X(F(P)))
			// P>=1 [G(((td_last=1) & (X(F(t1_last=1)))) => (X((F((t1_last=1) & (t1_last=1))) | (F((t1_last=1))))))]
			formula = "G((" + op1.toString() + " & (X(F" + op2.toString() + "))) => "
					+ "((X(F(" + op2.toString() + " & " + op3.toString()
							+ "))) | (X(F("
							+ op3.toString()
							+ ")))))";
		} else {
			formula = "G(" + op1.toString() + " => (F(" + op2.toString() + " & "
					+ "(X(F" + op3.toString() + ")))))";
		}
		setPattern(formula, false);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

}

