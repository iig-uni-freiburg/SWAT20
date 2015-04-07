package de.uni.freiburg.iig.telematik.swat.lukas.patterns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.constraint.AbstractConstraint;
import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Token;

public class InconsistentData extends DataflowAntiPattern {
	
	public static final String NAME = "Inconsistent Data D";
	public static final String DESC = "A task is using a data element while another is writing or destroying it.";
	
	public InconsistentData(Token token, Collection<RegularIFNetTransition> collection) {
		super();
		
		// all transitions in ts which change (write or destroy) the token
		ArrayList<RegularIFNetTransition> changeToken = 
				new ArrayList<RegularIFNetTransition>();
		
		// all transitions in ts which use (read, write or destroy the token or refer to it in guard)
		ArrayList<RegularIFNetTransition> useToken = 
				new ArrayList<RegularIFNetTransition>();
		
		for (RegularIFNetTransition t : collection) {
			
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

		String parallelExecExpr = "";
		
		for (RegularIFNetTransition transChange : changeToken) {
			
			for (RegularIFNetTransition transUse : useToken) {
				
				if (transUse.getName().equals(transChange.getName())) {
					continue;
				}
				
				String enabledPred = getEnabledTransPredicate(transChange, transUse);
				parallelExecExpr += "(" + enabledPred + ") | ";
				
			}
			
		}
		
		if (!parallelExecExpr.equals("")) {
			setPrismProperty("F(" + parallelExecExpr.substring(0, parallelExecExpr.length() - 3) + ")", true);
		} else {
			setPrismProperty("false", true);
		}
		
	}
	
	private String getEnabledTransPredicate(RegularIFNetTransition transChange,
			RegularIFNetTransition transUse) {
		
		String predicate = "";
		
		HashMap<String, HashMap<String, Integer>> placeToMulMap = new HashMap<String, HashMap<String, Integer>>();
		
		Iterator<IFNetFlowRelation> changeIt = transChange.getIncomingRelations().iterator();
		Iterator<IFNetFlowRelation> transIt = transUse.getIncomingRelations().iterator();
		
		predicateHelper(changeIt, placeToMulMap);
		predicateHelper(transIt, placeToMulMap);
		
		for (Map.Entry<String, HashMap<String, Integer>> colorOfPlace : placeToMulMap.entrySet()) {
			
			for (Map.Entry<String, Integer> mulOfColor : colorOfPlace.getValue().entrySet()) {
				 predicate += " & " + colorOfPlace.getKey() + "_" + mulOfColor.getKey() 
						 + ">" + (mulOfColor.getValue() - 1);
			}
			
		}
		
		return predicate.substring(3, predicate.length());
	}





	private void predicateHelper(Iterator<IFNetFlowRelation> it,
			HashMap<String, HashMap<String, Integer>> placeToMulMap) {
		
		while(it.hasNext()) {
			
			IFNetFlowRelation flowRel = it.next();
			IFNetPlace inputPlace = flowRel.getPlace();
			Set<String> colors = flowRel.getConstraint().support();
			Multiset<String> constraint = flowRel.getConstraint();
			Iterator<String> colorIt = colors.iterator();
			HashMap<String, Integer> colorToMulMap = placeToMulMap.get(inputPlace.getName());
			if (colorToMulMap == null) {
				colorToMulMap = new HashMap<String, Integer>();
				
			}
			
			while(colorIt.hasNext()) {
				
				String color = colorIt.next();
				int multiplicity = constraint.multiplicity(color);
				Integer mulInMap = colorToMulMap.get(color);
				
				if (mulInMap == null) {
					colorToMulMap.put(color, multiplicity);
				} else {
					colorToMulMap.put(color, multiplicity + mulInMap);
				}
				
			}
			
			placeToMulMap.put(inputPlace.getName(), colorToMulMap);
			
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
