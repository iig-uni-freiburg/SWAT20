package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.ArrayList;
import java.util.List;
/**
 * this class represents a counterexample. It stores the current position
 * and contains functions to get the current position and next transition
 * @author bernhard
 *
 */
public class CounterExampleVisualization {

	private List<String> path;
	private int currentPosition;
	/**
	 * Return the next transition in the path
	 * @return
	 */
	public String getNextTransition() {
		String t= path.get(currentPosition);
		currentPosition++;
		return t;
	}
	/**
	 * return true if the path reached the end, otherwise false
	 * @return
	 */
	public boolean pathEnded() {
		return currentPosition >= path.size();
	}
	/**
	 * Create an CounterExampleVisualization object
	 * @param counterExamplepath the Path to be visualized
	 */
	public CounterExampleVisualization(List<String> counterExamplepath) {

			path = new ArrayList<String>();
			System.out.println(counterExamplepath);
		
		for(String t:counterExamplepath) {
			path.add(t.replaceAll("[_]last", ""));
		}
		//System.out.println("CounterExamplePath: "+path);
		currentPosition=0;
	}
	/**
	 * create a Counterexample for the path t0t1t2 just for testing
	 */
	public CounterExampleVisualization() {
		path=new ArrayList<String>();
		path.add("t0");
		path.add("t1");
		path.add("t2");
	}
	/**
	 * return the current position in the path
	 * @return
	 */
	public int getCurrentPosition() {
		return currentPosition;
	}
	/**
	 * set the current position in the path
	 * @param currentPosition
	 */
	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}
	/**
	 * return the whole path
	 * @return
	 */
	public List<String> getPath() {
		// TODO Auto-generated method stub
		return path;
	}
}
