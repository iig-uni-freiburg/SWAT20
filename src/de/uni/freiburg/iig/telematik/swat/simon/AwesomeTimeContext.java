package de.uni.freiburg.iig.telematik.swat.simon;

import java.util.HashMap;
import java.util.Set;

import org.apache.commons.math3.random.GaussianRandomGenerator;

import com.thoughtworks.xstream.XStream;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeContext;
import de.uni.freiburg.iig.telematik.swat.jascha.AwesomeResourceContext;

public class AwesomeTimeContext implements ITimeContext {

	// hier alles rein, was von ITimeBehaviour (bzw. AbstractTimeBehaviour
	// ableitet
	private HashMap<String, ITimeBehaviour> timeBehaviour = new HashMap<>();
	
	private HashMap<String, Double> deadlines = new HashMap<>();
	
	double time;

	private String name;


	

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

		this.name = name;
	}

	@Override
	public double getTimeFor(String activity) {
		double neededTime = timeBehaviour.get(activity).getNeededTime();
		if(neededTime<0) neededTime=0;
		return neededTime;
	}



	public double getDeadlineFor(String name){
		checkDeadlines();
		return deadlines.get(name);
	}
	
	public void setDeadline(String name, double deadline){
		checkDeadlines();
		deadlines.put(name, deadline);
	}
	
	public boolean containsDeadlineFor(String name){
		checkDeadlines();
		return (deadlines.containsKey(name));
	}
	
	private void checkDeadlines(){
		//create Map if null (compatibility with old searlized verseions of TimeContext)
		if(deadlines==null)
			deadlines=new HashMap<>();
	}


	@Override
	public String getName() {
		return this.name;
	}
	

	@Override

	public boolean containsActivity(String activity) {
		return timeBehaviour.containsKey(activity);
	}


	

	@Override
	public ITimeBehaviour getTimeObjectFor(String activity) {
		return timeBehaviour.get(activity);
	}
	
	public void addBehaviour(String name, ITimeBehaviour behaveiour){
		timeBehaviour.put(name, behaveiour);
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
	
	public Set<String> getKnownActivities(){
		return timeBehaviour.keySet();
	}
	
	public AwesomeTimeContext clone(){
		AwesomeTimeContext clone = (AwesomeTimeContext) new XStream().fromXML(new XStream().toXML(this));
		return clone;
	}


}
