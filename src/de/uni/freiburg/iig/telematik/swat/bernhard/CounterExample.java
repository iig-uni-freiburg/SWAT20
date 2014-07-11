package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * this class represents a counterexample given by a reasoner,
 * you can store the path and some additional info
 * @author bernhard
 *
 */
public class CounterExample {

	private java.util.List<String> path;
	private String textInfo;
	private float probability;
	public CounterExample() {
		path=Arrays.asList("t0", "t1", "t2");
	}
	public CounterExample(java.util.List<String> p) {
		path=p;
	}
	public java.util.List<String> getPath() {
		return path;
	}
	public void setPath(java.util.List<String> path) {
		this.path = path;
	}
	
}
