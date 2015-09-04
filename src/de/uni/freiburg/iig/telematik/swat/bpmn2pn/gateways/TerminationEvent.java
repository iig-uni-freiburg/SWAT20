package de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways;

import java.util.HashMap;
import java.util.HashSet;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.BpmnElement;

public class TerminationEvent extends AbstractEvent implements BpmnElement {
	private String name;
	private HashSet<String> inBound;
	private HashSet<String> outBound;

	private HashMap<String, String> start = new HashMap();
	private HashMap<String, String> end = new HashMap();

	public TerminationEvent(String name, HashSet<String> inBound,
			HashSet<String> outBound) {
		super(name, inBound, outBound);
		this.name=name;
		this.inBound=inBound;
		this.outBound=outBound;
	}

	public TerminationEvent() {
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

	@Override
	public PTNet create(PTNet ifnc) {
		ifnc.addTransition(name);
		int i;
		i=0;
		for(String k : this.inBound) {
			ifnc.addPlace(name+"pre"+k+":"+i+":");	
			ifnc.addFlowRelationPT(name+"pre"+k+":"+i+":", name);
			start.put(k, name+"pre"+k+":"+i+":");
		}
		ifnc.addPlace(name+"post");
		ifnc.addFlowRelationTP(name, name+"post");
		// finally a postplace for each outgoing sequence flow is generated here (implicit parallel sequence flow)
		i=0;
		ifnc.addPlace(name+"post");
		ifnc.addFlowRelationTP(name, name+"post");
		return ifnc;
	}
}