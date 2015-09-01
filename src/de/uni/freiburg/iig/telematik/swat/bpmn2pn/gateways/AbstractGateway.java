package de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways;

import java.util.HashSet;

public abstract class AbstractGateway extends AbstractElement {
	public AbstractGateway(String name, HashSet<String> inBound,
			HashSet<String> outBound) {
		this.setName(name);
		this.setInBound(inBound);
		this.setOutBound(outBound);
	}

	public AbstractGateway() {
	}
}