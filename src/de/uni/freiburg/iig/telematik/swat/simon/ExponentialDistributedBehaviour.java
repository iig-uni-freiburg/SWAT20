package de.uni.freiburg.iig.telematik.swat.simon;
import org.apache.commons.math3.distribution.ExponentialDistribution;
public class ExponentialDistributedBehaviour extends AbstractTimeBehaviour {

	public ExponentialDistributedBehaviour(double mean){
	distribution = new ExponentialDistribution(mean);
}
}