package de.uni.freiburg.iig.telematik.swat.timeSimulation;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeContext;
import de.uni.freiburg.iig.telematik.swat.jascha.AwesomeResourceContext;
import de.uni.freiburg.iig.telematik.swat.jascha.Compound;
import de.uni.freiburg.iig.telematik.swat.jascha.CompoundResource;
import de.uni.freiburg.iig.telematik.swat.jascha.HumanResourceExtractor;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceSet;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceStore;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceType;
import de.uni.freiburg.iig.telematik.swat.jascha.SimpleResource;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.logs.SwatLogType;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;
import de.uni.freiburg.iig.telematik.swat.simon.AwesomeTimeContext;
import de.uni.freiburg.iig.telematik.swat.simon.ITimeBehaviourFactory;

public class ContextRepo {

	protected static AwesomeTimeContext timeContext;
	protected static AwesomeResourceContext resourceContext;

	public static ITimeContext getTimeContext() {
		if (timeContext == null) {
			timeContext = new AwesomeTimeContext();
			timeContext.setName("Example Time Context");
			LinkedList<Double> params1 = new LinkedList<>();
			params1.add(50.0);
			params1.add(2.0);
			LinkedList<Double> params2 = new LinkedList<>();
			params2.add(2.0);
			params2.add(0.3);
			LinkedList<Double> params3 = new LinkedList<>();
			params3.add(100.0);
			params3.add(5.0);
			timeContext.addBehaviour("test", ITimeBehaviourFactory.getBahaviour(DistributionType.NORMAL, params1));
			timeContext.addBehaviour("test2", ITimeBehaviourFactory.getBahaviour(DistributionType.LOG_NORMAL, params2));
			timeContext.addBehaviour("test3", ITimeBehaviourFactory.getBahaviour(DistributionType.NORMAL, params1));
			timeContext.addBehaviour("test4", ITimeBehaviourFactory.getBahaviour(DistributionType.LOG_NORMAL, params2));
			timeContext.addBehaviour("test5", ITimeBehaviourFactory.getBahaviour(DistributionType.NORMAL, params3));
			timeContext.setDeadline("test", 1234);
			timeContext.setDeadline("test2", 10);
		}
		return timeContext;
	}

	public static IResourceContext getResourceContext() {
		if (resourceContext == null) {
			 resourceContext = new AwesomeResourceContext();
			 resourceContext.setName("TestRepoContext");
			 
			 ResourceStore store = resourceContext.getResourceStore();
			 
			 /**
			  * LogfileResourceExtractor-Test
			  * Hier den Dateipfad anpassen
			  */
			
			//LogModel testModel = new LogModel(new File("/D:/Uni/MASTER/MASTERPROJEKT/workspace/smallLog.mxml"), SwatLogType.MXML);
			LogModel testModel = new LogModel(new File("/D:/Uni/MASTER/MASTERPROJEKT/workspace/P2P-log-v6-anonymized.mxml"), SwatLogType.MXML);
			//store.addHumanResourcesFromFile(testModel);
			try {
				resourceContext.getResourcesFromFile(testModel);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//resourceContext.addResourceUsage("Handwerkerarbeit", new SimpleResource("Handwerker",store));
			List<IResource> werkzeuge = new LinkedList<IResource>();
			werkzeuge.add(store.instantiateResource(ResourceType.SIMPLE, "Hammer"));
			werkzeuge.add(store.instantiateResource(ResourceType.SIMPLE, "KreuzschlitzKL"));
			werkzeuge.add(store.instantiateResource(ResourceType.SIMPLE, "KreuzschlitzGR"));
			IResource schraubenset = store.instantiateResource(ResourceType.COMPOUND, "Werkzeuge", werkzeuge);
			werkzeuge.add(schraubenset);
			//IResource werkzeugkasten = new CompoundResource("werkzeuge",werkzeuge);
			IResource hammer = werkzeuge.get(0);
			resourceContext.addResourceUsage("Handwerkerarbeit", hammer);			
			IResource rsTest1 = resourceContext.getResourceStore().instantiateResource(ResourceType.SIMPLE, "Zange");
			IResource rsTest2 = resourceContext.getResourceStore().instantiateResource(ResourceType.SHARED, "Waschbecken");
			IResource rsTest3 = resourceContext.getResourceStore().instantiateResource(ResourceType.SIMPLE, "Wasserhahn");
			resourceContext.getResourceStore().instantiateResource(ResourceType.COMPOUND,"werkzeuge",werkzeuge);
			
			resourceContext.getResourceStore().instantiateResource(ResourceType.HUMAN, "Flaschner-1");
			resourceContext.getResourceStore().instantiateResource(ResourceType.HUMAN, "Maurer-1");
			resourceContext.getResourceStore().instantiateResource(ResourceType.HUMAN, "Kassierer-1");
			resourceContext.getResourceStore().instantiateResource(ResourceType.HUMAN, "Hausmeister");
			
			//Test fuer ResourceSets und SimpleResources. Hausmeister kommt in zwei Sets vor, Loeschverhalten wie gewuenscht.
			IResource handerwerkerSet = resourceContext.getResourceStore().instantiateResource(ResourceType.SET, "Handwerker", 5);
			((ResourceSet)handerwerkerSet).addResource(resourceContext.getResourceStore().getResource("Flaschner-1"));
			((ResourceSet)handerwerkerSet).addResource(resourceContext.getResourceStore().getResource("Hausmeister"));			
			IResource mechanikerSet = resourceContext.getResourceStore().instantiateResource(ResourceType.SET, "Mechaniker", 5);
			((ResourceSet)mechanikerSet).addResource(resourceContext.getResourceStore().getResource("Hausmeister"));
			

			 IResource schrauebzieher1 = new SimpleResource("Schraubenzieher1", resourceContext.getResourceStore());
			 IResource schrauebzieher2 = new SimpleResource("Schraubenzieher2", resourceContext.getResourceStore());
			 IResource schrauebzieher3 = new SimpleResource("Schraubenzieher3", resourceContext.getResourceStore());
			 IResource schrauebzieher4 = new SimpleResource("Schraubenzieher4", resourceContext.getResourceStore());
			 IResource schrauebzieher5 = new SimpleResource("Schraubenzieher5", resourceContext.getResourceStore());
			 IResource schrauebzieher6 = new SimpleResource("Schraubenzieher6", resourceContext.getResourceStore());
			resourceContext.addResourceUsage("test", schrauebzieher1);
			resourceContext.addResourceUsage("test", schrauebzieher2);
			resourceContext.addResourceUsage("test", schrauebzieher3);
			resourceContext.addResourceUsage("test2", schrauebzieher1);
			resourceContext.addResourceUsage("test2", schrauebzieher2);
			resourceContext.addResourceUsage("test2", schrauebzieher3);
			resourceContext.addResourceUsage("test3", schrauebzieher1);
			resourceContext.addResourceUsage("test3", schrauebzieher2);
			resourceContext.addResourceUsage("test3", schrauebzieher3);
			resourceContext.addResourceUsage("test3", schrauebzieher6);
			resourceContext.addResourceUsage("test4", schrauebzieher4);
			resourceContext.addResourceUsage("test4", schrauebzieher5);
			resourceContext.addResourceUsage("test5", schrauebzieher5);
			resourceContext.addResourceUsage("test5", schrauebzieher1);
			resourceContext.addResourceUsage("test5", schrauebzieher3);
			
			for (String activity: resourceContext.getAllActivities()){
				System.out.println(activity);
				System.out.println(resourceContext.getAllowedResourcesFor(activity));
			}
		}
		return resourceContext;
	}

}
