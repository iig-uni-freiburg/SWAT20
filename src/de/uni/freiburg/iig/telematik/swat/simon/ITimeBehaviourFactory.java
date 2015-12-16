package de.uni.freiburg.iig.telematik.swat.simon;

import java.util.LinkedList;
import java.util.List;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;

public class ITimeBehaviourFactory {

	public static void main(String args[]) {
		// Nutzung:
		LinkedList<Double> parameter = new LinkedList<>();
		parameter.add(70.0);
		parameter.add(3.4);
		ITimeBehaviour test = ITimeBehaviourFactory.getBahaviour(DistributionType.NORMAL, parameter);
		for (int i = 0; i < 10; i++)
			System.out.println("Get needed time: " + test.getNeededTime());
		InversionMethodLogReader reader = new InversionMethodLogReader();
		reader.inversionMethod(reader.probabilityTimeDiagram(reader.createHistogram("C:/Users/Schonhart/Desktop/BachelorThesis/Logfiles/BafterA.mxml", "A")));
		
		//reader.parseLog("C:/Users/Schonhart/Desktop/BachelorThesis/Logfiles/BafterA.mxml");
	}

	public static ITimeBehaviour getBahaviour(DistributionType type, List<Double> params) {
		switch (type) {
		case NORMAL:
			return new NormalDistributedBehaviour(params.get(0), params.get(1));
		case LOG_NORMAL:
			return new LogNormalDistributedBavahiour(params.get(0), params.get(1));
		case CAUCHY:
			return new CauchyDistributedBehaviour(params.get(0), params.get(1));
		case EXPONENTIAL:
			return new ExponentialDistributedBehaviour(params.get(0));
		case GAMMA:
			return new GammaDistributedBehaviour(params.get(0), params.get(1));
		case POISSON:
			return new PoissonDistributedBehaviour(params.get(0));
		case LEVY:
			return new LevyDistributedBehaviour(params.get(0), params.get(1));
		case BETA:
			return new BetaDistributedBehaviour(params.get(0), params.get(1));
		case F:
			return new FDistributedBehaviour(params.get(0), params.get(1));
		default:
			break;
		}
		return null;
	}
	
	public static ITimeBehaviour getBehaviour(String path, String activity) {
		InversionMethodLogReader log = new InversionMethodLogReader();
		return new MeasuredTimeBahviour(log.probabilityTimeDiagram(log.createHistogram(path, activity)));
	}

}
