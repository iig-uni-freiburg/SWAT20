package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.xeslog;

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
import org.processmining.analysis.sciffchecker.logic.model.variable.RoleVariable;
import org.processmining.analysis.sciffchecker.logic.util.EventType;
import org.processmining.analysis.sciffchecker.logic.xml.XMLRuleSerializer;

import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.ModelInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.XESLogInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.Absent;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.ParameterTypeNames;


public class XESLogAbsent extends Absent {

	public XESLogAbsent() {
		ArrayList<String> paramTypes = new ArrayList<>( 
				Arrays.asList(ParameterTypeNames.ACTIVITY, ParameterTypeNames.ROLE,
						ParameterTypeNames.USER));
		mParameters.add(new Parameter(paramTypes, "P"));
		setFormalization();
	}

        @Override
	public final void setFormalization() {
		
		CompositeRule cr = new CompositeRule();
		Rule r = new Rule(cr);
		
		// define body
		Conjunction body = new Conjunction(r);
		SimpleActivityExecution activityExec1 = new SimpleActivityExecution(body, "A",
				EventType.complete, false);
		
		// define head
		Disjunction head = new Disjunction(r);
		r.setHead(head);
		
            switch (mParameters.get(0).getValue().getType()) {
                case ParameterTypeNames.ACTIVITY:
                    ActivityTypeVariable atv1 = new ActivityTypeVariable(activityExec1);
                    String actName = mParameters.get(0).getValue().getValue();
                    StringConstantAttribute activity1Name = new StringConstantAttribute(actName);
                    new SimpleStringConstraint(atv1, StringOP.EQUAL, activity1Name);
                    r.setBody(body);
                    break;
                case ParameterTypeNames.USER:
                    OriginatorVariable originator = new OriginatorVariable(activityExec1);
                    StringConstantAttribute userNameConst = new StringConstantAttribute(
                            mParameters.get(0).getValue().getValue());
                    new SimpleStringConstraint(originator, StringOP.EQUAL, userNameConst);
                    r.setBody(body);
                    break;
                default:
                    RoleVariable role = new RoleVariable(activityExec1);
                    StringConstantAttribute roleNameConst = new StringConstantAttribute(
                            mParameters.get(0).getValue().getValue());
                    new SimpleStringConstraint(role, StringOP.EQUAL, roleNameConst);
                    r.setBody(body);
                    break;
            }
		
		ArrayList<CompositeRule> rules = new ArrayList<>();
		rules.add(cr);
		mFormalization = rules; 
		//printRule();
		
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
		XESLogAbsent duplicate = new XESLogAbsent();
		duplicate.acceptInfoProfider(mInfoProvider);
		return duplicate;
	}

	@Override
	public boolean isAntiPattern() {
		return false;
	}
	
	public void printRule() {
		try {
			System.out.println("Rule: ");
			Element output = XMLRuleSerializer.serialize(((ArrayList<CompositeRule>) mFormalization).get(0), "test");
			XMLOutputter outPutter = new XMLOutputter();
			outPutter.output(output, System.out);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


}
