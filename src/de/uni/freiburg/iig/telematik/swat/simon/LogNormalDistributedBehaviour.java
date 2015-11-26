package de.uni.freiburg.iig.telematik.swat.simon;

import org.apache.commons.math3.distribution.LogNormalDistribution;

public class LogNormalDistributedBehaviour extends AbstractTimeBehaviour {

		public LogNormalDistributedBehaviour(double scale, double shape){
		distribution = new LogNormalDistribution(scale, shape);
	}
}
