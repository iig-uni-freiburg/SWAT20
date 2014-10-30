package de.uni.freiburg.iig.telematik.swat.editor.graph.change;

import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;

public class TransitionLabelingChange extends mxAtomicGraphModelChange {

	protected String name;
	SecurityLevel value;
	protected SecurityLevel previous;
	private IFNetGraph graph;

	public TransitionLabelingChange() {
		this(null, null, null);
	}

	public TransitionLabelingChange(IFNetGraph graph, String name, SecurityLevel secLevel) {
		this.graph = graph;
		this.name = name;
		this.value = secLevel;
		this.previous = this.value;
	}

	public void setCell(String value) {
		name = value;
	}

	public Object getName() {
		return name;
	}

	public void setValue(SecurityLevel value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setPrevious(SecurityLevel value) {
		previous = value;
	}

	public Object getPrevious() {
		return previous;
	}

	public void execute() {
		value = previous;
		previous = valueForCellChanged(name, previous);
	}

	protected SecurityLevel valueForCellChanged(String name, SecurityLevel value) {
		SecurityLevel oldValue = graph.getCurrentTransitionLabeling(name);
		graph.updateTransitionLabeling(name, value);

		return oldValue;
	}

}
