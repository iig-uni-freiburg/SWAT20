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
		Disjunction d = new Disjunction(r);
		Conjunction c = new Conjunction(d);
		SimpleActivityExecution sae = new SimpleActivityExecution(c, "A", EventType.performed, false);
		ActivityTypeVariable atv = new ActivityTypeVariable(sae); 
		OriginatorVariable av = new OriginatorVariable(sae);
		StringConstantAttribute sca1 = new StringConstantAttribute(trans.getName());
		StringConstantAttribute sca2 = new StringConstantAttribute(user.toString());
		new SimpleStringConstraint(atv, StringOP.EQUAL, sca1);
		new SimpleStringConstraint(av, StringOP.EQUAL, sca2);
		r.setHead(d);
		
		// define body
		Conjunction con1 = new Conjunction(r);
		SimpleActivityExecution sae1 = new SimpleActivityExecution(con1, "A", EventType.performed, false);
		ActivityTypeVariable atv1 = new ActivityTypeVariable(sae1);
		new SimpleStringConstraint(atv1, StringOP.EQUAL, sca1);
		
		r.setBody(con1);
		return cr;	
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
