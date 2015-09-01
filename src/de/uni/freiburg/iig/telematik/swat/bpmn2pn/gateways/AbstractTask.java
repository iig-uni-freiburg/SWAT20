package de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways;

import java.util.HashSet;

public abstract class AbstractTask extends AbstractElement {
	public AbstractTask(String name, HashSet<String> inBound,
			HashSet<String> outBound) {
		this.setName(name);
		this.setInBound(inBound);
		this.setOutBound(outBound);
	}

	public AbstractTask(String name, HashSet<String> inBound,
			HashSet<String> outBound, HashSet<String> inMessages, HashSet<String> outMessages) {
		this.setName(name);
		this.setInBound(inBound);
		this.setOutBound(outBound);
		this.setInMessages(inMessages);
		this.setOutMessages(outMessages);
	}

	public AbstractTask() {
	}
}