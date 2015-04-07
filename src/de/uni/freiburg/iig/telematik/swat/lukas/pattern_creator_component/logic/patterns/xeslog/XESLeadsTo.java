package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns.xeslog;

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
import org.processmining.analysis.sciffchecker.logic.model.variable.RoleVariable;
import org.processmining.analysis.sciffchecker.logic.model.variable.TimeVariable;
import org.processmining.analysis.sciffchecker.logic.util.EventType;
import org.processmining.analysis.sciffchecker.logic.util.TimeDisplacement;

import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.model_info_provider.ModelInfoProvider;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.model_info_provider.XESLogInfoProvider;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns.LeadsTo;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns.parameter.ParameterTypeNames;

public class XESLeadsTo extends LeadsTo {

	public XESLeadsTo() {
		ArrayList<String> paramTypes = new ArrayList<String>( 
				Arrays.asList(ParameterTypeNames.ACTIVITY, ParameterTypeNames.ROLE,
						ParameterTypeNames.USER));
		mParameters.add(new Parameter(paramTypes, "P"));
		mParameters.add(new Parameter(paramTypes, "Q"));
		setFormalization();
	}

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
		// an activity is executed by a certain user 
		else if (mParameters.get(0).getValue().getType().equals(ParameterTypeNames.USER)) {
			
			OriginatorVariable originator = new OriginatorVariable(activityExecution1);
			StringConstantAttribute userNameConst = new StringConstantAttribute(
					mParameters.get(0).getValue().getValue());
			new SimpleStringConstraint(originator, StringOP.EQUAL, userNameConst);
			r.setBody(body);
			
		} 
		// an activity is executed by a user with a certain role
		else {
			
			RoleVariable role = new RoleVariable(activityExecution1);
			StringConstantAttribute roleNameConst = new StringConstantAttribute(
					mParameters.get(0).getValue().getValue());
			new SimpleStringConstraint(role, StringOP.EQUAL, roleNameConst);
			r.setBody(body);
		}
		
		Disjunction head = new Disjunction(r);
		Conjunction c = new Conjunction(head);
		SimpleActivityExecution activityExecution2 = new SimpleActivityExecution(c, "B",
				EventType.complete, false);
		
		// then a certain activity is executed afterwards
		if (mParameters.get(1).getValue().getType().equals(ParameterTypeNames.ACTIVITY)) {
			ActivityTypeVariable atv1 = new ActivityTypeVariable(activityExecution2);
			String activityName = mParameters.get(1).getValue().getValue(); 
			StringConstantAttribute activityNameConst = new StringConstantAttribute(activityName);
			new SimpleStringConstraint(atv1, StringOP.EQUAL, activityNameConst);
		}
		// then an activity is executed by a certain user
		else if (mParameters.get(1).getValue().getType().equals(ParameterTypeNames.USER)) {
			OriginatorVariable originator = new OriginatorVariable(activityExecution2);
			StringConstantAttribute userNameConst = new StringConstantAttribute(
					mParameters.get(1).getValue().getValue());
			new SimpleStringConstraint(originator, StringOP.EQUAL, userNameConst);
		} 
		// then an activity is executed by a certain role
		else {
			RoleVariable role = new RoleVariable(activityExecution2);
			StringConstantAttribute roleNameConst = new StringConstantAttribute(
					mParameters.get(1).getValue().getValue());
			new SimpleStringConstraint(role, StringOP.EQUAL, roleNameConst);
			
		}
		
		new RelativeTimeConstraint(new TimeVariable(activityExecution2), TimeOP.AFTER, 
				new TimeVariable(activityExecution1), DisplacementOP.PLUS, new TimeDisplacement());
		ArrayList<CompositeRule> rules = new ArrayList<CompositeRule>();
		rules.add(cr);
		mFormalization = rules; 
		
	}

	@Override
	public void acceptInfoProfider(ModelInfoProvider provider) {
		
		XESLogInfoProvider xesInfo = (XESLogInfoProvider) provider;
		mInfoProvider = xesInfo;
		for (Parameter p : mParameters) {
			p.setTypeRange(ParameterTypeNames.ACTIVITY, xesInfo.getActivities());
			p.setTypeRange(ParameterTypeNames.ROLE, xesInfo.getRoles());
			p.setTypeRange(ParameterTypeNames.USER, xesInfo.getSubjects());
		}
		
	}

	@Override
	public CompliancePattern duplicate() {
		XESLeadsTo duplicate = new XESLeadsTo();
		duplicate.acceptInfoProfider(mInfoProvider);
		return duplicate;
	}

	@Override
	public boolean isAntiPattern() {
		return false;
	}

}
