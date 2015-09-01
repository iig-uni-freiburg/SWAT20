package de.uni.freiburg.iig.telematik.swat.bpmn2pn;

import java.util.HashMap;
import java.util.HashSet;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;

public class UserTask extends Task implements BpmnElement {

	private String name;
	private HashSet<String> inBound;
	private HashSet<String> outBound;

	private HashMap<String, String> start = new HashMap();
	private HashMap<String, String> end = new HashMap();

	public UserTask(String name, HashSet<String> inBound,
			HashSet<String> outBound) {
		super(name, inBound, outBound);
		this.name=name;
		this.inBound=inBound;
		this.outBound=outBound;
	}

	public UserTask() {
		super();
	}

	public String getStart(String identifier) {
		return this.start.get(identifier);
	}
	public String getEnd(String identifier) {
		return this.end.get(identifier);
	}

	public HashMap<String, String> getStart() {
		return this.start;
	}
	public HashMap<String, String> getEnd() {
		return this.end;
	}

	public void renameStart(String a, String b) {
		String h = this.start.get(a);
		this.start.remove(a);
		this.start.put(b, h);
	}

	public void renameEnd(String a, String b) {
		String h = this.end.get(a);
		this.end.remove(a);
		this.end.put(b, h);	
	}

	public IFNet create(IFNet ifnc) {
		ifnc.addTransition(name);

		int i;

		ifnc.addPlace("helperP"+name);
		ifnc.addFlowRelationPT("helperP"+name, name);

		i=0;
		for(String k : this.inBound) {
			start.put(k, "helperP"+name);
		}

		i=0;
		for(String k : this.outBound) {
			ifnc.addPlace(name+"post"+k+"["+i+"]");
			ifnc.addFlowRelationTP(name, name+"post"+k+"["+i+"]");
			end.put(k, name+"post"+k+"["+i+"]");
		}		

		return ifnc;
	}
}