package de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways;

import java.util.HashSet;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.BpmnElement;

public class SequenceFlow extends AbstractGateway implements BpmnElement {
	public SequenceFlow(String name, HashSet<String> inBound,
			HashSet<String> outBound) {
		super(name, inBound, outBound);
	}

	public SequenceFlow() {
		super();
	}

	@Override
	public PTNet create(PTNet ifnc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStart(String h) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEnd(String h) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void renameStart(String h, String string) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renameEnd(String h, String string) {
		// TODO Auto-generated method stub
		
	}
}