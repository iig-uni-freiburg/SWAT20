package de.uni.freiburg.iig.telematik.swat.simon;

import org.apache.commons.math3.distribution.BetaDistribution;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;

public class BetaDistributedBehaviour extends AbstractTimeBehaviour{

	public BetaDistributedBehaviour(double mean, double scale) {
		distribution = new BetaDistribution(mean, scale);
		type=DistributionType.BETA;
		setParameterNames("mean","scale");
	}
}
