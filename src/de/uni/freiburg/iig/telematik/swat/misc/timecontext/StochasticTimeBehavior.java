package de.uni.freiburg.iig.telematik.swat.misc.timecontext;

import org.apache.commons.math3.distribution.AbstractRealDistribution;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;

public class StochasticTimeBehavior implements TimeBehavior {

	AbstractRealDistribution distribution;
	DistributionType type;
	boolean isInUse = false;

	public StochasticTimeBehavior(AbstractRealDistribution distribution) {
		this.distribution = distribution;
	}

	@Override
	public double getNeededTime() {
		return distribution.sample();
	}

	public AbstractRealDistribution getDistribution() {
		return distribution;
	}

	public DistributionType getDistributionType() {
		return type;
	}

	public void setDistributionType(DistributionType type) {
		this.type = type;
	}

	@Override
	public boolean isInUse() {
		return isInUse;
	}

	@Override
	public void setIsInUse(boolean isInUse) {
		this.isInUse = isInUse;
	}

}
