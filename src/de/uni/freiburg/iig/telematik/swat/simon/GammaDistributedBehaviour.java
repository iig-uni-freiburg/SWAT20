package de.uni.freiburg.iig.telematik.swat.simon;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;;
public class GammaDistributedBehaviour extends AbstractTimeBehaviour{

	
	public GammaDistributedBehaviour (double shape, double scale) {
		distribution = new GammaDistribution(shape, scale);
		type=DistributionType.GAMMA;
		setParameterNames("shape","scale");
	}
}
