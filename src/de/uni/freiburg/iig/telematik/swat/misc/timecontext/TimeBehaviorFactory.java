package de.uni.freiburg.iig.telematik.swat.misc.timecontext;

import java.util.List;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.distribution.WeibullDistribution;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui.distributions.DistributionType;

public abstract class TimeBehaviorFactory {

	public static TimeBehavior getTimeBehavior(AbstractRealDistribution distribution) {
		StochasticTimeBehavior behavior = new StochasticTimeBehavior(distribution);
		behavior.setDistributionType(getType(distribution));
		return behavior;
	}

	private static DistributionType getType(AbstractRealDistribution distribution) {

		if (distribution instanceof NormalDistribution)
			return DistributionType.NORMAL;
		else if (distribution instanceof LogNormalDistribution)
			return DistributionType.LOG_NORMAL;
		else if (distribution instanceof GammaDistribution)
			return DistributionType.GAMMA;
		else if (distribution instanceof WeibullDistribution)
			return DistributionType.WEIBULL;
		else if (distribution instanceof UniformRealDistribution)
			return DistributionType.UNIFORM;

		return DistributionType.UNKNOWN;
	}

	public static TimeBehavior getTimeBehavior(double[] realTimeSamples) {
		//test if realTimeSamples matches a common distribution

		//if none matches suficient, return measuredBehavio
		return new MeasuredTimedActivity(realTimeSamples, 100);

	}

	public static TimeBehavior getTimeBehavior(List<Double> timing) {
		double[] resultArray = new double[timing.size()];
		int i = 0;
		for (double d : timing) {
			resultArray[i] = d;
			i++;
		}

		return getTimeBehavior((double[]) resultArray);
	}

}
