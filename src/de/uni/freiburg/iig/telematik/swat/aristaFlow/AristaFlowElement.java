package de.uni.freiburg.iig.telematik.swat.aristaFlow;

import java.util.ArrayList;
import java.util.List;

/** Data Structure to hold AristaFlow Elements **/
class AristaFlowElement {
	PTequivalent type;
	List<String> incomingLinks = new ArrayList<String>();
	List<String> outgoingLinks = new ArrayList<String>();
	String internalName;
	String displayName;
        String originator;

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

	public AristaFlowElement(String internalName, String displayName) {
		this.internalName = internalName;
		this.displayName = displayName;
	}

	public AristaFlowElement(String internalName, String displayName, PTequivalent equiv) {
		this(internalName, displayName);
		this.type = equiv;
	}

	public AristaFlowElement(String name, PTequivalent equiv) {
		this(name, name);
		this.type = equiv;
	}

	public void addIncomingRelation(String relation) {
		incomingLinks.add(relation);
	}

	public void addOutgoingRelation(String relation) {
		outgoingLinks.add(relation);
	}

	public List<String> getOutgoingRelations() {
		return outgoingLinks;
	}

	public List<String> getIncomingRelations() {
		return incomingLinks;
	}

	public enum PTequivalent {
		PLACE, TRANSITION;
	}
}