package de.uni.freiburg.iig.telematik.swat.simon;
import org.apache.commons.math3.distribution.LevyDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;;

public class LevyDistributedBehaviour extends AbstractTimeBehaviour {
	// vll müssen Änderungen an den Parametern vorgenommen werden
	public LevyDistributedBehaviour(double mean, double scale) {
		distribution = new LevyDistribution(null, mean, scale);
		type=DistributionType.LEVY;
		setParameterNames("mean","scale");
	}

}
