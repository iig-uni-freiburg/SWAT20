package de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui.distributions;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;

public class LogNormalDistributionView extends AbstractDistributionView {

	public LogNormalDistributionView() {
		setParamNames("scale", "shape");
		setType(DistributionType.LOG_NORMAL);
	}

	//	@Override
	//	public String toString() {
	//		return "Log-Normal distributed";
	//	}

	@Override
	public AbstractRealDistribution getDistribution() {
		return new LogNormalDistribution(params[0], params[1]);
	}
	


}
