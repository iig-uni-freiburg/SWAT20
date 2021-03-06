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

public class IfAThenB extends LogCompliancePattern {

	protected final String mPatternName = "If A then B";

	protected final String mDescription = "If Activity A exists, activity B must exist";

	// <RuleEntry name="test"><Rule><Body><Conjunction><Activities><SimpleActivity name="A" forbidden="false" /></Activities></Conjunction></Body><Head><Disjunction><Conjunction><Activities><SimpleActivity name="B" forbidden="false" /></Activities></Conjunction></Disjunction></Head></Rule></RuleEntry>

	public IfAThenB() {
		ArrayList<String> paramTypes = new ArrayList<>(Arrays.asList(ParameterTypeNames.ACTIVITY));
		mParameters.add(new Parameter(paramTypes, "A"));
		mParameters.add(new Parameter(paramTypes, "B"));
		//mPatternName = "If A then B";
		setFormalization();
	}

	public static void main(String args[]) {
		IfAThenB test = new IfAThenB();
		test.printRule();
	}

	@Override
	public void acceptInfoProfider(ModelInfoProvider provider) {
		XESLogInfoProvider xesInfo = (XESLogInfoProvider) provider;
		mInfoProvider = xesInfo;
		Parameter p = mParameters.get(0);
		p.setTypeRange(ParameterTypeNames.ACTIVITY, xesInfo.getActivities());
		Parameter q = mParameters.get(1);
		q.setTypeRange(ParameterTypeNames.ACTIVITY, xesInfo.getActivities());

	}

	@Override
	public CompliancePattern duplicate() {
		IfAThenB duplicate = new IfAThenB();
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
		
		SimpleActivityExecution activityExec2 = new SimpleActivityExecution(conj, "B", EventType.complete, false);
		
		if (mParameters.get(1).getValue().getType().equals(ParameterTypeNames.ACTIVITY)) {

			ActivityTypeVariable atv2 = new ActivityTypeVariable(activityExec2);
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
		return false;
	}

	@Override
	public String getName() {
		return mPatternName;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

	@Override
	public PatternRequirements[] requires() {
		PatternRequirements req[] = { PatternRequirements.COMPLETE };
		return req;
	}

}
