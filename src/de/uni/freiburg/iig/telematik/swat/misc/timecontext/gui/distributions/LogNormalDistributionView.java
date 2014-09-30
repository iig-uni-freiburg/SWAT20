package de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui.distributions;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;

public class LogNormalDistributionView extends NormalDistributionView implements IDistributionView {

	@Override
	public String toString() {
		return "Log-Normal distributed";
	}

	@Override
	public AbstractRealDistribution getDistribution() {
		return new LogNormalDistribution(Double.parseDouble(mean.getText()), Double.parseDouble(sd.getText()));
	}

}
