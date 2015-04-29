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
import org.processmining.analysis.sciffchecker.logic.model.variable.RoleVariable;
import org.processmining.analysis.sciffchecker.logic.util.EventType;

import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.ModelInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.XESLogInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.Exists;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.ParameterTypeNames;

public class XESLogExists extends Exists {
	
	public XESLogExists() {
		ArrayList<String> paramTypes = new ArrayList<String>( 
				Arrays.asList(ParameterTypeNames.ACTIVITY, ParameterTypeNames.ROLE,
						ParameterTypeNames.USER));
		mParameters.add(new Parameter(paramTypes, "P"));
		setFormalization();
	}

	@Override
	public void acceptInfoProfider(ModelInfoProvider provider) {
		
		XESLogInfoProvider xesInfo = (XESLogInfoProvider) provider;
		mInfoProvider = xesInfo;
		Parameter p = mParameters.get(0);
		p.setTypeRange(ParameterTypeNames.ACTIVITY, xesInfo.getActivities());
		p.setTypeRange(ParameterTypeNames.ROLE, xesInfo.getRoles());
		p.setTypeRange(ParameterTypeNames.USER, xesInfo.getSubjects());

	}

	@Override
	public CompliancePattern duplicate() {
		XESLogExists duplicate = new XESLogExists();
		duplicate.acceptInfoProfider(mInfoProvider);
		return duplicate;
	}

	@Override
	public void setFormalization() {
		
		CompositeRule cr = new CompositeRule();
		Rule r = new Rule(cr);
		
		// define head
		Disjunction head = new Disjunction(r);
		Conjunction c = new Conjunction(head);
		SimpleActivityExecution activityExec1 = new SimpleActivityExecution(c, "A",
				EventType.complete, false);
		
		if (mParameters.get(0).getValue().getType().equals(ParameterTypeNames.ACTIVITY)) {
			
			ActivityTypeVariable atv1 = new ActivityTypeVariable(activityExec1);
			String actName = mParameters.get(0).getValue().getValue(); 
			StringConstantAttribute activity1Name = new StringConstantAttribute(actName);
			new SimpleStringConstraint(atv1, StringOP.EQUAL, activity1Name);
			r.setHead(head);
			
		} else if (mParameters.get(0).getValue().getType().equals(ParameterTypeNames.USER)) {
			
			OriginatorVariable originator = new OriginatorVariable(activityExec1);
			StringConstantAttribute userNameConst = new StringConstantAttribute(
					mParameters.get(0).getValue().getValue());
			new SimpleStringConstraint(originator, StringOP.DIFFERENT, userNameConst);
			r.setHead(head);
			
		} else {
			
			RoleVariable role = new RoleVariable(activityExec1);
			StringConstantAttribute roleNameConst = new StringConstantAttribute(
					mParameters.get(0).getValue().getValue());
			new SimpleStringConstraint(role, StringOP.DIFFERENT, roleNameConst);
			r.setHead(head);
			
		}
		
		ArrayList<CompositeRule> rules = new ArrayList<CompositeRule>();
		rules.add(cr);
		mFormalization = rules;
		
	}

	@Override
	public boolean isAntiPattern() {
		return false;
	}

}
