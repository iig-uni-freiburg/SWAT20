/**
 * 
 */
package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.Comparator;

import de.uni.freiburg.iig.telematik.swat.simulation.WorkflowExecutionPlan;

/**
 * @author Jascha
 * Compares (sorts) WorkflowExecutionPlans according to their FireSequences "names" (lexicographical).
 * IMPORTANT:
 * For TreeSets using this comparator means that two WorkflowExecutionPlans are equal when their FireSequence is the same!
 * This also means that the "older" WEP will be replaced with the new WEP since each object can only be once in a TreeSet!
 */
public class WEPsortByFireSequence implements Comparator<WorkflowExecutionPlan> {

	@Override
	public int compare(WorkflowExecutionPlan wep0, WorkflowExecutionPlan wep1) {		
		return wep0.getSeq().getTransitionString().compareTo(wep1.getSeq().getTransitionString());
	}

}
