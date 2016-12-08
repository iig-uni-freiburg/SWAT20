package de.uni.freiburg.iig.telematik.swat.simon;
import org.apache.commons.math3.distribution.BinomialDistribution;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;;
public class BinomialDistributedBehaviour extends AbstractTimeBehaviour{
	
	private BinomialDistribution distribution;
	
	public BinomialDistributedBehaviour(double trials, double p) {
		distribution = new BinomialDistribution((int)trials, p);
		type=DistributionType.BINOMIAL;
		setParameterNames("trials","p");
		setParameterValues(trials, p);
	}
	
	public double getNeededTime() {
		return distribution.sample();
	}
}
