package de.uni.freiburg.iig.telematik.swat.timeSimulation;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeContext;

public class TimedNetRep {
	
	public static TimedNet getSimpleLinearTimedNet(IResourceContext resContext, ITimeContext timeContext){
		TimedNet net = new TimedNet();
		net.addTransition("test");
		net.addTransition("test2");
		net.addPlace("start");
		net.getPlace("start").setState(1);
		net.addPlace("place2");
		net.addPlace("end");
		net.addFlowRelationPT("start", "test");
		net.addFlowRelationTP("test", "place2");
		net.addFlowRelationPT("place2", "test2");
		net.addFlowRelationTP("test2", "end");
		net.setResourceContext(resContext);
		net.setTimeContext(timeContext);
		net.setInitialMarking(net.getMarking());
		return net;
	}
	
	public static TimedNet getSimpleORTimedNet(IResourceContext resContext, ITimeContext timeContext){
		TimedNet net = new TimedNet();
		net.addTransition("test");
		net.addTransition("test2");
		net.addPlace("start");
		net.getPlace("start").setState(1);
		net.addPlace("end");
		net.addFlowRelationPT("start", "test");
		net.addFlowRelationTP("test", "end");
		net.addFlowRelationPT("start", "test2");
		net.addFlowRelationTP("test2", "end");
		net.setResourceContext(resContext);
		net.setTimeContext(timeContext);
		net.setInitialMarking(net.getMarking());
		return net;
	}
	
	public static TimedNet getSimpleANDTimedNet(IResourceContext resContext, ITimeContext timeContext){
		TimedNet net = new TimedNet();
		net.addTransition("test");
		net.addTransition("test2");
		net.addTransition("silent");
		net.addTransition("silent2");
		net.addPlace("start");
		net.getPlace("start").setState(1);
		net.addPlace("end");
		net.addPlace("p1");
		net.addPlace("p2");
		net.addPlace("p3");
		net.addPlace("p4");
		net.addFlowRelationPT("start", "silent");
		net.addFlowRelationTP("silent", "p1");
		net.addFlowRelationTP("silent", "p2");
		net.addFlowRelationPT("p1", "test");
		net.addFlowRelationPT("p2", "test2");
		net.addFlowRelationTP("test", "p3");
		net.addFlowRelationTP("test2", "p4");
		net.addFlowRelationPT("p3", "silent2");
		net.addFlowRelationPT("p4", "silent2");
		net.addFlowRelationTP("silent2", "end");
		net.setResourceContext(resContext);
		net.setTimeContext(timeContext);
		net.setInitialMarking(net.getMarking());
		return net;
	}

}
