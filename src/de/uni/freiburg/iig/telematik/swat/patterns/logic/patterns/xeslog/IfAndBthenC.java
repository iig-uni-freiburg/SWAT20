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

public class IfAndBthenC extends LogCompliancePattern {

	public IfAndBthenC() {
		ArrayList<String> paramTypes = new ArrayList<String>(Arrays.asList(ParameterTypeNames.ACTIVITY));
		mParameters.add(new Parameter(paramTypes, "A"));
		mParameters.add(new Parameter(paramTypes, "B"));
		mParameters.add(new Parameter(paramTypes, "C"));
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
		Parameter b = mParameters.get(1);
		b.setTypeRange(ParameterTypeNames.ACTIVITY, xesInfo.getActivities());
		Parameter c = mParameters.get(2);
		c.setTypeRange(ParameterTypeNames.ACTIVITY, xesInfo.getActivities());

	}

	@Override
	public String getName() {
		return "If A and B then C";
	}

	@Override
	public String getDescription() {
		return "If A and B occured, C must occur";
	}

	@Override
	public CompliancePattern duplicate() {
		IfAndBthenC duplicate = new IfAndBthenC();
		duplicate.acceptInfoProfider(mInfoProvider);
		return duplicate();
	}

	@Override
	public void setFormalization() {
		CompositeRule cr = new CompositeRule();

		//Empty Body
		Rule r = new Rule(cr);
		Conjunction body = new Conjunction(r);

		SimpleActivityExecution execA = new SimpleActivityExecution(body, "A", EventType.complete, false);

		if (TestParam(0)) {
			ActivityTypeVariable atv1 = new ActivityTypeVariable(execA);
			String actName = mParameters.get(0).getValue().getValue();
			StringConstantAttribute activity1Name = new StringConstantAttribute(actName);
			new SimpleStringConstraint(atv1, StringOP.EQUAL, activity1Name);
		}

		SimpleActivityExecution execB = new SimpleActivityExecution(body, "B", EventType.complete, false);

		if (TestParam(1)) {
			ActivityTypeVariable atv2 = new ActivityTypeVariable(execB);
			String actName = mParameters.get(1).getValue().getValue();
			StringConstantAttribute activity2Name = new StringConstantAttribute(actName);
			new SimpleStringConstraint(atv2, StringOP.EQUAL, activity2Name);
		}

		r.setBody(body);
		Disjunction disjunct = new Disjunction(r);
		Conjunction conj = new Conjunction(disjunct);

		SimpleActivityExecution execC = new SimpleActivityExecution(conj, "C", EventType.complete, false);

		if (TestParam(2)) {
			ActivityTypeVariable atv3 = new ActivityTypeVariable(execC);
			String actName = mParameters.get(2).getValue().getValue();
			StringConstantAttribute activity3Name = new StringConstantAttribute(actName);
			new SimpleStringConstraint(atv3, StringOP.EQUAL, activity3Name);
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

	private boolean TestParam(int i) {
		return mParameters.get(i).getValue().getType().equals(ParameterTypeNames.ACTIVITY)
				&& !mParameters.get(i).getValue().getValue().isEmpty();
	}

}
