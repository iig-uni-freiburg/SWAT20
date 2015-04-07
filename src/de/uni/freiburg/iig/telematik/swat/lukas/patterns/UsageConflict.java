package de.uni.freiburg.iig.telematik.swat.lukas.patterns;

import java.util.List;
import java.util.Set;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.StatePredicate;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Relation;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Activity;

public class UsageConflict extends PBNI {

	public static final String NAME = "Usage Conflict";
	public static final String DESC = "";

	public UsageConflict(AbstractIFNetTransition<IFNetFlowRelation> highTrans, AbstractIFNetTransition<IFNetFlowRelation> lowTrans, 
			IFNetPlace p, Multiset<String> commonConsumedColors) {
		super();
		String marking = createMarkingExpr(highTrans);
		String transitions = createTransitionExpr(p, commonConsumedColors);
		String constraintExpr = createConstraint(p, highTrans, lowTrans, commonConsumedColors);
		String prismExpr = "F(" + marking + " & (" + constraintExpr + ") & ((" + transitions + ") U " + new Activity(lowTrans.getName()).toString() + "))";
		setPrismProperty(prismExpr, true);
		mOperands.add(new Activity(lowTrans.getName()));
		mOperands.add(new Activity(highTrans.getName()));
		mOperands.add(new StatePredicate(p.getName(), Relation.EQUALS, 1));
	}

	private String createConstraint(
			IFNetPlace p, 
			AbstractIFNetTransition<IFNetFlowRelation> highTrans,
			AbstractIFNetTransition<IFNetFlowRelation> lowTrans, Multiset<String> commonConsumedColors) {
		
		String expr = "";
		Set<String> colors = commonConsumedColors.support();
		for (String color : colors) {
			int const1 = highTrans.getConsumedTokens(color);
			int const2 = lowTrans.getConsumedTokens(color);
			expr += "(" + p.getName() + "_" + color + "<" + (const1 + const2) + ") & ";
		}
		expr = expr.substring(0, expr.length() - 3);
		return expr;
	}

	private String createTransitionExpr(IFNetPlace p, Multiset<String> commonConsumedColors) {
		String expr = "";
		List<IFNetFlowRelation> incommingRels = p.getIncomingRelations();
		for (IFNetFlowRelation rel : incommingRels) {
			AbstractIFNetTransition<IFNetFlowRelation> transition = rel.getTransition();
			Set<String> colors = commonConsumedColors.support();
			boolean illegalTransition = false;
			for (String color : colors) {
				illegalTransition = transition.producesColor(color);	
			}
			expr += new Activity(transition.getName()).getNegation() + " & ";
			
		}
		expr = expr.substring(0, expr.length() - 3);
		return expr;
	}

	private String createMarkingExpr(AbstractIFNetTransition<IFNetFlowRelation> highTrans) {
		String expr = "";
		List<IFNetFlowRelation> incommingRels = highTrans.getIncomingRelations();
		for (IFNetFlowRelation rel : incommingRels) {
			Multiset<String> constraints = rel.getConstraint();
			IFNetPlace inputPlace = rel.getPlace();
			Set<String> colors = inputPlace.getColorsWithCapacityRestriction();
			String pName = rel.getPlace().getName();
			for (String color : colors) {
				int mul = constraints.multiplicity(color);
				expr += "(" + pName +"_" + color + ">=" + mul + ") & ";
			}
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
