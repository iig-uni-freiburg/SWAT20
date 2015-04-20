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
import org.processmining.analysis.sciffchecker.logic.model.variable.OriginatorVariable;
import org.processmining.analysis.sciffchecker.logic.model.variable.StringVariableAttribute;
import org.processmining.analysis.sciffchecker.logic.model.variable.TimeVariable;
import org.processmining.analysis.sciffchecker.logic.util.EventType;
import org.processmining.analysis.sciffchecker.logic.util.TimeDisplacement;

import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.ModelInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.XESLogInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.ParameterTypeNames;

public class FourEyes extends CompliancePattern {

	public FourEyes() {
		ArrayList<String> paramTypes = new ArrayList<String>(Arrays.asList(ParameterTypeNames.ACTIVITY));
		mParameters.add(new Parameter(paramTypes, "A"));
		mParameters.add(new Parameter(paramTypes, "B"));
		setFormalization();
	}

	@Override
	public void acceptInfoProfider(ModelInfoProvider provider) {
		XESLogInfoProvider xesInfo = (XESLogInfoProvider) provider;
		mInfoProvider = xesInfo;
		for (Parameter p : mParameters) {
			p.setTypeRange(ParameterTypeNames.ACTIVITY, xesInfo.getActivities());
		}
	}

	@Override
	public String getName() {
		return "4 Eyes";
	}

	@Override
	public String getDescription() {
		return "Activity A and B must not ne performed by same user";
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
		SimpleActivityExecution activityExecution1 = new SimpleActivityExecution(body, "A",
				EventType.complete, false);
		
		// a certain activity is executed 
		if (mParameters.get(0).getValue().getType().equals(ParameterTypeNames.ACTIVITY)) {
			
			ActivityTypeVariable atv1 = new ActivityTypeVariable(activityExecution1);
			String activityName = mParameters.get(0).getValue().getValue(); 
			StringConstantAttribute activityNameConst = new StringConstantAttribute(activityName);
			new SimpleStringConstraint(atv1, StringOP.EQUAL, activityNameConst);
			r.setBody(body);
		} 

		
		Disjunction head = new Disjunction(r);
		Conjunction c = new Conjunction(head);
		SimpleActivityExecution activityExecution2 = new SimpleActivityExecution(c, "B",
				EventType.complete, false);
		
		// then a certain activity is executed afterwards but with other user
		if (mParameters.get(1).getValue().getType().equals(ParameterTypeNames.ACTIVITY)) {
			//ActivityTypeVariable atv1 = new ActivityTypeVariable(activityExecution2);
			OriginatorVariable o_b = new OriginatorVariable(activityExecution2);
			String activityName = mParameters.get(1).getValue().getValue(); //somewhoe get user here
			//StringConstantAttribute activityNameConst = new StringConstantAttribute(activityName);
			OriginatorVariable o_a = new OriginatorVariable(activityExecution1);
			StringVariableAttribute sva = new StringVariableAttribute(o_a);
			new SimpleStringConstraint(o_b, StringOP.EQUAL, sva);
		}
		
		new RelativeTimeConstraint(new TimeVariable(activityExecution2), TimeOP.AFTER, 
				new TimeVariable(activityExecution1), DisplacementOP.PLUS, new TimeDisplacement());
		ArrayList<CompositeRule> rules = new ArrayList<CompositeRule>();
		rules.add(cr);
		mFormalization = rules; 

	}

	@Override
	public boolean isAntiPattern() {
		return false;
	}

}
