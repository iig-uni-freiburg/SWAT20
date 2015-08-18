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

public class BbeforeA extends LogCompliancePattern {

	public BbeforeA() {
		ArrayList<String> paramTypes = new ArrayList<>(Arrays.asList(ParameterTypeNames.ACTIVITY));
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
		Parameter a = mParameters.get(0);
		Parameter b = mParameters.get(1);
		a.setTypeRange(ParameterTypeNames.ACTIVITY, xesInfo.getActivities());
		b.setTypeRange(ParameterTypeNames.ACTIVITY, xesInfo.getActivities());

	}

	@Override
	public String getName() {
		return "B some time before A";
	}

	@Override
	public String getDescription() {
		return getName();
	}

	@Override
	public CompliancePattern duplicate() {
		BbeforeA duplicate = new BbeforeA();
		duplicate.acceptInfoProfider(mInfoProvider);
		return duplicate;
	}

	@Override
	public final void setFormalization() {

		CompositeRule cr = new CompositeRule();
		Rule r = new Rule(cr);
		Conjunction body = new Conjunction(r);
		SimpleActivityExecution execA = new SimpleActivityExecution(body, "A", EventType.complete, false);

		if (!paramIsEmpty(0)) {
			ActivityTypeVariable atv1 = new ActivityTypeVariable(execA);
			String actName = mParameters.get(0).getValue().getValue();
			StringConstantAttribute activity1Name = new StringConstantAttribute(actName);
			new SimpleStringConstraint(atv1, StringOP.EQUAL, activity1Name);
		}
		r.setBody(body);

		Disjunction disjunct = new Disjunction(r);
		Conjunction conj = new Conjunction(disjunct);

		SimpleActivityExecution execB = new SimpleActivityExecution(conj, "B", EventType.complete, false);

		TimeVariable t_b = new TimeVariable(execB);
		TimeVariable t_a = new TimeVariable(execA);

		RelativeTimeConstraint timeB = new RelativeTimeConstraint(t_b, TimeOP.BEFORE, t_a, DisplacementOP.PLUS, new TimeDisplacement());

		if (!paramIsEmpty(1)) {
			ActivityTypeVariable atv2 = new ActivityTypeVariable(execB);
			StringConstantAttribute activityB = new StringConstantAttribute(mParameters.get(1).getValue().getValue());
			new SimpleStringConstraint(atv2, StringOP.EQUAL, activityB);
			//r.setHead(head);

		}

		ArrayList<CompositeRule> rules = new ArrayList<>();
		rules.add(cr);
		mFormalization = rules;

	}

	@Override
	public boolean isAntiPattern() {
		// TODO Auto-generated method stub
		return false;
	}

}
