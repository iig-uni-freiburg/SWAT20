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

public class NrepediationOfBbeforeA extends LogCompliancePattern {

	public static void main(String args[]) {
		NrepediationOfBbeforeA test = new NrepediationOfBbeforeA();
		test.printRule();
	}

	public NrepediationOfBbeforeA() {
		ArrayList<String> paramTypes = new ArrayList<String>(Arrays.asList(ParameterTypeNames.ACTIVITY));
		ArrayList<String> numberType = new ArrayList<String>(Arrays.asList(ParameterTypeNames.NUMBER));
		mParameters.add(new Parameter(paramTypes, "B"));
		mParameters.add(new Parameter(numberType, "min"));
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
		//set Range for first Parameter: Activity (all Activities)
		mParameters.get(0).setTypeRange(ParameterTypeNames.ACTIVITY, xesInfo.getActivities());
		mParameters.get(2).setTypeRange(ParameterTypeNames.ACTIVITY, xesInfo.getActivities());

	}

	@Override
	public String getName() {
		return "N repediations of B before A";
	}

	@Override
	public String getDescription() {
		return "B must occur at least N times before A may occur";
	}

	@Override
	public CompliancePattern duplicate() {
		NrepediationOfBbeforeA duplicate = new NrepediationOfBbeforeA();
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

		if (mParameters.get(2).getValue().getType().equals(ParameterTypeNames.ACTIVITY)) {
			//min & max Parameter

			String activityA = mParameters.get(2).getValue().getValue();
			
			ActivityTypeVariable atvA = new ActivityTypeVariable(execA);
			StringConstantAttribute constA = new StringConstantAttribute(activityA);
			new SimpleStringConstraint(atvA, StringOP.EQUAL, constA);
			r.setBody(body);
		}

		Disjunction head = new Disjunction(r);
		Conjunction c = new Conjunction(head);

		int min = getMin();
		RepeatedActivityExecution repeatB = new RepeatedActivityExecution(c, "B", EventType.complete, min);
		ActivityTypeVariable atvB = new ActivityTypeVariable(repeatB);


		TimeVariable t_a = new TimeVariable(execA);
		TimeVariable t_b = new TimeVariable(repeatB);

		RelativeTimeConstraint beforeB = new RelativeTimeConstraint(t_b, TimeOP.BEFORE, t_a, DisplacementOP.PLUS, new TimeDisplacement());
		
		String activityB = mParameters.get(0).getValue().getValue();
		StringConstantAttribute constB = new StringConstantAttribute(activityB);

		SimpleStringConstraint constr = new SimpleStringConstraint(atvB, StringOP.EQUAL, constB);

		ArrayList<CompositeRule> rules = new ArrayList<CompositeRule>();
		rules.add(cr);
		mFormalization = rules;

	}

	private int getMin(){
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
