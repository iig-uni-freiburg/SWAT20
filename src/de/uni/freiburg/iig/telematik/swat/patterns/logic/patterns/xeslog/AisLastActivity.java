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

public class AisLastActivity extends LogCompliancePattern {

	public AisLastActivity() {
		ArrayList<String> paramTypes = new ArrayList<>(Arrays.asList(ParameterTypeNames.ACTIVITY));
		mParameters.add(new Parameter(paramTypes, "A"));
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
		a.setTypeRange(ParameterTypeNames.ACTIVITY, xesInfo.getActivities());

	}

	@Override
	public String getName() {
		return "A is last activity";
	}

	@Override
	public String getDescription() {
		return "A must be the last activity";
	}

	@Override
	public CompliancePattern duplicate() {
		AisLastActivity duplicate = new AisLastActivity();
		duplicate.acceptInfoProfider(mInfoProvider);
		return duplicate;
	}

	@Override
	public final void setFormalization() {
		CompositeRule cr = new CompositeRule();
		Rule r = new Rule(cr);
		Conjunction body = new Conjunction(r);
		SimpleActivityExecution activityExec1 = new SimpleActivityExecution(body, "A", EventType.complete, false);

		if (mParameters.get(0).getValue().getType().equals(ParameterTypeNames.ACTIVITY)) {

			ActivityTypeVariable atv1 = new ActivityTypeVariable(activityExec1);
			String actName = mParameters.get(0).getValue().getValue();
			StringConstantAttribute activity1Name = new StringConstantAttribute(actName);
			new SimpleStringConstraint(atv1, StringOP.EQUAL, activity1Name);
		}
		r.setBody(body);

		Disjunction disjunct = new Disjunction(r);
		Conjunction conj = new Conjunction(disjunct);

		SimpleActivityExecution activityExec2 = new SimpleActivityExecution(conj, "B", EventType.complete, true);

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
