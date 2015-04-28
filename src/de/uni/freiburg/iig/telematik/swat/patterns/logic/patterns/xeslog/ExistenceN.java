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
import org.processmining.analysis.sciffchecker.logic.model.variable.ActivityTypeVariable;
import org.processmining.analysis.sciffchecker.logic.util.EventType;

import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.ModelInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.XESLogInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.PatternRequirements;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.ParameterTypeNames;

public class ExistenceN extends LogCompliancePattern {

	public static void main(String args[]) {
		ExistenceN rule = new ExistenceN();
		rule.printRule();

	}

	public ExistenceN() {
		ArrayList<String> paramTypes = new ArrayList<String>(Arrays.asList(ParameterTypeNames.ACTIVITY));
		ArrayList<String> numberType = new ArrayList<String>(Arrays.asList(ParameterTypeNames.NUMBER));
		mParameters.add(new Parameter(paramTypes, "Activity"));
		mParameters.add(new Parameter(numberType, "min"));
		mParameters.add(new Parameter(numberType, "max"));
		setFormalization();
	}

	@Override
	public void acceptInfoProfider(ModelInfoProvider provider) {
		XESLogInfoProvider xesInfo = (XESLogInfoProvider) provider;
		mInfoProvider = xesInfo;
		//set Range for first Parameter: Activity (all Activities)
		mParameters.get(0).setTypeRange(ParameterTypeNames.ACTIVITY, xesInfo.getActivities());

	}

	@Override
	public String getName() {
		return "Repeat N-M times";

	}

	@Override
	public String getDescription() {
		return "Activity must repeat at least N and most M times";
	}

	@Override
	public CompliancePattern duplicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setFormalization() {
		CompositeRule cr = new CompositeRule();

		//Empty Body
		Rule r = new Rule(cr);
		Conjunction body = new Conjunction(r);
		r.setBody(body);

		Disjunction head = new Disjunction(r);
		Conjunction c = new Conjunction(head);

		if (mParameters.get(1).getValue().getType().equals(ParameterTypeNames.NUMBER)
				&& mParameters.get(2).getValue().getType().equals(ParameterTypeNames.NUMBER)) {
			//min & max Parameter
			int min = getMin();
			int max = getMax();
			String activity = mParameters.get(0).getValue().getValue();
			StringConstantAttribute stringConst = new StringConstantAttribute(activity);

			RepeatedActivityExecution exec = new RepeatedActivityExecution(c, "A", EventType.complete, max, min);

			ActivityTypeVariable atv = new ActivityTypeVariable(exec);
			StringConstantAttribute strconst = new StringConstantAttribute(activity);
			SimpleStringConstraint stc = new SimpleStringConstraint(atv, StringOP.EQUAL, strconst);

			String activityName = mParameters.get(0).getValue().getValue();
			StringConstantAttribute stringConstant = new StringConstantAttribute(activityName);
		}


		ArrayList<CompositeRule> rules = new ArrayList<CompositeRule>();
		rules.add(cr);
		mFormalization = rules;

	}

	@Override
	public boolean isAntiPattern() {
		return false;
	}

	private int getMin() {
		try {
			return Integer.parseInt(mParameters.get(1).getValue().getValue());
		} catch (Exception e) {
			//Rule not yet instantiatet, return 0
			return 0;
		}
	}

	private int getMax() {
		try {
			return Integer.parseInt(mParameters.get(2).getValue().getValue());
		} catch (Exception e) {
			//Rule not yet instantiatet, return 2
			return 2;
		}
	}

	@Override
	public PatternRequirements[] requires() {
		PatternRequirements req[] = { PatternRequirements.COMPLETE };
		return req;
	}

}
