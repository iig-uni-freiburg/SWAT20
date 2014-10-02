package de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui.distributions;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.BetaDistribution;

public class BetaDistibutionView extends AbstractDistributionView {

	public BetaDistibutionView() {
		setParamNames("alpha", "beta");
		setType(DistributionType.BETA);
	}

	@Override
	public AbstractRealDistribution getDistribution() {
		return new BetaDistribution(params[0], params[1]);
	}

}
