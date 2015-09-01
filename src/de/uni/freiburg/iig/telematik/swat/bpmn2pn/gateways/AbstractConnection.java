package de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways;

import java.util.HashSet;

public abstract class AbstractConnection extends AbstractElement {
	public AbstractConnection(String name, HashSet<String> inBound,
			HashSet<String> outBound) {
		this.setName(name);
		this.setInBound(inBound);
		this.setOutBound(outBound);
	}

	public AbstractConnection() {
	}
}