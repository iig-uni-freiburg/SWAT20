package de.uni.freiburg.iig.telematik.swat.lukas.patterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
import org.processmining.analysis.sciffchecker.logic.util.EventType;

import de.uni.freiburg.iig.telematik.sewol.accesscontrol.rbac.RBACModel;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Role;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Activity;

public class PerformedBy extends OrganizationalPattern {
	
	public static final String NAME = "T Performed-By R";
	public static final String DESC = "Transition T is performed by a User with Role R.";
	
	public PerformedBy(Activity t, Role role) {
				
		CompositeRule cr = new CompositeRule();
		Rule r = new Rule(cr);
		
		// define body
		Conjunction body = new Conjunction(r);
		
		SimpleActivityExecution activityExec = 
				new SimpleActivityExecution(body, "A", EventType.complete, false);
		
		ActivityTypeVariable atv1 = new ActivityTypeVariable(activityExec);
		StringConstantAttribute activity1Name = new StringConstantAttribute(t.getName());
		new SimpleStringConstraint(atv1, StringOP.EQUAL, activity1Name);
		r.setBody(body);
		
		// define head
		Disjunction head = buildHeadConjunctions(r, 
				new HashSet<String>(Arrays.asList(role.getName())));
		
		r.setHead(head);
		
		ArrayList<CompositeRule> rules = new ArrayList<CompositeRule>();
		rules.add(cr);
		setRules(rules); 
	}
	
	public PerformedBy(Activity t, Role role, RBACModel accessModel) {
		
		Set<String> legalRoles = accessModel.getDominatingRoles(role.getName());
		legalRoles.add(role.getName());
		
		CompositeRule cr = new CompositeRule();
		Rule r = new Rule(cr);
		
		// define body
		Conjunction body = new Conjunction(r);
		
		SimpleActivityExecution activityExec = 
				new SimpleActivityExecution(body, "A", EventType.complete, false);
		
		ActivityTypeVariable atv1 = new ActivityTypeVariable(activityExec);
		StringConstantAttribute activity1Name = new StringConstantAttribute(t.getName());
		new SimpleStringConstraint(atv1, StringOP.EQUAL, activity1Name);
		r.setBody(body);
		
		// define head
		Disjunction head = buildHeadConjunctions(r, legalRoles);
		
		r.setHead(head);
		
		ArrayList<CompositeRule> rules = new ArrayList<CompositeRule>();
		rules.add(cr);
		setRules(rules);
		
	}

	private Disjunction buildHeadConjunctions(Rule r, Set<String> dominatingRoles) {
		
		Disjunction head = new Disjunction(r);
		
		for (String role : dominatingRoles) {
			Conjunction conjuncts = new Conjunction(head);
			SimpleActivityExecution activityExec11 = 
					new SimpleActivityExecution(conjuncts, "A", EventType.complete, false);
			StringConstantAttribute roleName = new StringConstantAttribute(role);
			RoleVariable roleOfOriginator = new RoleVariable(activityExec11);
			new SimpleStringConstraint(roleOfOriginator, StringOP.EQUAL, roleName);
		}
		
		return head;
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
