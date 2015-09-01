package de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways;

import java.util.HashSet;

import de.uni.freiburg.iig.telematik.swat.bpmn2pn.BpmnElement;

public abstract class AbstractElement implements BpmnElement { // @DOC without
	private String name = null;
	private HashSet<String> inBound = new HashSet<String>();
	private HashSet<String> outBound = new HashSet<String>();


	private HashSet<String> inMessages = new HashSet<String>();
	private HashSet<String> outMessages = new HashSet<String>();

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setInBound(HashSet<String> inBound) {
		this.inBound = inBound;
	}

	public void setOutBound(HashSet<String> outBound) {
		this.outBound = outBound;
	}

	public void setInMessages(HashSet<String> inBound) {
		this.inMessages = inBound;
	}

	public void setOutMessages(HashSet<String> outBound) {
		this.outMessages = outBound;
	}

	public HashSet<String> getInBound() {
		HashSet<String> result = new HashSet<String>();
		try {
			result = this.inBound;
		} catch (Error r) {
		}
		return result;
	}

	public HashSet<String> getOutBound() {
		HashSet<String> result = new HashSet<String>();
		try {
			result = this.outBound;
		} catch (Error r) {
		}
		return result;
	}

	public HashSet<String> getInMessages() {
		HashSet<String> result = new HashSet<String>();
		try {
			result = this.inMessages;
		} catch (Error r) {
		}
		return result;
	}

	public HashSet<String> getOutMessages() {
		HashSet<String> result = new HashSet<String>();
		try {
			result = this.outMessages;
		} catch (Error r) {
		}
		return result;
	}
}