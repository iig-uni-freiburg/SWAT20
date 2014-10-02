package de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui.distributions;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.GammaDistribution;

public class GammaDistributionView extends NormalDistributionView implements IDistributionView {

	public GammaDistributionView() {
		setParamNames("shape", "scale");
		setType(DistributionType.GAMMA);
	}

	@Override
	public AbstractRealDistribution getDistribution() {
		return new GammaDistribution(params[0], params[1]);
	}

}
