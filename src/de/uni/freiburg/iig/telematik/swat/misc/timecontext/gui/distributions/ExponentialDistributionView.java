package de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui.distributions;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.ExponentialDistribution;

public class ExponentialDistributionView extends AbstractDistributionView {

	public ExponentialDistributionView() {
		setParamNames("mean");
		setType(DistributionType.EXPONENTIAL);
	}

	@Override
	public AbstractRealDistribution getDistribution() {
		return new ExponentialDistribution(params[0]);
	}


}
