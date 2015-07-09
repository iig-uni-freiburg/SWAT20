package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.xeslog;

import java.util.ArrayList;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.processmining.analysis.sciffchecker.logic.model.rule.CompositeRule;
import org.processmining.analysis.sciffchecker.logic.xml.XMLRuleSerializer;

import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.PatternRequirements;

public abstract class LogCompliancePattern extends CompliancePattern {

	public abstract PatternRequirements[] requires();

	public void printRule() {
		try {
			System.out.println("Rule: ");
			Element output = XMLRuleSerializer.serialize(((ArrayList<CompositeRule>) mFormalization).get(0), "test");
			XMLOutputter outPutter = new XMLOutputter();
			outPutter.output(output, System.out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getFormalization() {
		Object result = super.getFormalization();
		//printRule();
		return result;
	}

	protected boolean paramIsEmpty(int i) {
		return mParameters.get(0).getValue().getValue().isEmpty();
	}

}
