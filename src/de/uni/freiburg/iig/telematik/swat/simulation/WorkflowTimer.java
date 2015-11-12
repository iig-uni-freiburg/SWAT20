package de.uni.freiburg.iig.telematik.swat.simulation;

import java.util.ArrayList;
import java.util.TreeMap;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.NetConstraint;

public class WorkflowTimer {
	
	double time;
	//TreeMap<Double, NetConstraint> pendingNets = new TreeMap<>();
	TreeMap<Double, String> pendingNets = new TreeMap<>(); //which net is the next to do something
	ArrayList<TimedNet> nets = new ArrayList<>();

}
