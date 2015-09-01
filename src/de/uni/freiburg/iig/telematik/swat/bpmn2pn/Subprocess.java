package de.uni.freiburg.iig.telematik.swat.bpmn2pn;

import java.util.HashSet;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.AbstractTask;

public class Subprocess extends AbstractTask implements BpmnElement {

	public static int numberTasks = 0;
	private String name;
	private HashSet<String> inBound;
	private HashSet<String> outBound;
	private HashSet<String> inMessages;
	private HashSet<String> outMessages;
	private HashSet<String> start = new HashSet<String>();
	private HashSet<String> end = new HashSet<String>();

	public Subprocess(String name, HashSet<String> inBound, HashSet<String> outBound, HashSet<String> inMessages, HashSet<String> outMessages) {
		super(name, inBound, outBound, inMessages, outMessages);
		this.name=name;
		this.inBound=inBound;
		this.outBound=outBound;
		this.inMessages=inMessages;
		this.outMessages=outMessages;
		numberTasks++;
	}
	public Subprocess() {
		super();
	}
	public Subprocess(String name, HashSet<String> inBound, HashSet<String> outBound, HashSet<String> inMessages, HashSet<String> outMessages, String a, String b, HashSet<String> startEvents, HashSet<String> endEvents) {
		super(name, inBound, outBound, inMessages, outMessages);
		this.name=name;
		this.inBound=inBound;
		this.outBound=outBound;
		this.inMessages=inMessages;
		this.outMessages=outMessages;
		this.start=startEvents;
		this.end=endEvents;
		numberTasks++;
	}

	@Override
	public PTNet create(PTNet ifnc) {		
		return ifnc;
	}

	@Override
	public void renameEnd(String kee, String start) {
	}

	public HashSet<String> getStart() {
		return this.start;
	}
	public HashSet<String> getEnd() {
		return this.end;
	}
	@Override
	public String getStart(String h) {
		String r=null;
		for(String h1: this.start) {
			r=h1;
		}
		return r;
	}

	@Override
	public String getEnd(String h) {
		String r=null;
		for(String h1: this.start) {
			r=h1;
		}
		return r;
	}
	@Override
	public void renameStart(String h, String string) {
		// TODO Auto-generated method stub
		
	}

}