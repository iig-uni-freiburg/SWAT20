package de.uni.freiburg.iig.telematik.swat.simon;

import org.apache.commons.math3.distribution.AbstractRealDistribution;


import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;

public abstract class AbstractTimeBehaviour implements ITimeBehaviour {
	
	AbstractRealDistribution distribution;
	boolean available =true;
	DistributionType type;

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

	}




