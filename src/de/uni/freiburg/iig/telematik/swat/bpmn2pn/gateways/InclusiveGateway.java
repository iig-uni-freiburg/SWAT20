package de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.BpmnElement;

public class InclusiveGateway extends AbstractGateway implements BpmnElement {

	private String name;
	private HashSet<String> inBound;
	private HashSet<String> outBound;

	private HashMap<String, String> start = new HashMap();
	private HashMap<String, String> end = new HashMap();
	public InclusiveGateway(String name, HashSet<String> inBound,
			HashSet<String> outBound) {
		super(name, inBound, outBound);
		this.name=name;
		this.inBound=inBound;
		this.outBound=outBound;

	}

	public InclusiveGateway() {
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

		HashSet<String> outSet;
		HashSet<String> inSet;

		Set<Set<String>> sssk;
		int cnt;
		int i = 0;

		if(this.inBound.size()>1) {

			outSet = new HashSet<String>();
			for(String k : this.inBound) {
				outSet.add(k);
				ifnc.addPlace(name+"pre"+k+"["+i+"]");
				start.put(k, name+"pre"+k+"["+i+"]");
			}	
			sssk = generateAllSubsets(outSet);
			cnt=0;
			for(Set<String> hhh : sssk) {
				if(hhh.size()!=0) {
					ifnc.addTransition(name+"["+cnt+"]-in");
					ifnc.addFlowRelationTP(name+"["+cnt+"]-in", "helperP"+name);
					for(String inner : hhh) {
						ifnc.addFlowRelationPT(name+"pre"+inner+"[0]", name+"["+cnt+"]-in");
					}
				}
				cnt++;
			}

		} else {

			for(String k : this.inBound) {
				start.put(k, "helperP"+name);
			}
		}
		i = 0;
		if(this.outBound.size()>1) {

			inSet = new HashSet<String>();
			for(String k : this.outBound) {
				inSet.add(k);
				ifnc.addPlace(name+"post"+k+"["+i+"]");
				end.put(k, name+"post"+k+"["+i+"]");
			}	
			sssk = generateAllSubsets(inSet);
			cnt=0;
			for(Set<String> hhh : sssk) {
				if(hhh.size()!=0) {
					ifnc.addTransition(name+"["+cnt+"]-out");
					ifnc.addFlowRelationPT("helperP"+name, name+"["+cnt+"]-out");
					for(String inner : hhh) {
						ifnc.addFlowRelationTP(name+"["+cnt+"]-out", name+"post"+inner+"[0]");
					}
				}
				cnt++;
			}
		} else {
			for(String k : this.outBound) {
				end.put(k, "helperP"+name);
			}
		}
		return ifnc;
	}


	public static <T> Set<Set<T>> generateAllSubsets(Set<T> original) {
		Set<Set<T>> allSubsets = new HashSet<Set<T>>();

		allSubsets.add(new HashSet<T>()); //Add empty set.

		for (T element : original) {
			// Copy subsets so we can iterate over them without ConcurrentModificationException
			Set<Set<T>> tempClone = new HashSet<Set<T>>(allSubsets);

			// All element to all subsets of the current power set.
			for (Set<T> subset : tempClone) {
				Set<T> extended = new HashSet<T>(subset);
				extended.add(element);
				allSubsets.add(extended);
			}
		}
		return allSubsets;
	}



}