package de.uni.freiburg.iig.telematik.swat.simon;



import org.apache.commons.math3.distribution.UniformRealDistribution;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;


public class UniformDistributedBehaviour extends AbstractTimeBehaviour{
	
	public UniformDistributedBehaviour(double lower, double upper){
		distribution=new UniformRealDistribution(lower, upper);
		type=DistributionType.UNIFORM;
		setParameterNames("lower","upper");
	}
}
