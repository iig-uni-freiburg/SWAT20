package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.xeslog;

import java.util.ArrayList;
import java.util.Arrays;

import org.processmining.analysis.sciffchecker.logic.model.StringOP;
import org.processmining.analysis.sciffchecker.logic.model.attribute.StringConstantAttribute;
import org.processmining.analysis.sciffchecker.logic.model.constraint.SimpleStringConstraint;
import org.processmining.analysis.sciffchecker.logic.model.rule.CompositeRule;
import org.processmining.analysis.sciffchecker.logic.model.rule.Conjunction;
import org.processmining.analysis.sciffchecker.logic.model.rule.Disjunction;
import org.processmining.analysis.sciffchecker.logic.model.rule.Rule;
import org.processmining.analysis.sciffchecker.logic.model.rule.execution.SimpleActivityExecution;
import org.processmining.analysis.sciffchecker.logic.model.variable.ActivityTypeVariable;
import org.processmining.analysis.sciffchecker.logic.util.EventType;

import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.ModelInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.XESLogInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.PatternRequirements;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.ParameterTypeNames;

public class IfAthenNotB extends LogCompliancePattern {

	public IfAthenNotB() {
		ArrayList<String> paramTypes = new ArrayList<String>(Arrays.asList(ParameterTypeNames.ACTIVITY));
		mParameters.add(new Parameter(paramTypes, "A"));
		mParameters.add(new Parameter(paramTypes, "B"));
		setFormalization();
	}

	@Override
	public PatternRequirements[] requires() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void acceptInfoProfider(ModelInfoProvider provider) {
		XESLogInfoProvider xesInfo = (XESLogInfoProvider) provider;
		mInfoProvider = xesInfo;
		for (Parameter p : mParameters) {
			p.setTypeRange(ParameterTypeNames.ACTIVITY, xesInfo.getActivities());
		}

	}

	@Override
	public String getName() {
		return "If A then B is not allowed";
	}

	@Override
	public String getDescription() {
		return "If A occured then B must not occur";
	}

	@Override
	public CompliancePattern duplicate() {
		IfAthenNotB duplicate = new IfAthenNotB();
		duplicate.acceptInfoProfider(mInfoProvider);
		return duplicate;
	}

	@Override
	public void setFormalization() {

		CompositeRule cr = new CompositeRule();

		//Empty Body
		Rule r = new Rule(cr);
		Conjunction body = new Conjunction(r);
		r.setBody(body);


		
		SimpleActivityExecution execA = new SimpleActivityExecution(body, "A", EventType.complete, false);

		if (mParameters.get(0).getValue().getType().equals(ParameterTypeNames.ACTIVITY)) {
			//min & max Parameter
			String activityA = mParameters.get(0).getValue().getValue();
			ActivityTypeVariable atvA = new ActivityTypeVariable(execA);
			StringConstantAttribute stringConstA = new StringConstantAttribute(activityA);
			SimpleStringConstraint constA = new SimpleStringConstraint(atvA, StringOP.EQUAL, stringConstA);
		}

		Disjunction head = new Disjunction(r);
		Conjunction c = new Conjunction(head);

		SimpleActivityExecution execB = new SimpleActivityExecution(c, "B", EventType.complete, true);
		String activityB = mParameters.get(1).getValue().getValue();
		ActivityTypeVariable atvB = new ActivityTypeVariable(execB);
		StringConstantAttribute stringConstB = new StringConstantAttribute(activityB);
		SimpleStringConstraint constB = new SimpleStringConstraint(atvB, StringOP.EQUAL, stringConstB);

		ArrayList<CompositeRule> rules = new ArrayList<CompositeRule>();
		rules.add(cr);
		mFormalization = rules;

	}

	@Override
	public boolean isAntiPattern() {
		// TODO Auto-generated method stub
		return false;
	}

}
