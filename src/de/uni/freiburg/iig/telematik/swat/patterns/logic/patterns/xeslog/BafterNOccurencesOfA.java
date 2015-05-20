package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.xeslog;

import java.util.ArrayList;
import java.util.Arrays;

import org.processmining.analysis.sciffchecker.logic.model.DisplacementOP;
import org.processmining.analysis.sciffchecker.logic.model.StringOP;
import org.processmining.analysis.sciffchecker.logic.model.TimeOP;
import org.processmining.analysis.sciffchecker.logic.model.attribute.StringConstantAttribute;
import org.processmining.analysis.sciffchecker.logic.model.constraint.RelativeTimeConstraint;
import org.processmining.analysis.sciffchecker.logic.model.constraint.SimpleStringConstraint;
import org.processmining.analysis.sciffchecker.logic.model.rule.CompositeRule;
import org.processmining.analysis.sciffchecker.logic.model.rule.Conjunction;
import org.processmining.analysis.sciffchecker.logic.model.rule.Disjunction;
import org.processmining.analysis.sciffchecker.logic.model.rule.Rule;
import org.processmining.analysis.sciffchecker.logic.model.rule.execution.RepeatedActivityExecution;
import org.processmining.analysis.sciffchecker.logic.model.rule.execution.SimpleActivityExecution;
import org.processmining.analysis.sciffchecker.logic.model.variable.ActivityTypeVariable;
import org.processmining.analysis.sciffchecker.logic.model.variable.TimeVariable;
import org.processmining.analysis.sciffchecker.logic.util.EventType;
import org.processmining.analysis.sciffchecker.logic.util.TimeDisplacement;

import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.ModelInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.XESLogInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.PatternRequirements;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.ParameterTypeNames;

public class BafterNOccurencesOfA extends LogCompliancePattern {

	public BafterNOccurencesOfA() {
		ArrayList<String> paramTypes = new ArrayList<String>(Arrays.asList(ParameterTypeNames.ACTIVITY));
		ArrayList<String> numberType = new ArrayList<String>(Arrays.asList(ParameterTypeNames.NUMBER));
		mParameters.add(new Parameter(paramTypes, "A"));
		mParameters.add(new Parameter(numberType, "N"));
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
		//set Range for first and last Parameter: Activity (all Activities). scnd parameter is a number
		mParameters.get(0).setTypeRange(ParameterTypeNames.ACTIVITY, xesInfo.getActivities());
		mParameters.get(2).setTypeRange(ParameterTypeNames.ACTIVITY, xesInfo.getActivities());

	}

	@Override
	public String getName() {
		return "B after N occurences of A";
	}

	@Override
	public String getDescription() {
		return "B must occur some time after at least N occurences of A";
	}

	@Override
	public CompliancePattern duplicate() {
		BafterNOccurencesOfA duplicate = new BafterNOccurencesOfA();
		duplicate.acceptInfoProfider(mInfoProvider);
		return duplicate();
	}

	@Override
	public void setFormalization() {

		CompositeRule cr = new CompositeRule();
		Rule r = new Rule(cr);
		Conjunction body = new Conjunction(r);

		RepeatedActivityExecution execA = new RepeatedActivityExecution(body, "A", EventType.complete, getMin());
		TimeVariable timeA = new TimeVariable(execA, "completion");

		//SimpleActivityExecution execA = new SimpleActivityExecution(body, "A", EventType.complete, false);

		if (!paramIsEmpty(0)) {
			//Acticity A
			String activityA = mParameters.get(0).getValue().getValue();
			ActivityTypeVariable atvA = new ActivityTypeVariable(execA);
			StringConstantAttribute constA = new StringConstantAttribute(activityA);
			new SimpleStringConstraint(atvA, StringOP.EQUAL, constA);
		}

		r.setBody(body);

		Disjunction head = new Disjunction(r);
		Conjunction c = new Conjunction(head);

		SimpleActivityExecution execB = new SimpleActivityExecution(c, "B", EventType.complete, false);
		TimeVariable timeB = new TimeVariable(execB);
		RelativeTimeConstraint t_b = new RelativeTimeConstraint(timeB, TimeOP.AFTER, timeA, DisplacementOP.PLUS, new TimeDisplacement());

		if (!paramIsEmpty(2)) {
			//Activity B
			String activityB = mParameters.get(2).getValue().getValue();
			ActivityTypeVariable atvB = new ActivityTypeVariable(execB);
			StringConstantAttribute constB = new StringConstantAttribute(activityB);
			new SimpleStringConstraint(atvB, StringOP.EQUAL, constB);
		}

		ArrayList<CompositeRule> rules = new ArrayList<CompositeRule>();
		rules.add(cr);
		mFormalization = rules;

	}

	@Override
	public boolean isAntiPattern() {
		// TODO Auto-generated method stub
		return false;
	}

	private int getMin() {
		try {
			return Integer.parseInt(mParameters.get(1).getValue().getValue());
		} catch (Exception e) {
			return 2;
		}
	}

}
