package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.ModelInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;

public abstract class CompliancePattern implements Comparable<CompliancePattern> {
    private ArrayList<String> counterExample;
	
	private enum State {
		NOT_INSTANTIATED, INSTANTIATED
	}
	
	protected boolean mSatisfied;
	
	protected double mProb = -1;
	
	private State mState = State.NOT_INSTANTIATED;
	
	protected ArrayList<Parameter> mParameters = new ArrayList<>();
	
	@XStreamOmitField
	protected ModelInfoProvider mInfoProvider;

	@XStreamOmitField
	protected Object mFormalization;
	
	public abstract void acceptInfoProfider(ModelInfoProvider provider);
	
	public abstract String getName();
	
	public abstract String getDescription();
	
	@XStreamOmitField
	protected boolean loadedFromDisk = false;

	public boolean isLoadedFromDisk() {
		return loadedFromDisk;
	}

	public void setLoadedFromDisk(boolean loadedFromDisk) {
		this.loadedFromDisk = loadedFromDisk;
	}

	public ArrayList<Parameter> getParameters() {
		return mParameters;
	}
	
	public void instantiate() {
		mState = State.INSTANTIATED;
	}
	
	public boolean isInstantiated() {
		return (mState == State.INSTANTIATED);
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
		return mFormalization;
	}


	public abstract void setFormalization();

	public abstract boolean isAntiPattern();

	public void setCounterExample(ArrayList<String> path) {
		this.counterExample=path;	
	}
        
        public ArrayList<String> getCounterExample(){
            return counterExample;
        }

	@Override
	public int compareTo(CompliancePattern o) {
		return getName().compareTo(o.getName());
	}

}
