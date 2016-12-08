package de.uni.freiburg.iig.telematik.swat.simon;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;
public class FDistributedBehaviour extends AbstractTimeBehaviour{

	public FDistributedBehaviour(double numeratorDegreesOfFreedom, double denominatorDegreesOfFreedom) {
		distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
		type=DistributionType.F;
		setParameterNames("numeratorDegreesOfFreedom","denominatorDegreesOfFreedom");
		setParameterValues(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
	}
}
