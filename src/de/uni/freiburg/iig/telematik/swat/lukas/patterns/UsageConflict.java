package de.uni.freiburg.iig.telematik.swat.lukas.patterns;

import java.util.List;
import java.util.Set;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.PlacePredicate;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Relation;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Transition;

public class UsageConflict extends PBNI {

	public static final String NAME = "Usage Conflict";
	public static final String DESC = "";

	public UsageConflict(AbstractIFNetTransition<IFNetFlowRelation> highTrans, Transition lowTrans, IFNetPlace p) {
		super();
		String marking = createMarkingExpr(highTrans);
		String prismExpr = "(F" + marking + ") & (X(F(" + lowTrans.toString() + ")))";
		setPrismProperty(prismExpr, true);
		mOperands.add(lowTrans);
		mOperands.add(new Transition(highTrans.getName()));
		mOperands.add(new PlacePredicate(p.getName(), Relation.EQUALS, 1));
	}

	private String createMarkingExpr(AbstractIFNetTransition<IFNetFlowRelation> highTrans) {
		String expr = "";
		List<IFNetFlowRelation> incommingRels = highTrans.getIncomingRelations();
		for (IFNetFlowRelation rel : incommingRels) {
			Multiset<String> constraints = rel.getConstraint();
			Set<String> colors = highTrans.getConsumedColors();
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
