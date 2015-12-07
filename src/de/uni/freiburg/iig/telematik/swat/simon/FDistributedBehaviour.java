package de.uni.freiburg.iig.telematik.swat.simon;
import org.apache.commons.math3.distribution.FDistribution;
public class FDistributedBehaviour extends AbstractTimeBehaviour{

	public FDistributedBehaviour(double numeratorDegreesOfFreedom, double denominatorDegreesOfFreedom) {
		distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
	}
}
