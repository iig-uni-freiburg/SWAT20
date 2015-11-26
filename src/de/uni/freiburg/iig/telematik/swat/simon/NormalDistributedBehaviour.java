package de.uni.freiburg.iig.telematik.swat.simon;

import org.apache.commons.math3.distribution.NormalDistribution;


import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;



public class NormalDistributedBehaviour extends AbstractTimeBehaviour{
	
	public NormalDistributedBehaviour(double mean, double sd){
		distribution=new NormalDistribution(mean, sd);
		type=DistributionType.LOG_NORMAL;
	}

}
