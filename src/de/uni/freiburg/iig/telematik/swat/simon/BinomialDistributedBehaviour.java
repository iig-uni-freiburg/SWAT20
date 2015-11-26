package de.uni.freiburg.iig.telematik.swat.simon;
import org.apache.commons.math3.distribution.BinomialDistribution;;
public class BinomialDistributedBehaviour extends AbstractTimeBehaviour{
	
	private BinomialDistribution binomialDistribution;
	
	public BinomialDistributedBehaviour(int trials, double p) {
		binomialDistribution = new BinomialDistribution(trials, p);
	}
	
	public double getNeededTime() {
		return binomialDistribution.sample();
	}
}
