package de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions;

import java.util.List;


public class DistributionViewFactory {

	public static AbstractDistributionView getDistributionView(DistributionType type) {
		switch (type) {
		case NORMAL:
			return new NormalDistributionView();
		case LOG_NORMAL:
			return new LogNormalDistributionView();
		case GAMMA:
			return new GammaDistributionView();
		case EXPONENTIAL:
			return new ExponentialDistributionView();
		case BETA:
			return new BetaDistibutionView();
		case UNIFORM:
			return new UniformDistributionView();
		default:
			return null;
		}
	}

	public static AbstractDistributionView getDistributionView(double[] realTimeSamples) {
		return new MeasuredDistributionView(realTimeSamples, 100);
	}

	public static AbstractDistributionView getDistributionView(List<Double> realTimeSamples) {
		double[] result = new double[realTimeSamples.size()];
		int i = 0;
		for (double d : realTimeSamples) {
			result[i] = d;
			i++;
		}

		return getDistributionView(result);
	}

}
