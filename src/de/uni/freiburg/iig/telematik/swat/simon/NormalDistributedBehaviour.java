package de.uni.freiburg.iig.telematik.swat.simon;

import org.apache.commons.math3.distribution.NormalDistribution;


import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;



public class NormalDistributedBehaviour extends AbstractTimeBehaviour{
	
	public NormalDistributedBehaviour(double mean, double deviation){
		distribution=new NormalDistribution(mean, deviation);
		type=DistributionType.NORMAL;
		setParameterNames("mean","deviation");
	}

}
