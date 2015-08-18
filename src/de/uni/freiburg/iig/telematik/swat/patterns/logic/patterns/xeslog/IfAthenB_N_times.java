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
import org.processmining.analysis.sciffchecker.logic.model.rule.execution.RepeatedActivityExecution;
import org.processmining.analysis.sciffchecker.logic.model.rule.execution.SimpleActivityExecution;
import org.processmining.analysis.sciffchecker.logic.model.variable.ActivityTypeVariable;
import org.processmining.analysis.sciffchecker.logic.util.EventType;

import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.ModelInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.XESLogInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.PatternRequirements;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.ParameterTypeNames;

public class IfAthenB_N_times extends LogCompliancePattern {

	public IfAthenB_N_times() {
		ArrayList<String> paramTypes = new ArrayList<>(Arrays.asList(ParameterTypeNames.ACTIVITY));
		ArrayList<String> numberType = new ArrayList<>(Arrays.asList(ParameterTypeNames.NUMBER));
		mParameters.add(new Parameter(paramTypes, "A"));
		mParameters.add(new Parameter(numberType, "N"));
		mParameters.add(new Parameter(paramTypes, "B"));
		setFormalization();
	}

	@Override
	public PatternRequirements[] requires() {
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
		return "If A then B N times";
	}

	@Override
	public String getDescription() {
		return "If activity A occured, activity B must occur N times";
	}

	@Override
	public CompliancePattern duplicate() {
		IfAthenB_N_times duplicate = new IfAthenB_N_times();
		duplicate.acceptInfoProfider(mInfoProvider);
		return duplicate;
	}

	@Override
	public final void setFormalization() {

		CompositeRule cr = new CompositeRule();
		Rule r = new Rule(cr);
		Conjunction body = new Conjunction(r);

		//RepeatedActivityExecution execA = new RepeatedActivityExecution(body, "A", EventType.complete, getMin());

		SimpleActivityExecution execA = new SimpleActivityExecution(body, "A", EventType.complete, false);

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

		//SimpleActivityExecution execB = new SimpleActivityExecution(c, "B", EventType.complete, false);
		RepeatedActivityExecution execB = new RepeatedActivityExecution(c, "B", EventType.complete, getMin());

		if (!paramIsEmpty(2)) {
			//Activity B
			String activityB = mParameters.get(2).getValue().getValue();
			ActivityTypeVariable atvB = new ActivityTypeVariable(execB);
			StringConstantAttribute constB = new StringConstantAttribute(activityB);
			new SimpleStringConstraint(atvB, StringOP.EQUAL, constB);
		}

		ArrayList<CompositeRule> rules = new ArrayList<>();
		rules.add(cr);
		mFormalization = rules;

	}

	private int getMin() {
		try {
			return Integer.parseInt(mParameters.get(1).getValue().getValue());
		} catch (Exception e) {
			return 2;
		}
	}

	@Override
	public boolean isAntiPattern() {
		// TODO Auto-generated method stub
		return false;
	}

}
