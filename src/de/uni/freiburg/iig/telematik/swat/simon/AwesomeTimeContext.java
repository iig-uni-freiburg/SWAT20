package de.uni.freiburg.iig.telematik.swat.simon;

import java.util.HashMap;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeContext;

public class AwesomeTimeContext implements ITimeContext {

	// hier alles rein, was von ITimeBehaviour (bzw. AbstractTimeBehaviour
	// ableitet
	private HashMap<String, ITimeBehaviour> timeBehaviour = new HashMap<>();
	
	double time;
	private String name;
	

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		this.name = name;
	}

	@Override
	public double getTimeFor(String activity) {
		return timeBehaviour.get(activity).getNeededTime();
	}



	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}
	
	@Override
	public void reset() {
		time=0.0;
		
	}

	@Override
	public double incrementTime(double inc) {
		time+=inc;
		return time;
		
	}

	@Override
	public double getTime() {
		return time;
	}

	@Override
	public ITimeBehaviour getTimeObjectFor(String activity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsActivity(String activity) {
		return timeBehaviour.containsKey(activity);
	}


}
