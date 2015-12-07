package de.uni.freiburg.iig.telematik.swat.simon;
import org.apache.commons.math3.distribution.LevyDistribution;;

public class LevyDistributedBehaviour extends AbstractTimeBehaviour {
	// vll m�ssen �nderungen an den Parametern vorgenommen werden
	public LevyDistributedBehaviour(double mean, double scale) {
		distribution = new LevyDistribution(null, mean, scale);
	}

}
