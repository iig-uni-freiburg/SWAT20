package de.uni.freiburg.iig.telematik.swat.lukas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.XMLOutputter;
import org.processmining.analysis.sciffchecker.logic.model.rule.CompositeRule;
import org.processmining.analysis.sciffchecker.logic.xml.XMLRuleSerializer;
import de.invation.code.toval.parser.ParserException;


public abstract class ResourcePattern extends CompliancePattern {
	
	private static HashMap<String, String> mPatternDescription;
	private List<CompositeRule> mRules;

	public ResourcePattern() {
		super(false);
	}

	public static final String NAME = "Resource Pattern";
	
	public static ArrayList<String> getPatternNames() {
		
		return new ArrayList<String>(Arrays.asList(PerformedBy.NAME, SegregatedFrom.NAME,
				USegregatedFrom.NAME, BoundedWith.NAME, MSegregated.NAME, RBoundedWith.NAME));
		
	}
	
	 public static HashMap<String, String> getPatternDescription() {
			
			if (mPatternDescription == null) {
				mPatternDescription = new HashMap<String, String>(); 
				mPatternDescription.put(PerformedBy.NAME, PerformedBy.DESC);
				mPatternDescription.put(SegregatedFrom.NAME, SegregatedFrom.DESC);
				mPatternDescription.put(USegregatedFrom.NAME, USegregatedFrom.DESC);
				mPatternDescription.put(BoundedWith.NAME, BoundedWith.DESC);
				mPatternDescription.put(MSegregated.NAME, MSegregated.DESC);
				mPatternDescription.put(RBoundedWith.NAME, RBoundedWith.DESC); 
			}
			
			return mPatternDescription;
	}
	
	@Override
	public boolean isAntiPattern() {
		return false;
	}

	public List<CompositeRule> getRules() {
		return mRules;
	}
	
	protected void setRules(List<CompositeRule> rules) {
		mRules = rules;
	}
	
	public void printOut() {
		
		for (CompositeRule rule : mRules) {
			Element elem = XMLRuleSerializer.serialize(rule, "Root");
			XMLOutputter outputter = new XMLOutputter();
			try {
			  outputter.output(elem, System.out);       
			}
			catch (IOException e) {
			  System.err.println(e);
			}
		}
	}

}
