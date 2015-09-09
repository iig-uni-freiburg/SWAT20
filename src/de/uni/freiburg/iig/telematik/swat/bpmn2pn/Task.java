package de.uni.freiburg.iig.telematik.swat.bpmn2pn;
import java.util.HashMap;
import java.util.HashSet;

import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.AbstractTask;
public class Task extends AbstractTask {
	public static int numberTasks = 0;
	private String name;
	private HashSet<String> inBound;
	private HashSet<String> outBound;
	private HashSet<String> inMessages;
	private HashSet<String> outMessages;
	private HashMap<String, String> start = new HashMap<>();
	private HashMap<String, String> end = new HashMap<>();
	
	public Task(String name, HashSet<String> inBound, HashSet<String> outBound, HashSet<String> inMessages, HashSet<String> outMessages) {
		super(name, inBound, outBound, inMessages, outMessages);
		this.name=name;
		this.inBound=inBound;
		this.outBound=outBound;
		this.inMessages=inMessages;
		this.outMessages=outMessages;
		numberTasks++;
	}
	public int getNumberTasks() {
		return numberTasks;
	}
	public Task() {
		super();
	}
	public Task(String name2, HashSet<String> inBound2, HashSet<String> outBound2) {
		super(name2,inBound2,outBound2);
		numberTasks++;
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
		this.start.put(a, b);
	}

	public void renameEnd(String a, String b) {
		String h = this.end.get(a);
		this.end.remove(a);
		this.end.put(b, h);	
	}
	public PTNet create(PTNet ifnc) {

		// a task element directly corresponds to a single transition
		ifnc.addTransition(sanitize(name));

		// there is exactly one preplace when there is at least one incoming sequence flow
		if(this.inBound.size()!=0) {
			ifnc.addPlace("helperP"+name);
			ifnc.addFlowRelationPT("helperP"+name, sanitize(name));
		}

		// therefore each ingoing sequence flow is mapped to the only existent preplace
		for(String k : this.inBound) {
			start.put(k, "helperP"+name);
		}

		// finally a postplace for each outgoing sequence flow is generated here (implicit parallel sequence flow)
		int i=0;
		for(String k : this.outBound) {
			ifnc.addPlace(name+"post"+k+"_"+i+"_");
			//ifnc.getPlace(name+"post"+k+"_"+i+"_").setCapacity(1);
			ifnc.addFlowRelationTP(name, name+"post"+k+"_"+i+"_");
			//ifnc.getFlowRelation(name).setConstraint(1);
			end.put(k, name+"post"+k+"_"+i+"_");
		}		

		return ifnc;

	}
}