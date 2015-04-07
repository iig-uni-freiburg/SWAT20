package de.uni.freiburg.iig.telematik.swat.lukas.patterns;

import java.util.List;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.StatePredicate;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Relation;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Activity;

public class Causal extends PBNI {
	
	public static final String NAME = "Causal";
	public static final String DESC = "A Low-User can Deduce that a High-Aktivity was performed.";

	public Causal(Activity highTransition, IFNetPlace p, Activity lowTransition) {
		super();
		String prismExpr = "F((" + highTransition.toString() + ") & (X(F(" + exprHelper(p) + " U " + lowTransition.toString() + "))))";
		setPrismProperty(prismExpr, true);
		mOperands.add(lowTransition);
		mOperands.add(highTransition);
		mOperands.add(new StatePredicate(p.getName(), Relation.EQUALS, 1));
	}
	
	private String exprHelper(IFNetPlace p) {
		
		List<IFNetFlowRelation> flowRelations = p.getIncomingRelations();
		String expr = "";
		for (IFNetFlowRelation flowRel : flowRelations) {
			AbstractIFNetTransition<IFNetFlowRelation> t = flowRel.getTransition();
			expr += new Activity(t.getName()).getNegation() + " & "; 
		}
		expr = expr.substring(0, expr.length() - 3);
		return "(" + expr + ")";
		
	}

	@Override
	public boolean isAntiPattern() {
		return true;
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
