package de.uni.freiburg.iig.telematik.swat.simon;

import org.apache.commons.math3.distribution.CauchyDistribution;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;

public class CauchyDistributedBehaviour extends AbstractTimeBehaviour {

	public CauchyDistributedBehaviour(double median, double scale) {
		distribution = new CauchyDistribution(median, scale);
	}
}