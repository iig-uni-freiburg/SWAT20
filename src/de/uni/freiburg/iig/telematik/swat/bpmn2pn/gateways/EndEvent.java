package de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways;

import java.util.HashMap;
import java.util.HashSet;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.BpmnElement;

public class EndEvent extends AbstractGateway implements BpmnElement {
	private String name;
	private HashSet<String> inBound;
	private HashSet<String> outBound;

	private HashMap<String, String> start = new HashMap();
	private HashMap<String, String> end = new HashMap();
	public EndEvent(String name, HashSet<String> inBound,
			HashSet<String> outBound) {
		super(name, inBound, outBound);
		this.name=name;
		this.inBound=inBound;
		this.outBound=outBound;
	}

	public EndEvent() {
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


	public PTNet create(PTNet ifnc) {

		// the start event directly corresponds to a transition
		ifnc.addTransition(name);
		int i;

		// add a single preplace with a single black token indicating that the event is enabled and can be fired when there is no incoming message flow
		ifnc.addPlace(name+"pre");

		PTMarking h = ifnc.getMarking();

		Multiset<String> pInMarking = new Multiset<String>();
		pInMarking.add("black");
		
		//h.set(name+"pre", pInMarking); //for ifnet
		h.set(name+"pre", 1); //for ptnet

		ifnc.setMarking(h);		
		ifnc.addFlowRelationPT(name+"pre", name);

		// create a postplace for each outgoing sequence flow (parallel flow)
		i=0;
		for(String k : this.outBound) {
			ifnc.addPlace(name+"post"+k+"["+i+"]");
			ifnc.addFlowRelationTP(name, name+"post"+k+"["+i+"]");
			end.put(k, name+"post"+k+"["+i+"]");
		}		

		return ifnc;
	}
}