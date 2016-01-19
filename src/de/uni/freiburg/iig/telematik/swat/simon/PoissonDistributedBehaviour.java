package de.uni.freiburg.iig.telematik.swat.simon;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;

public class PoissonDistributedBehaviour extends AbstractTimeBehaviour {

	PoissonDistribution distribution;
	
	
	//lambda is the expected value
	public PoissonDistributedBehaviour(double lambda) {
		
		distribution = new PoissonDistribution(lambda);
		type=DistributionType.POISSON;
	}
	
	public double getNeededTime() {
		return distribution.sample();
	}
}
