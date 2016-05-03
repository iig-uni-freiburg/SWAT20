package de.uni.freiburg.iig.telematik.swat.simulation;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedTransition;

public class FireElement {
	
	private double time;
	private AbstractTimedTransition t;
	
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public AbstractTimedTransition getTransition() {
		return t;
	}
	public void setTransition(AbstractTimedTransition t) {
		this.t = t;
	}
	
	public String toString(){
		return t.getNet().getName()+": "+t.getLabel()+" ("+time+")";
	}


}
