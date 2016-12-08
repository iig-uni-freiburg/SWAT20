package de.uni.freiburg.iig.telematik.swat.simon;

import org.apache.commons.math3.distribution.WeibullDistribution;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;


public class WeibullDistributedBehaviour  extends AbstractTimeBehaviour{
	
	public WeibullDistributedBehaviour(double alpha, double beta){
		distribution=new WeibullDistribution(alpha, beta);
		type=DistributionType.WEIBULL;
		setParameterNames("alpha","beta");
		setParameterValues(alpha, beta);
	}

}
