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
import org.processmining.analysis.sciffchecker.logic.model.variable.OriginatorVariable;
import org.processmining.analysis.sciffchecker.logic.util.EventType;

import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.ModelInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.XESLogInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.PatternRequirements;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.ParameterTypeNames;

public class AperformedByB extends LogCompliancePattern {

	public AperformedByB() {
		ArrayList<String> paramTypes = new ArrayList<>(Arrays.asList(ParameterTypeNames.ACTIVITY));
		ArrayList<String> userType = new ArrayList<>(Arrays.asList(ParameterTypeNames.USER));
		mParameters.add(new Parameter(paramTypes, "A"));
		mParameters.add(new Parameter(userType, "B"));
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
		Parameter a = mParameters.get(0);
		Parameter b = mParameters.get(1);
		a.setTypeRange(ParameterTypeNames.ACTIVITY, xesInfo.getActivities());
		b.setTypeRange(ParameterTypeNames.USER, xesInfo.getSubjects());

	}

	@Override
	public String getName() {
		return "A performed by B";
	}

	@Override
	public String getDescription() {
		return "Activity A must be performed by user B";
	}

	@Override
	public CompliancePattern duplicate() {
		AperformedByB duplicate = new AperformedByB();
		duplicate.acceptInfoProfider(mInfoProvider);
		return duplicate;
	}

	@Override
	public final void setFormalization() {
		//TODO
		CompositeRule cr = new CompositeRule();
		Rule r = new Rule(cr);
		Conjunction body = new Conjunction(r);

		r.setBody(body);

		Disjunction disjunct = new Disjunction(r);
		Conjunction conj = new Conjunction(disjunct);

		SimpleActivityExecution activityExec1 = new SimpleActivityExecution(conj, "A", EventType.complete, false);

		if (mParameters.get(0).getValue().getType().equals(ParameterTypeNames.ACTIVITY)) {
			ActivityTypeVariable atv1 = new ActivityTypeVariable(activityExec1);
			String actName = mParameters.get(0).getValue().getValue();
			StringConstantAttribute activity1Name = new StringConstantAttribute(actName);
			new SimpleStringConstraint(atv1, StringOP.EQUAL, activity1Name);
		}

		//SimpleActivityExecution activityExec2 = new SimpleActivityExecution(conj, "B", EventType.complete, false);

		OriginatorVariable o_a = new OriginatorVariable(activityExec1);

		StringConstantAttribute activityOperator = new StringConstantAttribute(mParameters.get(1).getValue().getValue());


		SimpleStringConstraint const2 = new SimpleStringConstraint(o_a, StringOP.EQUAL, activityOperator);


		ArrayList<CompositeRule> rules = new ArrayList<>();
		rules.add(cr);
		mFormalization = rules;

	}

	@Override
	public boolean isAntiPattern() {
		return false;
	}

}
