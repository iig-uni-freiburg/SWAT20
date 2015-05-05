package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.xeslog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;
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
import org.processmining.analysis.sciffchecker.logic.model.variable.StringVariableAttribute;
import org.processmining.analysis.sciffchecker.logic.util.EventType;
import org.processmining.analysis.sciffchecker.logic.xml.XMLRuleSerializer;

import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.ModelInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.XESLogInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.PatternRequirements;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.ParameterTypeNames;

public class FourEyes extends LogCompliancePattern {

	public static void main(String args[]) throws IOException {
		FourEyes test = new FourEyes();
		CompositeRule rule = test.getRule();
		Element output = XMLRuleSerializer.serialize(rule, "test");
		XMLOutputter outPutter = new XMLOutputter();
		outPutter.output(output, System.out);

	}

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
		FourEyes duplicate = new FourEyes();
		duplicate.acceptInfoProfider(mInfoProvider);
		//duplicate.setLoadedFromDisk(loadedFromDisk);
		return duplicate;
	}

	@Override
	public void setFormalization() {
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

		SimpleActivityExecution activityExecution2 = new SimpleActivityExecution(c, "B", EventType.complete, true);
		OriginatorVariable o_b = new OriginatorVariable(activityExecution2);
		String activityName = mParameters.get(1).getValue().getValue(); //somewhoe get user here
		//StringConstantAttribute activityNameConst = new StringConstantAttribute(activityName);
		OriginatorVariable o_a = new OriginatorVariable(activityExecution1);
		StringVariableAttribute sva = new StringVariableAttribute(o_a);
		new SimpleStringConstraint(o_b, StringOP.EQUAL, sva);
		
		// then a certain activity is executed afterwards but with other user
		if (mParameters.get(1).getValue().getType().equals(ParameterTypeNames.ACTIVITY)) {
			ActivityTypeVariable atv1 = new ActivityTypeVariable(activityExecution2);
			String activityBName = mParameters.get(1).getValue().getValue();
			StringConstantAttribute activityB = new StringConstantAttribute(activityBName);
			SimpleStringConstraint atv1Constraint = new SimpleStringConstraint(atv1, StringOP.EQUAL, activityB);

		}
		
		//		new RelativeTimeConstraint(new TimeVariable(activityExecution2), TimeOP.AFTER, 
		//				new TimeVariable(activityExecution1), DisplacementOP.PLUS, new TimeDisplacement());
		ArrayList<CompositeRule> rules = new ArrayList<CompositeRule>();
		rules.add(cr);
		mFormalization = rules;
	}

	public CompositeRule getRule() {
		return ((ArrayList<CompositeRule>) mFormalization).get(0);
	}

	@Override
	public boolean isAntiPattern() {
		return false;
	}

	@Override
	public PatternRequirements[] requires() {
		PatternRequirements req[] = { PatternRequirements.COMPLETE };
		return req;
	}

}
