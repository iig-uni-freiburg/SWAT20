package de.uni.freiburg.iig.telematik.swat.lukas.patterns;

import java.util.ArrayList;
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
import org.processmining.analysis.sciffchecker.logic.model.variable.OriginatorVariable;
import org.processmining.analysis.sciffchecker.logic.model.variable.RoleVariable;
import org.processmining.analysis.sciffchecker.logic.model.variable.StringVariableAttribute;
import org.processmining.analysis.sciffchecker.logic.util.EventType;

import de.uni.freiburg.iig.telematik.seram.accesscontrol.rbac.RBACModel;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Role;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Transition;

public class RBoundedWith extends ResourcePattern {
	
	public static final String NAME = "T1, T2 R-Bounded-With R";
	public static final String DESC = "Activities T1 and T2 have to be performed by different Users with the same Role R.";
	
	public RBoundedWith(Transition t1, Transition t2, Role role) {
		
		CompositeRule cr = new CompositeRule();
		Rule r = new Rule(cr);
		
		// define body
		Conjunction body = new Conjunction(r);
		SimpleActivityExecution activityExec11 = 
				new SimpleActivityExecution(body, "A", EventType.complete, false);
		ActivityTypeVariable atv1 = new ActivityTypeVariable(activityExec11);
		StringConstantAttribute activity1Name = new StringConstantAttribute(t1.getName());
		new SimpleStringConstraint(atv1, StringOP.EQUAL, activity1Name);
		SimpleActivityExecution activityExec12 = 
				new SimpleActivityExecution(body, "B", EventType.complete, false);
		ActivityTypeVariable atv2 = new ActivityTypeVariable(activityExec12);
		StringConstantAttribute activity2Name = new StringConstantAttribute(t2.getName());
		new SimpleStringConstraint(atv2, StringOP.EQUAL, activity2Name);
		r.setBody(body);
		
		// define head
		Disjunction head = new Disjunction(r);
		Conjunction conjuncts = new Conjunction(head);
		SimpleActivityExecution activityExec21 = 
				new SimpleActivityExecution(conjuncts, "A", EventType.complete, false);
		SimpleActivityExecution activityExec22 = 
				new SimpleActivityExecution(conjuncts, "B", EventType.complete, false);
		OriginatorVariable originatorA = new OriginatorVariable(activityExec21);
		OriginatorVariable originatorB = new OriginatorVariable(activityExec22);
		StringVariableAttribute svaOB = new StringVariableAttribute(originatorB); 
		new SimpleStringConstraint(originatorA, StringOP.DIFFERENT, svaOB);
		RoleVariable roleA = new RoleVariable(activityExec21);
		RoleVariable roleB = new RoleVariable(activityExec22);
		StringConstantAttribute sca1 = new StringConstantAttribute(role.toString()); 
		new SimpleStringConstraint(roleA, StringOP.EQUAL, sca1);
		new SimpleStringConstraint(roleB, StringOP.EQUAL, sca1);
		r.setHead(head);
		
		ArrayList<CompositeRule> rules = new ArrayList<CompositeRule>();
		rules.add(cr);
		setRules(rules);
	}
	
	public RBoundedWith(Transition t1, Transition t2, Role role, RBACModel accessModel) {
		
		Set<String> legalRoles = accessModel.getDominatingRoles(role.getName());
		legalRoles.add(role.getName());
		
		CompositeRule cr = new CompositeRule();
		Rule r = new Rule(cr);
		
		// define body
		Conjunction body = new Conjunction(r);
		SimpleActivityExecution activityExec11 = 
				new SimpleActivityExecution(body, "A", EventType.complete, false);
		ActivityTypeVariable atv1 = new ActivityTypeVariable(activityExec11);
		StringConstantAttribute activity1Name = new StringConstantAttribute(t1.getName());
		new SimpleStringConstraint(atv1, StringOP.EQUAL, activity1Name);
		SimpleActivityExecution activityExec12 = 
				new SimpleActivityExecution(body, "B", EventType.complete, false);
		ActivityTypeVariable atv2 = new ActivityTypeVariable(activityExec12);
		StringConstantAttribute activity2Name = new StringConstantAttribute(t2.getName());
		new SimpleStringConstraint(atv2, StringOP.EQUAL, activity2Name);
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
			SimpleActivityExecution activityExec21 = 
					new SimpleActivityExecution(conjuncts, "A", EventType.complete, false);
			SimpleActivityExecution activityExec22 = 
					new SimpleActivityExecution(conjuncts, "B", EventType.complete, false);
			OriginatorVariable originatorA = new OriginatorVariable(activityExec21);
			OriginatorVariable originatorB = new OriginatorVariable(activityExec22);
			StringVariableAttribute svaOB = new StringVariableAttribute(originatorB); 
			new SimpleStringConstraint(originatorA, StringOP.DIFFERENT, svaOB);
			RoleVariable roleA = new RoleVariable(activityExec21);
			RoleVariable roleB = new RoleVariable(activityExec22);
			StringConstantAttribute sca = new StringConstantAttribute(role.toString()); 
			new SimpleStringConstraint(roleA, StringOP.EQUAL, sca);
			new SimpleStringConstraint(roleB, StringOP.EQUAL, sca);
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
