package de.uni.freiburg.iig.telematik.swat.simon;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;

public class ITimeBehaviourFactory {

	public static void main(String args[]) throws IOException, ParserException {
		// Nutzung:
		LinkedList<Double> parameter = new LinkedList<>();
		parameter.add(70.0);
		parameter.add(3.4);
		ITimeBehaviour test = ITimeBehaviourFactory.getBahaviour(DistributionType.NORMAL, parameter);
		for (int i = 0; i < 10; i++)
			System.out.println("Get needed time: " + test.getNeededTime());
		
		
		ITimeBehaviour measuredBehaviour = getBehaviour("C:/Users/Schonhart/Desktop/BachelorThesis/Logfiles/BNA.mxml", "A");
		for (int i = 0; i<100;i++){
			System.out.println("time: " + measuredBehaviour.getNeededTime());
		}
		

		
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
		case WEIBULL:
			return new WeibullDistributedBehaviour(params.get(0), params.get(1));
		case UNIFORM:
			if (params.get(0) < params.get(1)) {
				return new UniformDistributedBehaviour(params.get(0), params.get(1));
			} else
				throw new ParameterException("First parameter must be smaller than second parameter");
		case BINOMIAL:
			if (params.get(0) >= 0 && params.get(0) <= 1 && params.get(1) >= 0 && params.get(1) <= 1) {
				return new BinomialDistributedBehaviour(params.get(0), params.get(1));
			} else {
				throw new ParameterException("Parameters must be between 0 and 1");
			}
		default:
			break;
		}
		return null;
	}
	

	

	public static ITimeBehaviour getBehaviour(String path, String activity) throws IOException, ParserException{
		InversionMethodLogReader reader = new InversionMethodLogReader(path);
		return new MeasuredTimeBehaviour(reader.probabilityTimeDiagram(reader.createHistogram(activity)));

	}

}
