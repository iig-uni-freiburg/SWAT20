package de.uni.freiburg.iig.telematik.swat.simon;

import org.apache.commons.math3.distribution.CauchyDistribution;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;

public class CauchyDistributedBehaviour extends AbstractTimeBehaviour {

	public CauchyDistributedBehaviour(double median, double scale) {
		distribution = new CauchyDistribution(median, scale);
		type=DistributionType.CAUCHY;
	}
}
