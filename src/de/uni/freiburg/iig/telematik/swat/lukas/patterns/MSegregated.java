package de.uni.freiburg.iig.telematik.swat.lukas.patterns;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.processmining.analysis.sciffchecker.logic.model.StringOP;
import org.processmining.analysis.sciffchecker.logic.model.attribute.StringConstantAttribute;
import org.processmining.analysis.sciffchecker.logic.model.constraint.SimpleStringConstraint;
import org.processmining.analysis.sciffchecker.logic.model.rule.CompositeRule;
import org.processmining.analysis.sciffchecker.logic.model.rule.Conjunction;
import org.processmining.analysis.sciffchecker.logic.model.rule.Disjunction;
import org.processmining.analysis.sciffchecker.logic.model.rule.Rule;
import org.processmining.analysis.sciffchecker.logic.model.rule.execution.SimpleActivityExecution;
import org.processmining.analysis.sciffchecker.logic.model.variable.ActivityTypeVariable;
import org.processmining.analysis.sciffchecker.logic.model.variable.RoleVariable;
import org.processmining.analysis.sciffchecker.logic.model.variable.StringVariableAttribute;
import org.processmining.analysis.sciffchecker.logic.util.EventType;

import de.invation.code.toval.misc.SetUtils;
import de.invation.code.toval.misc.SetUtils.PowerSet;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.rbac.RBACModel;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Role;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Transition;

public class MSegregated extends ResourcePattern {
	
	public static final String NAME  = "(T1,..., Tn) M-Segregated (R1,..., Rm)";
	public static final String DESC = "All Transitions have to be performed by one of the roles (R1,...,Rm)."
			+ " But not all Transitions are performed by the same role.";
	
	public MSegregated(Set<Transition> transitions, Set<Role> roles) {
		
		ArrayList<CompositeRule> rules = new ArrayList<CompositeRule>();
		
		ensureValidRole(transitions, roles, rules);
		
		ensureUnequalRoles(transitions, rules);
		
		setRules(rules);

	}
	
	public MSegregated(List<Transition> transitions, List<Role> roles, RBACModel model) {
		

	}


	private void ensureValidRole(Set<Transition> transitions, Set<Role> roles,
			ArrayList<CompositeRule> rules) {
		for (Transition trans : transitions) {
			
			CompositeRule cr = new CompositeRule();
			Rule r = new Rule(cr);
			
			// define body
			Conjunction body = new Conjunction(r);
			SimpleActivityExecution activityExec11 = 
					new SimpleActivityExecution(body, "A", EventType.complete, false);
			ActivityTypeVariable atv1 = new ActivityTypeVariable(activityExec11);
			StringConstantAttribute activity1Name = new StringConstantAttribute(trans.getName());
			new SimpleStringConstraint(atv1, StringOP.EQUAL, activity1Name);
			r.setBody(body);
			
			// define head
			Disjunction head = new Disjunction(r);
			
			for (Role roleOp : roles) {
				Conjunction conjuncts = new Conjunction(head);
				SimpleActivityExecution activityExec21 = 
						new SimpleActivityExecution(conjuncts, "A", EventType.complete, false);
				RoleVariable roleA = new RoleVariable(activityExec21);
				StringConstantAttribute sca1 = new StringConstantAttribute(roleOp.toString());
				new SimpleStringConstraint(roleA, StringOP.EQUAL, sca1);
			}
			
			r.setHead(head);
			
			rules.add(cr);

		}
	}


	private void ensureUnequalRoles(Set<Transition> transitions,
			ArrayList<CompositeRule> rules) {
		PowerSet<Transition> transPowerSet = SetUtils.getPowerSet(transitions);
		
		for (List<HashSet<Transition>> subsetsOfSameSize : transPowerSet.values()) {
			
			for (HashSet<Transition> subset : subsetsOfSameSize) {
				
				CompositeRule cr = new CompositeRule();
				Rule r = new Rule(cr);
				// define body
				Conjunction body = new Conjunction(r);
				int counter = 0;
				for (Transition trans : subset) {
					counter++;
					String variableName = "A" + counter;
					SimpleActivityExecution activityExec = 
							new SimpleActivityExecution(body, variableName, 
									EventType.complete, false);
					ActivityTypeVariable atv1 = new ActivityTypeVariable(activityExec);
					StringConstantAttribute activity1Name = new StringConstantAttribute(trans.getName());
					new SimpleStringConstraint(atv1, StringOP.EQUAL, activity1Name);
				}
				
				r.setBody(body);
				
				Disjunction head = new Disjunction(r);
				RoleVariable firstRole = null;
				
				for (int i=1; i<=subset.size(); i++) {
					Conjunction c = new Conjunction(head);
					counter++;
					String variableName = "A" + i;
					SimpleActivityExecution activityExec = 
							new SimpleActivityExecution(c, variableName, 
									EventType.complete, false);
					if (firstRole == null) {
						firstRole = new RoleVariable(activityExec);
						continue;
					} else {
						RoleVariable roleVar = new RoleVariable(activityExec);
						StringVariableAttribute svaR = new StringVariableAttribute(firstRole); 
						new SimpleStringConstraint(roleVar, StringOP.DIFFERENT, svaR);
					}
				}
				
				r.setHead(head);
				rules.add(cr);
			}		
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
