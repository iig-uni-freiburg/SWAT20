package de.uni.freiburg.iig.telematik.swat.simon;
import org.apache.commons.math3.distribution.GammaDistribution;;
public class GammaDistributedBehaviour extends AbstractTimeBehaviour{

	
	public GammaDistributedBehaviour (double shape, double scale) {
		distribution = new GammaDistribution(shape, scale);
	}
}
