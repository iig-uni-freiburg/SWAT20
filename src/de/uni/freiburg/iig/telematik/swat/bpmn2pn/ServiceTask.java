package de.uni.freiburg.iig.telematik.swat.bpmn2pn;

import java.util.HashSet;

public class ServiceTask extends Task implements BpmnElement {
	public ServiceTask(String name, HashSet<String> inBound,
			HashSet<String> outBound) {
		super(name, inBound, outBound);
	}

	public ServiceTask() {
		super();
	}
}