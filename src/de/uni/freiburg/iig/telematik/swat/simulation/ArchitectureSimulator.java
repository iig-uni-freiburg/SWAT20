package de.uni.freiburg.iig.telematik.swat.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeContext;

public class ArchitectureSimulator {
	
	private List<TimedNet> nets = new ArrayList<>();
	
	private IResourceContext resContext;
	
	private ITimeContext timeContext;
	
	public void simulateOneStep(){
		TimedNet net = getRandomNet();
		try {
			net.fire();
		} catch (PNException e) {
		}	
	}
	
	public void simulateComplete(){
		while(moreToSimulate()){
			simulateOneStep();
		}
	}
	
	private TimedNet getRandomNet(){
		int max = nets.size();
		int index = ThreadLocalRandom.current().nextInt(0, max);
		return nets.get(index);
	}
	
	private boolean moreToSimulate(){
		for (TimedNet net:nets){
			if(net.moreToSimulate())
				return true;
		}
		return false;
	}
	
	public void setResourceContext(IResourceContext context){
		this.resContext=context;
		for (TimedNet net:nets)
			net.setResourceContext(resContext);
	}
	
	public void setTimeContext(ITimeContext context){
		this.timeContext=context;
		for(TimedNet net:nets)
			net.setTimeContext(timeContext);
	}
	
	public void reset(){
		for (TimedNet net:nets)
			net.reset();
	}

}
