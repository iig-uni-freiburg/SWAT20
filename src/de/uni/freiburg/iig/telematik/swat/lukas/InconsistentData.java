package de.uni.freiburg.iig.telematik.swat.lukas;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.constraint.AbstractConstraint;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractRegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;

public class InconsistentData extends DataflowPattern {
	
	public static final String NAME = "Inconsistent Data";
	
	public InconsistentData(Token token, Collection<AbstractRegularIFNetTransition<IFNetFlowRelation>> ts) {
		super();
		String formula = "";
		
		// all transitions in ts which change (write or destroy) the token
		ArrayList<AbstractRegularIFNetTransition<IFNetFlowRelation>> changeToken = 
				new ArrayList<AbstractRegularIFNetTransition<IFNetFlowRelation>>();
		
		// all transitions in ts which use (read, write or destroy the token or refer to it in guard)
		ArrayList<AbstractRegularIFNetTransition<IFNetFlowRelation>> useToken = 
				new ArrayList<AbstractRegularIFNetTransition<IFNetFlowRelation>>();
		
		for (AbstractRegularIFNetTransition<IFNetFlowRelation> t : ts) {
			
			Map<String, Set<AccessMode>> accessModesOfTransition = t.getAccessModes();
			Collection<AccessMode> ams = accessModesOfTransition.get(token.toString());
			if (ams  != null && (ams.contains(AccessMode.WRITE) || ams.contains(AccessMode.DELETE))) {
				changeToken.add(t);
			}
			Set<AbstractConstraint<?>> guards = t.getGuards();
			boolean guardRefersToToken = false;
			
			for (AbstractConstraint<?> guard : guards) {
				String element = guard.getElement();
				if (element.equals(token.toString())) {
					guardRefersToToken = true;
				}
			}
			
			if ((ams != null && (ams.contains(AccessMode.WRITE) || ams.contains(AccessMode.DELETE) 
					|| ams.contains(AccessMode.READ))) || guardRefersToToken) {
				useToken.add(t);
			}
		}
		
		for (AbstractRegularIFNetTransition<IFNetFlowRelation> t : changeToken) {
			formula += " | F((" + t.getName() + "=1) & (";
			for (AbstractRegularIFNetTransition<IFNetFlowRelation> t2 : useToken) {
				if (!t.equals(t2)) {
					formula += "(" + t2.getName() + "=1) | ";
				}
			}
			formula = formula.substring(0, formula.length() - 3) + ") | (";
			boolean parallelExecution = false;
			for (AbstractRegularIFNetTransition<IFNetFlowRelation> t2 : useToken) {
				if (!t.equals(t2) && t.getLabel().equals(t2.getLabel())) {
					formula += "(" + t.getName() + "=1) & (" + t2.getName() + "=1) | ";
					parallelExecution = true;
				}
			}
			if (parallelExecution) {
				formula = formula.substring(0, formula.length() - 3) + ")";
			} else {
				formula = formula.substring(0, formula.length() - 4) + ")";
			}
		}
		formula = formula.substring(3, formula.length());
		setPattern(formula, true);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

}
