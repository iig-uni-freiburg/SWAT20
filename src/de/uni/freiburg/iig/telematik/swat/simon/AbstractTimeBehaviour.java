package de.uni.freiburg.iig.telematik.swat.simon;

import java.text.DecimalFormat;

import org.apache.commons.math3.distribution.AbstractRealDistribution;


import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;

public abstract class AbstractTimeBehaviour implements ITimeBehaviour {
	
	AbstractRealDistribution distribution;
	boolean available =true;
	DistributionType type;
	DecimalFormat format = new DecimalFormat("##.##");

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

	}




