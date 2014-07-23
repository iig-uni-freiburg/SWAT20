package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import de.invation.code.toval.constraint.AbstractConstraint;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;

public class InconsistentData extends DataflowPattern {
	
	public static final String NAME = "Inconsistent Data";
	
	public InconsistentData(Token token, Collection<RegularIFNetTransition> collection) {
		super("");
		String formula = "";
		
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
		
		HashMap<String, ArrayList<RegularIFNetTransition>> instOfSameTransMap = 
				new HashMap<String, ArrayList<RegularIFNetTransition>>();
		
		/* check for each instance of a transition if there is another instance of this transition and store it 
		 * in a map! */
		
		for (RegularIFNetTransition transOut : changeToken) {
		
			for (RegularIFNetTransition transIn : changeToken) {
				if (transOut.equals(transIn)) {
					continue;
				}   
				if (transOut.getLabel().equals(transIn.getLabel())) {
					ArrayList<RegularIFNetTransition> sameTransInsts = 
							instOfSameTransMap.get(transOut.getName());
					if (sameTransInsts == null) {
						sameTransInsts = new ArrayList<RegularIFNetTransition>(); 
					}
					sameTransInsts.add(transIn);
					instOfSameTransMap.put(transOut.getName(), sameTransInsts);
				}
			}
		}
		
		// build prism property as string
		String part1 = "", part2 = "";
		for (RegularIFNetTransition tChange : changeToken) {
			for (RegularIFNetTransition tUse : useToken) {
				part1 += getParallelExecExpr(tChange, tUse) + " | ";
			}
			ArrayList<RegularIFNetTransition> sameTransInsts = instOfSameTransMap.get(tChange.getName());
			if (sameTransInsts != null) {
				for (RegularIFNetTransition t : sameTransInsts) {
					part2 += getParallelExecExpr(tChange, t) + " | ";
				}
			}
			String str = part1 + "" + part2;
			str = str.substring(0, str.length() - 3);
			formula += "(F(" + str + ")) | ";
			
		}
		
		formula = formula.substring(0, formula.length() - 3);
		setPattern(formula, true);
	}
	
	private static String getParallelExecExpr(RegularIFNetTransition a, 
			RegularIFNetTransition b) {
		
		Transition t1 = new Transition(a.getName());
		Transition t2 = new Transition(b.getName());
		
		return "(((!" + t2.toString() + ") U (" + t1.toString() + " & (F" + t2.toString() + "))) "
				+ "& ((!" + t1.toString() + ") U (" + t2.toString() + " & (F" + t1.toString() + "))))";
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	public static void main(String [] args)
	{
		IFNet inet = IFNetTestUtils.create6PlaceIFnetWithAccessModes();
		Iterator<RegularIFNetTransition> it = inet.getRegularTransitions().iterator();
		RegularIFNetTransition a = it.next();
		RegularIFNetTransition b = it.next();
		String str = getParallelExecExpr(a, b);
		System.out.println(str);
		
	}

	// (!A U (B & F(A))) & (!B U (A & F(B)))
	 /* ((!(t1_last=1)) U ((tIn_last=1) & (F(t1_last=1)))) & ((!(tIn_last=1)) U ((t1_last=1) & (F(tIn_last=1))))
	 */
}
