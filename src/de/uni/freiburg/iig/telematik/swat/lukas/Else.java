package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.List;

public class Else extends AtomicPattern {
	
	public static final String NAME = "P LeadsTo Q Else R Else ...";
	public static final String DESC = "IF P occurs, Q should occur else R and so on.";

	public Else(List<NetElementExpression> operands) {
		super();
		
		mOperands.addAll(operands);
		
		if (operands.size() == 2) {
		
			LeadsTo p = new LeadsTo(operands.get(0), operands.get(1));
			setPrismProperty(p.getPrismLTLProperty(), false);
			
		} else if (operands.size() > 2) {
			
			String formula = "G(" + operands.get(0) + " => " + getImplication(operands) + ")";
			setPrismProperty(formula, false);
			
		}
		
	}

	private String getImplication(List<NetElementExpression> operands) {
		
		String implication = "(F " + operands.get(1) + ")";
		String excludedOccurences = "(G " + operands.get(1).getNegation() + ")";
		
		for (int i = 2; i < operands.size(); i++) {
			implication += " | " + "(" + excludedOccurences + " & (F" + operands.get(i) + "))";
			excludedOccurences += "& (G" + operands.get(i).getNegation() + ")";
		}
		
		return "(" + implication + ")";
		
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESC;
	}
	
}
