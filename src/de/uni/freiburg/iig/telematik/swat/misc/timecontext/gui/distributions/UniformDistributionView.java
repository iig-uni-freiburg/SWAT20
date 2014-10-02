package de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui.distributions;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;

public class UniformDistributionView extends AbstractDistributionView {

	public UniformDistributionView() {
		setParamNames("low", "high");
		setType(DistributionType.UNIFORM);
	}

	@Override
	public AbstractRealDistribution getDistribution() {
		return new UniformRealDistribution(params[0], params[1]);
	}

}
