package de.uni.freiburg.iig.telematik.swat.bpmn2pn;

import java.util.HashSet;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;

public interface BpmnElement {
	public void setName(String name);

	public String getName();

	// @DOC
	public HashSet<String> getInBound();
	// @DOC
	public HashSet<String> getOutBound();


	// @DOC
	public PTNet create(PTNet ifnc);

	// @DOC
	public String getStart(String h);

	// @DOC
	public String getEnd(String h);

	// @DOC
	public void renameStart(String h, String string);

	// @DOC
	public void renameEnd(String h, String string);


}