package de.uni.freiburg.iig.telematik.swat.simon;
import org.apache.commons.math3.*;

import org.apache.commons.math3.distribution.NormalDistribution;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;

public class TimeObject implements ITimeBehaviour{
 
	private AbstractTimeBehaviour mTimeBehaviour;
 
	public TimeObject(AbstractTimeBehaviour timeBehaviour) {
		mTimeBehaviour = timeBehaviour;
	}
	@Override
	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setAvailability(boolean isAvailable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getNeededTime() {
		// TODO Auto-generated method stub
		return mTimeBehaviour.getNeededTime();
	}
	
	

}
