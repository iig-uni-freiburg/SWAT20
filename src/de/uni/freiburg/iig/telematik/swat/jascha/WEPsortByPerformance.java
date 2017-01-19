/**
 * 
 */
package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.Comparator;

import de.uni.freiburg.iig.telematik.swat.simulation.WorkflowExecutionPlan;

/**
 * @author Jascha
 * Compares (sorts) WorkflowExecutionPlans according to their Performance and numberOfRuns.
 * If two WEP have the same performance, the numberOfRuns is used for second level ordering.
 * If this is the same too, then the FireSequence String is used.
 * IMPORTANT:
 * Be careful when using this with TreeSets!
 *
 */
public class WEPsortByPerformance implements Comparator<WorkflowExecutionPlan> {

	@Override
	public int compare(WorkflowExecutionPlan wep0, WorkflowExecutionPlan wep1) {		
		if ((wep0.equals(wep1)) || (wep0 == wep1)){
			return 0;
		}
		if (wep0.getPerformance() > wep1.getPerformance()){
			return 1;
		}
		if (wep0.getPerformance() < wep1.getPerformance()){
			return -1;
		}
		if (wep0.getPerformance() == wep1.getPerformance()){
			//here we do a second level ordering with numberOfRuns
			if (wep0.getNumberOfRuns() < wep1.getNumberOfRuns())
				return -1;
			if (wep0.getNumberOfRuns() > wep1.getNumberOfRuns())
				return 1;
			if (wep0.getNumberOfRuns() == wep1.getNumberOfRuns()){
				String s0 = wep0.getSeq().getTransitionString();
				String s1 = wep1.getSeq().getTransitionString();
				return s0.compareTo(s1);
			}
		}
		return 0;
	}

}
