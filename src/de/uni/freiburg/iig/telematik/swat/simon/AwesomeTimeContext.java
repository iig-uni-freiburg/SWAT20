package de.uni.freiburg.iig.telematik.swat.simon;

import java.util.HashMap;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeContext;

public class AwesomeTimeContext implements ITimeContext {

	// hier alles rein, was von ITimeBehaviour (bzw. AbstractTimeBehaviour
	// ableitet
	private HashMap<String, ITimeBehaviour> timeBehaviour = new HashMap<>();
	
	double time;
	

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getTimeFor(String activity) {

		ITimeBehaviour behave = timeBehaviour.get(activity);
		behave.getNeededTime();

		return timeBehaviour.get(activity).getNeededTime();
	}

	@Override
	public ITimeBehaviour getTimeObjectFor(String activity) {
		return timeBehaviour.get(activity);
	}
	
	public void addBehaviour(String name, ITimeBehaviour behaveiour){
		timeBehaviour.put(name, behaveiour);
	}

	@Override
	public boolean containsActivity(String activity) {
		return timeBehaviour.containsKey(activity);
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

}
