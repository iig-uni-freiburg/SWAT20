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

public class CbeforeAndB extends LogCompliancePattern {

	public CbeforeAndB() {
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
		for (Parameter para : mParameters)
			para.setTypeRange(ParameterTypeNames.ACTIVITY, xesInfo.getActivities());

	}

	@Override
	public String getName() {
		return "C some time before A and B";
	}

	@Override
	public String getDescription() {
		return "C must occur before activity A and B";
	}

	@Override
	public CompliancePattern duplicate() {
		CbeforeAndB duplicate = new CbeforeAndB();
		duplicate.acceptInfoProfider(mInfoProvider);
		return duplicate();
	}

	@Override
	public void setFormalization() {

		CompositeRule cr = new CompositeRule();
		Rule r = new Rule(cr);
		Conjunction body = new Conjunction(r);

		SimpleActivityExecution execA = new SimpleActivityExecution(body, "A", EventType.complete, false);
		SimpleActivityExecution execB = new SimpleActivityExecution(body, "B", EventType.complete, false);
		TimeVariable t_a = new TimeVariable(execA);
		TimeVariable t_b = new TimeVariable(execB);

		if (!paramIsEmpty(0)) { //A
			ActivityTypeVariable atv1 = new ActivityTypeVariable(execA);
			String actName = mParameters.get(0).getValue().getValue();
			StringConstantAttribute activity1Name = new StringConstantAttribute(actName);
			new SimpleStringConstraint(atv1, StringOP.EQUAL, activity1Name);
		}

		if (!paramIsEmpty(1)) { //B
			ActivityTypeVariable atv2 = new ActivityTypeVariable(execB);
			String actBName = mParameters.get(1).getValue().getValue();
			StringConstantAttribute activity2Name = new StringConstantAttribute(actBName);
			new SimpleStringConstraint(atv2, StringOP.EQUAL, activity2Name);
		}

		r.setBody(body);
		Disjunction disjunct = new Disjunction(r);
		Conjunction conj = new Conjunction(disjunct);

		SimpleActivityExecution execC = new SimpleActivityExecution(conj, "C", EventType.complete, false);

		//Conjunction conj2 = new Conjunction(r);
		TimeVariable timeC = new TimeVariable(execC);
		RelativeTimeConstraint t_c1 = new RelativeTimeConstraint(timeC, TimeOP.BEFORE, t_a, DisplacementOP.PLUS, new TimeDisplacement());
		RelativeTimeConstraint t_c2 = new RelativeTimeConstraint(timeC, TimeOP.BEFORE, t_b, DisplacementOP.PLUS, new TimeDisplacement());

		if (!paramIsEmpty(2)) {
			ActivityTypeVariable atv3 = new ActivityTypeVariable(execC);
			String actCName = mParameters.get(2).getValue().getValue();
			StringConstantAttribute activity3Name = new StringConstantAttribute(actCName);
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

}
