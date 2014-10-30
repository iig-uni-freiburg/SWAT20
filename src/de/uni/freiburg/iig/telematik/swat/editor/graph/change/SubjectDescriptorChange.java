package de.uni.freiburg.iig.telematik.swat.editor.graph.change;

import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;

public class SubjectDescriptorChange extends mxAtomicGraphModelChange {

	protected String name;
	String value;
	protected String previous;
	private IFNetGraph graph;

	public SubjectDescriptorChange() {
		this(null, null, null);
	}

	public SubjectDescriptorChange(IFNetGraph graph, String activity, String subject) {
		this.graph = graph;
		this.name = activity;
		this.value = subject;
		this.previous = this.value;
	}

	public void setCell(String value) {
		name = value;
	}

	public Object getName() {
		return name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setPrevious(String value) {
		previous = value;
	}

	public Object getPrevious() {
		return previous;
	}

	public void execute() {
		value = previous;
		previous = valueForCellChanged(name, previous);
	}

	protected String valueForCellChanged(String activity, String subject) {
		String oldValue = graph.getCurrentSubjectDescriptorForTransition(activity);
		graph.updateSubjectDescriptorForTransition(activity, subject);

		return oldValue;
	}

}
