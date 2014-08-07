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

public class BoundedWith extends ResourcePattern {
	
	public static final String NAME = "Bounded With";
	
	public BoundedWith(Transition t1, Transition t2) {
		
		CompositeRule cr = new CompositeRule();
		Rule r = new Rule(cr);
		
		// define body
		Conjunction con1 = new Conjunction(r);
		SimpleActivityExecution sae1 = new SimpleActivityExecution(con1, "A", EventType.performed, false);
		ActivityTypeVariable atv1 = new ActivityTypeVariable(sae1); 
		OriginatorVariable av1 = new OriginatorVariable(sae1);
		StringConstantAttribute sca21 = new StringConstantAttribute(t1.getName());
		SimpleStringConstraint c21 = new SimpleStringConstraint(atv1, StringOP.EQUAL, sca21);
		r.setBody(con1);
		
		// define head
		Disjunction d = new Disjunction(r);
		Conjunction c = new Conjunction(d);
		SimpleActivityExecution sae = new SimpleActivityExecution(c, "B", EventType.performed, false);
		ActivityTypeVariable atv = new ActivityTypeVariable(sae); 
		OriginatorVariable av = new OriginatorVariable(sae);
		StringConstantAttribute sca1 = new StringConstantAttribute(t2.getName());
		StringVariableAttribute sva2 = new StringVariableAttribute(av1); 
		new SimpleStringConstraint(atv, StringOP.EQUAL, sca1);
		new SimpleStringConstraint(av, StringOP.EQUAL, sva2);
		r.setHead(d);
		
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
