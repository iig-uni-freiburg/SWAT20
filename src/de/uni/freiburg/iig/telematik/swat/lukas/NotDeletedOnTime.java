package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.constraint.AbstractConstraint;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractRegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;

public class NotDeletedOnTime extends DataflowPattern {
	
	public static final String NAME = "D Not Deleted On Time";
	
	public static final String DESC = "A data element D is read and not destroyed "
			+ "and afterwards it is never read again.";
	
	public NotDeletedOnTime(Token token, Collection<RegularIFNetTransition> collection) {
		
		super();
		String formula = "";
		String readToken = getTransitionsAccessingToken(collection, token, AccessMode.READ);
		// all transitions in ts which read the token, refer to it in the guard, but don't destroy it
		Set<AbstractRegularIFNetTransition<IFNetFlowRelation>> transitions = 
				new HashSet<AbstractRegularIFNetTransition<IFNetFlowRelation>>();
		
		for (AbstractRegularIFNetTransition<IFNetFlowRelation> t : collection) {
			
			Map<String, Set<AccessMode>> amsPerToken = t.getAccessModes();
			Collection<AccessMode> ams = amsPerToken.get(token.toString());
			if (ams  != null && (ams.contains(AccessMode.READ)) && (!ams.contains(AccessMode.DELETE))) {
				transitions.add(t);
			}
			Set<AbstractConstraint<?>> guards = t.getGuards();
			for (AbstractConstraint<?> guard : guards) {
				String element = guard.getElement();
				if (element.equals(token.toString()) && ((ams  == null) || (!ams.contains(AccessMode.DELETE)))) {
					transitions.add(t);
				}
			}
		}
		
		if (transitions.size() == 0) {
			setPrismProperty("false", true);
		} else if (transitions.size() == 1) {
			AbstractRegularIFNetTransition<IFNetFlowRelation> t = transitions.iterator().next();
			formula += "G((" + t.getName() + "_last=1) => ((" + t.getName() + "_last=1) U (G(!" + readToken + "))))";
			setPrismProperty(formula, true);
		} else {
			for (AbstractRegularIFNetTransition<IFNetFlowRelation> t : transitions) {
				formula += " | (G((" + t.getName() + "_last=1) => ((" + t.getName() + 
						"_last=1) U (G(!" + readToken + ")))))";
			}
			formula = formula.substring(3, formula.length());
			setPrismProperty(formula, true);
		}
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
