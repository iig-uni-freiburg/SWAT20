package de.uni.freiburg.iig.telematik.swat.simon;
import org.apache.commons.math3.distribution.PoissonDistribution;

public class PoissonDistributedBehaviour extends AbstractTimeBehaviour {

	PoissonDistribution poissonDistribution;
	
	
	//lambda is the expected value
	public PoissonDistributedBehaviour(double lambda) {
		poissonDistribution = new PoissonDistribution(lambda);
	}
	
	public double getNeededTime() {
		return poissonDistribution.sample();
	}
}
