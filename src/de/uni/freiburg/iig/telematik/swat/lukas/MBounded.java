package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;
import java.util.List;

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
import org.processmining.analysis.sciffchecker.logic.util.EventType;

public class MBounded extends ResourcePattern {
	
	public static final String NAME = "A1,...,An M-Bounded U";
	public static final String DESC = "The activities A1,...,An are performed by the same user U.";
	
	public MBounded(List<Transition> transitions, User user) {
		
		List<CompositeRule> rules = new ArrayList<CompositeRule>();
		for (Transition trans : transitions) {
			rules.add(createRule(trans, user));
		}
		
		setRules(rules);
		
	}

	private CompositeRule createRule(Transition trans, User user) {
		
		CompositeRule cr = new CompositeRule();
		Rule r = new Rule(cr);
		
		// define head
		Disjunction head = new Disjunction(r);
		Conjunction c = new Conjunction(head);
		SimpleActivityExecution activityExec = new SimpleActivityExecution(c, "A", 
				EventType.performed, false);
		OriginatorVariable originator = new OriginatorVariable(activityExec);
		StringConstantAttribute userNameConst = new StringConstantAttribute(user.toString());
		new SimpleStringConstraint(originator, StringOP.EQUAL, userNameConst);
		r.setHead(head);
		
		// define body
		Conjunction body = new Conjunction(r);
		SimpleActivityExecution activityExec2 = new SimpleActivityExecution(body, "A",
				EventType.performed, false);
		ActivityTypeVariable activityVar = new ActivityTypeVariable(activityExec2);
		StringConstantAttribute actNameConst = new StringConstantAttribute(trans.getName());
		new SimpleStringConstraint(activityVar, StringOP.EQUAL, actNameConst);
		r.setBody(body);
		
		return cr;	
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
