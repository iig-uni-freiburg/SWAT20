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

public class IfAthenBorC extends LogCompliancePattern {

	public static void main(String args[]) {
		IfAthenBorC test = new IfAthenBorC();
		test.printRule();
	}

	public IfAthenBorC() {
		ArrayList<String> paramTypes = new ArrayList<String>(Arrays.asList(ParameterTypeNames.ACTIVITY));
		mParameters.add(new Parameter(paramTypes, "A"));
		mParameters.add(new Parameter(paramTypes, "B"));
		mParameters.add(new Parameter(paramTypes, "C"));
		//mPatternName = "If A then B";
		setFormalization();
	}

	@Override
	public void acceptInfoProfider(ModelInfoProvider provider) {
		XESLogInfoProvider xesInfo = (XESLogInfoProvider) provider;
		mInfoProvider = xesInfo;
		Parameter a = mParameters.get(0);
		Parameter b = mParameters.get(1);
		Parameter c = mParameters.get(2);
		a.setTypeRange(ParameterTypeNames.ACTIVITY, xesInfo.getActivities());
		b.setTypeRange(ParameterTypeNames.ACTIVITY, xesInfo.getActivities());
		c.setTypeRange(ParameterTypeNames.ACTIVITY, xesInfo.getActivities());
	}

	@Override
	public String getName() {
		return "If Activity A then B or C";
	}

	@Override
	public String getDescription() {
		return "If Activity A exists then Activity B or C must exist";
	}

	@Override
	public CompliancePattern duplicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setFormalization() {
		CompositeRule cr = new CompositeRule();
		Rule r = new Rule(cr);
		Conjunction body = new Conjunction(r);
		SimpleActivityExecution activityExec1 = new SimpleActivityExecution(body, "A", EventType.complete, false);

		if (mParameters.get(0).getValue().getType().equals(ParameterTypeNames.ACTIVITY)) {

			ActivityTypeVariable atv1 = new ActivityTypeVariable(activityExec1);
			String actName = mParameters.get(0).getValue().getValue();
			StringConstantAttribute activity1Name = new StringConstantAttribute(actName);
			new SimpleStringConstraint(atv1, StringOP.EQUAL, activity1Name);
			r.setBody(body);
		}

		Disjunction disjunct = new Disjunction(r);
		Conjunction conj1 = new Conjunction(disjunct);

		SimpleActivityExecution activityExec2 = new SimpleActivityExecution(conj1, "B", EventType.complete, false);

		if (mParameters.get(1).getValue().getType().equals(ParameterTypeNames.ACTIVITY)) {

			ActivityTypeVariable atv2 = new ActivityTypeVariable(activityExec2);
			StringConstantAttribute activityB = new StringConstantAttribute(mParameters.get(1).getValue().getValue());
			new SimpleStringConstraint(atv2, StringOP.EQUAL, activityB);
			//r.setHead(head);
		}

		Conjunction conj2 = new Conjunction(disjunct);

		SimpleActivityExecution activityExec3 = new SimpleActivityExecution(conj2, "C", EventType.complete, false);
		if (mParameters.get(2).getValue().getType().equals(ParameterTypeNames.ACTIVITY)) {
			ActivityTypeVariable atv3 = new ActivityTypeVariable(activityExec3);
			StringConstantAttribute activityC = new StringConstantAttribute(mParameters.get(2).getValue().getValue());
			new SimpleStringConstraint(atv3, StringOP.EQUAL, activityC);
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

	@Override
	public PatternRequirements[] requires() {
		PatternRequirements req[] = { PatternRequirements.COMPLETE };
		return req;
	}

}
