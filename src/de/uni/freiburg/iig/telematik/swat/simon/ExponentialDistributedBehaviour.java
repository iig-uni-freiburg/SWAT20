package de.uni.freiburg.iig.telematik.swat.simon;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;
public class ExponentialDistributedBehaviour extends AbstractTimeBehaviour {

	public ExponentialDistributedBehaviour(double mean){
	distribution = new ExponentialDistribution(mean);
	type=DistributionType.EXPONENTIAL;
}
}