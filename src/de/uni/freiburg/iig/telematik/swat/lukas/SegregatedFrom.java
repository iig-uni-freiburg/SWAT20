package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;

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


public class SegregatedFrom extends ResourcePattern {
	
	public static final String NAME = "A1 Segregated-From A2";
	public static final String DESC = null;
	
	public SegregatedFrom(Transition t1, Transition t2) {
		
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
		StringVariableAttribute svaRB = new StringVariableAttribute(roleB); 
		new SimpleStringConstraint(roleA, StringOP.DIFFERENT, svaRB);
		r.setHead(head);
		
		ArrayList<CompositeRule> rules = new ArrayList<CompositeRule>();
		rules.add(cr);
		setRules(rules);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
