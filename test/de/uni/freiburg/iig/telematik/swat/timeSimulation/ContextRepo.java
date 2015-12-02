package de.uni.freiburg.iig.telematik.swat.timeSimulation;

import java.util.LinkedList;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeContext;
import de.uni.freiburg.iig.telematik.swat.jascha.AwesomeResourceContext;
import de.uni.freiburg.iig.telematik.swat.jascha.SimpleResource;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;
import de.uni.freiburg.iig.telematik.swat.simon.AwesomeTimeContext;
import de.uni.freiburg.iig.telematik.swat.simon.ITimeBehaviourFactory;

public class ContextRepo {

	protected static AwesomeTimeContext timeContext;
	protected static AwesomeResourceContext resourceContext;

	public static ITimeContext getTimeContext() {
		if (timeContext == null) {
			timeContext = new AwesomeTimeContext();
			LinkedList<Double> params1 = new LinkedList<>();
			params1.add(50.0);
			params1.add(2.0);
			LinkedList<Double> params2 = new LinkedList<>();
			params2.add(2.0);
			params2.add(0.3);
			timeContext.addBehaviour("test", ITimeBehaviourFactory.getBahaviour(DistributionType.NORMAL, params1));
			timeContext.addBehaviour("test2", ITimeBehaviourFactory.getBahaviour(DistributionType.LOG_NORMAL, params2));
		}
		return timeContext;
	}

	public static IResourceContext getResourceContext() {
		if (resourceContext == null) {
			 resourceContext = new AwesomeResourceContext();
			 IResource schrauebzieher1 = new SimpleResource("Schraubenzieher1");
			 IResource schrauebzieher2 = new SimpleResource("Schraubenzieher2");
			resourceContext.addResourceUsage("test", schrauebzieher1);
			resourceContext.addResourceUsage("test", schrauebzieher2);
			resourceContext.addResourceUsage("test2", schrauebzieher1);
			resourceContext.addResourceUsage("test2", schrauebzieher2);
		}
		return resourceContext;
	}

}
