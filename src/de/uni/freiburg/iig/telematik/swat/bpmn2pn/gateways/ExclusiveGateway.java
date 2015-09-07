package de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways;

import java.util.HashMap;
import java.util.HashSet;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.BpmnElement;

public class ExclusiveGateway extends AbstractGateway implements BpmnElement {

	private String name;
	private HashSet<String> inBound;
	private HashSet<String> outBound;

	private HashMap<String, String> start = new HashMap();
	private HashMap<String, String> end = new HashMap();
	public ExclusiveGateway(String name, HashSet<String> inBound,
			HashSet<String> outBound) {
		super(name, inBound, outBound);
		this.name=name;
		this.inBound=inBound;
		this.outBound=outBound;

	}

	public ExclusiveGateway() {
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

		// create a common preplace for each incoming sequence flow
		ifnc.addPlace("helperP"+name);

		// each incoming sequence flow is mapped to the same preplace
		for(String k : this.inBound) {
			start.put(k, "helperP"+name);
		}

		// add a transition with postplace each for all outgoing sequence flow when there is at least one outgoing link
		// in the else case a single termination transition is being created when there is no outgoing link
		int i=0;
		if(this.outBound.size()>0) {
			for(String k : this.outBound) {

				ifnc.addPlace(name+"post"+k+"_"+i+"_");
				ifnc.addTransition("posthelperT"+k);
				ifnc.addFlowRelationPT("helperP"+name, "posthelperT"+k);
				ifnc.addFlowRelationTP("posthelperT"+k, name+"post"+k+"_"+i+"_");
				end.put(k, name+"post"+k+"_"+i+"_");
			}
		} else {
			ifnc.addTransition(name);
			ifnc.addFlowRelationPT("helperP"+name, name); // @doc termination event when gateway has no outgoing links
		}

		return ifnc;
	}
}