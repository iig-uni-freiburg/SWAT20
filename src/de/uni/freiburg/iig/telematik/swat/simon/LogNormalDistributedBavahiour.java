
package de.uni.freiburg.iig.telematik.swat.simon;

import org.apache.commons.math3.distribution.LogNormalDistribution;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;

public class LogNormalDistributedBavahiour extends AbstractTimeBehaviour {
	
	public LogNormalDistributedBavahiour (double scale, double shape){
		distribution=new LogNormalDistribution(scale, shape);
		type = DistributionType.LOG_NORMAL;
		setParameterNames("scale","shape");
	}

}
