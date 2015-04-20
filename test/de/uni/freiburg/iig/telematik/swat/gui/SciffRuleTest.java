package de.uni.freiburg.iig.telematik.swat.gui;

import java.io.IOException;

import org.jdom.JDOMException;
import org.processmining.analysis.sciffchecker.gui.SciffRuleDialog;
import org.processmining.analysis.sciffchecker.logic.model.rule.CompositeRule;
import org.processmining.analysis.sciffchecker.logic.model.rule.RuleVisitorAdapter;

public class SciffRuleTest {

	public static void main(String args[]) throws JDOMException, IOException {
		CompositeRule r = SciffRuleDialog.showRuleDialog(null);
		RuleVisitorAdapter test = new RuleVisitorAdapter();
		test.visit(r);
	}
}
