package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.ArrayList;
import java.util.List;

public class CounterExampleVisualization {

	private List<String> path;
	private int currentPosition;
	
	public String getNextTransition() {
		String t= path.get(currentPosition);
		currentPosition++;
		return t;
	}
	public boolean pathEnded() {
		return currentPosition >= path.size();
	}

	public CounterExampleVisualization(List<String> counterExamplepath) {
		if(counterExamplepath != null)
			path = counterExamplepath;
		else {
			path=new ArrayList<String>();
			path.add("t0");
			path.add("t1");
			path.add("t2");
		}
		currentPosition=0;
	}
	public CounterExampleVisualization() {
		path=new ArrayList<String>();
		path.add("t0");
		path.add("t1");
		path.add("t2");
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}

	public List<String> getPath() {
		// TODO Auto-generated method stub
		return path;
	}
}
