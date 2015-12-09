package de.uni.freiburg.iig.telematik.swat.timeSimulation;

import java.util.LinkedList;
import java.util.List;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeContext;
import de.uni.freiburg.iig.telematik.swat.jascha.AwesomeResourceContext;
import de.uni.freiburg.iig.telematik.swat.jascha.CompoundResource;
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
			timeContext.addBehaviour("test3", ITimeBehaviourFactory.getBahaviour(DistributionType.NORMAL, params1));
			timeContext.addBehaviour("test4", ITimeBehaviourFactory.getBahaviour(DistributionType.LOG_NORMAL, params2));
		}
		return timeContext;
	}

	public static IResourceContext getResourceContext() {
		if (resourceContext == null) {
			 resourceContext = new AwesomeResourceContext();
			
			//Jascha
			resourceContext.addResourceUsage("Handwerkerarbeit", new SimpleResource("Handwerker"));
			List<IResource> werkzeuge = new LinkedList<IResource>();
			werkzeuge.add(new SimpleResource("Hammer"));
			werkzeuge.add(new SimpleResource("Kreuzschlitz_klein"));
			werkzeuge.add(new SimpleResource("Kreuzschlitz_gross"));
			IResource schraubenset = new CompoundResource("Schraubenset");
			werkzeuge.add(schraubenset);
			IResource werkzeugkasten = new CompoundResource("werkzeuge",werkzeuge);
			resourceContext.addResourceUsage("Handwerkerarbeit", werkzeugkasten);
			
			// Neue Ressource Waschmaschine wird gleichzeitig dem ResourceStore hinzugef�gt.
			resourceContext.addResourceUsage("Waschen", new SimpleResource("Waschmaschine", resourceContext.getResourceStore()));
			// Z�hlen der verf�gbaren H�mmer
			resourceContext.getResourceStore().countAvailable("Hammer");

			 IResource schrauebzieher1 = new SimpleResource("Schraubenzieher1", resourceContext.getResourceStore());
			 IResource schrauebzieher2 = new SimpleResource("Schraubenzieher2", resourceContext.getResourceStore());
			 IResource schrauebzieher3 = new SimpleResource("Schraubenzieher3", resourceContext.getResourceStore());
			 IResource schrauebzieher4 = new SimpleResource("Schraubenzieher4", resourceContext.getResourceStore());
			 IResource schrauebzieher5 = new SimpleResource("Schraubenzieher5", resourceContext.getResourceStore());
			 IResource schrauebzieher6 = new SimpleResource("Schraubenzieher6", resourceContext.getResourceStore());
			resourceContext.addResourceUsage("test", schrauebzieher1);
			resourceContext.addResourceUsage("test", schrauebzieher2);
			resourceContext.addResourceUsage("test", schrauebzieher3);
			//resourceContext.addResourceUsage("test2", schrauebzieher1);
			resourceContext.addResourceUsage("test2", schrauebzieher2);
			//resourceContext.addResourceUsage("test2", schrauebzieher3);
			resourceContext.addResourceUsage("test3", schrauebzieher1);
			resourceContext.addResourceUsage("test3", schrauebzieher2);
			//resourceContext.addResourceUsage("test3", schrauebzieher3);
			//resourceContext.addResourceUsage("test3", schrauebzieher6);
			resourceContext.addResourceUsage("test4", schrauebzieher4);
			resourceContext.addResourceUsage("test4", schrauebzieher5);
		}
		return resourceContext;
	}

}
