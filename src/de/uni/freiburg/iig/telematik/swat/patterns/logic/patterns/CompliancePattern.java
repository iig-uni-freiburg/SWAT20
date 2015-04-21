package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns;

import java.util.ArrayList;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.processmining.analysis.sciffchecker.logic.model.rule.CompositeRule;
import org.processmining.analysis.sciffchecker.logic.xml.XMLRuleSerializer;

import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.ModelInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;

public abstract class CompliancePattern {
	
	private enum State {
		NOT_INSTANTIATED, INSTANTIATED
	}
	
	protected boolean mSatisfied;
	
	protected double mProb = -1;
	
	private State mState = State.NOT_INSTANTIATED;
	
	protected ArrayList<Parameter> mParameters = new ArrayList<Parameter>();
	
	protected ModelInfoProvider mInfoProvider;

	protected Object mFormalization;
	
	public abstract void acceptInfoProfider(ModelInfoProvider provider);
	
	public abstract String getName();
	
	public abstract String getDescription();
	
	public ArrayList<Parameter> getParameters() {
		return mParameters;
	}
	
	public void instantiate() {
		mState = State.INSTANTIATED;
	}
	
	public boolean isInstantiated() {
		return (mState == State.INSTANTIATED)? true : false;
	}

	public abstract CompliancePattern duplicate();

	public void notInstantiated() {
		mState = State.NOT_INSTANTIATED;
		
	}
	
	public boolean isSatisfied() {
		return mSatisfied;
	}
	
	public void setSatisfied(boolean sat) {
		mSatisfied = sat;
	}
	
	public double getProbability() {
		return mProb;
	}
	
	public void setProbability(double prob) {
		mProb = prob;
	}
	
	public Object getFormalization() {
		setFormalization();
		printRule();
		return mFormalization;
	}

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

	protected abstract void setFormalization();

	public abstract boolean isAntiPattern();

	public void setCounterExample(ArrayList<String> path) {
		// TODO Auto-generated method stub
		
	}

}
