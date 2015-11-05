package de.uni.freiburg.iig.telematik.swat.simon;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;

public class NormalDistributedBehaviour extends AbstractTimeBehaviour{
	
	public NormalDistributedBehaviour(double mean, double sd){
		distribution=new NormalDistribution(mean, sd);
	}

}
