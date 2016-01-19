package de.uni.freiburg.iig.telematik.swat.simon;
import org.apache.commons.math3.distribution.BinomialDistribution;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;;
public class BinomialDistributedBehaviour extends AbstractTimeBehaviour{
	
	private BinomialDistribution distribution;
	
	public BinomialDistributedBehaviour(int trials, double p) {
		distribution = new BinomialDistribution(trials, p);
		type=DistributionType.BINOMIAL;
	}
	
	public double getNeededTime() {
		return distribution.sample();
	}
}
