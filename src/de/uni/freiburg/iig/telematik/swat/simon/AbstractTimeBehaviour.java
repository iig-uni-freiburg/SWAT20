package de.uni.freiburg.iig.telematik.swat.simon;

import java.text.DecimalFormat;

import org.apache.commons.math3.distribution.AbstractRealDistribution;


import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;

public abstract class AbstractTimeBehaviour implements ITimeBehaviour {
	
	AbstractRealDistribution distribution;
	boolean available =true;
	DistributionType type = DistributionType.UNKNOWN;
	DecimalFormat format = new DecimalFormat("##.##");
	String[] parameterNames;

	@Override
	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return available;
	}

	@Override
	public void setAvailability(boolean isAvailable) {
		this.available=isAvailable;

	}

	@Override
	public double getNeededTime() {
		return distribution.sample();
	}
	
	public DistributionType getType(){
		return type;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(type.toString());
		try{
			sb.append("("+format.format(distribution.getNumericalMean()));
			sb.append(", "+format.format(distribution.getNumericalVariance())+")");
		} catch (Exception e){
			//ignore parameters
		}
		
		return sb.toString();
	}
	
	protected void setParameterNames(String... names){
		parameterNames=names;
	}
	
	public int getNumberOfParameters(){
		return parameterNames.length;
	}
	
	public String getParameter(int index){
		return parameterNames[index];
	}
	
	public String[] getParameters(){
		return parameterNames;
	}

	}




