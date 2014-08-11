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
import org.processmining.analysis.sciffchecker.logic.model.variable.StringVariableAttribute;
import org.processmining.analysis.sciffchecker.logic.util.EventType;


public class USegregatedFrom extends ResourcePattern {
	
	public static final String NAME = "A1 U-Segregated-From A2";
	public static final String DESC = "Activity A1 and Activity A2 are performed by different Users.";
	
	public USegregatedFrom(Transition t1, Transition t2) {
		
		CompositeRule cr = new CompositeRule();
		Rule r = new Rule(cr);
		
		// define body
		Conjunction body = new Conjunction(r);
		
		SimpleActivityExecution activityExec1 = new SimpleActivityExecution(body, "A", EventType.performed, false);
		SimpleActivityExecution avtivityExec2 = new SimpleActivityExecution(body, "B", EventType.performed, false);
		
		ActivityTypeVariable atv1 = new ActivityTypeVariable(activityExec1);
		StringConstantAttribute activity1Name = new StringConstantAttribute(t1.getName());
		new SimpleStringConstraint(atv1, StringOP.EQUAL, activity1Name);
		
		ActivityTypeVariable atv2 = new ActivityTypeVariable(avtivityExec2);
		StringConstantAttribute activity2Name = new StringConstantAttribute(t2.getName());
		new SimpleStringConstraint(atv2, StringOP.EQUAL, activity2Name);
		
		r.setBody(body);
		
		// define head
		Disjunction head = new Disjunction(r);
		Conjunction conjuncts = new Conjunction(head);
		SimpleActivityExecution activityExec11 = 
				new SimpleActivityExecution(conjuncts, "A", EventType.performed, false);
		SimpleActivityExecution avtivityExec22 = 
				new SimpleActivityExecution(conjuncts, "B", EventType.performed, false);
		OriginatorVariable originatorOfakt1 = new OriginatorVariable(activityExec11);
		OriginatorVariable originatorOfakt2 = new OriginatorVariable(avtivityExec22);
		StringVariableAttribute sva2 = new StringVariableAttribute(originatorOfakt1);
		new SimpleStringConstraint(originatorOfakt2, StringOP.DIFFERENT, sva2);
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
		return DESC;
	}

}
