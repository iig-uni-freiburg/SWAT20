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
	}

	public static ITimeBehaviour getBahaviour(DistributionType type, List<Double> params) {
		switch (type) {
		case NORMAL:
			return new NormalDistributedBehaviour(params.get(0), params.get(1));
		case LOG_NORMAL:
			return new LogNormalDistributedBavahiour(params.get(0), params.get(1));

		default:
			break;
		}
		return null;
	}

}
