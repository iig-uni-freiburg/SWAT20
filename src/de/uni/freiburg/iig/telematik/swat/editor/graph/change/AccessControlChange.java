package de.uni.freiburg.iig.telematik.swat.editor.graph.change;

import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.uni.freiburg.iig.telematik.seram.accesscontrol.ACModel;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.menu.IFNetToolBar;

public class AccessControlChange extends mxAtomicGraphModelChange {

	/**
	 *
	 */
	protected String name;
	ACModel value;
	protected ACModel previous;
	private IFNetGraph graph;
	private PNEditor editor;

	/**
	 * 
	 */
	public AccessControlChange() {
		this(null, null);
	}

	/**
	 * 
	 */
	// public TokenChange(PNGraph pnGraph, PNGraphCell cell, Multiset value)
	// {
	// this.graph = pnGraph;
	// this.cell = cell;
	// this.value = value;
	// this.previous = this.value;
	// }

	public AccessControlChange(PNEditor editor, ACModel acModel) {
		this.editor = editor;
		this.graph = (IFNetGraph) editor.getGraphComponent().getGraph();
		this.name = name;
		this.value = acModel;
		this.previous = this.value;
	}

	/**
	 * 
	 */
	public void setCell(String value) {
		name = value;
	}

	/**
	 * @return the cell
	 */
	public Object getName() {
		return name;
	}

	/**
	 * 
	 */
	public void setValue(ACModel value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * 
	 */
	public void setPrevious(ACModel value) {
		previous = value;
	}

	/**
	 * @return the previous
	 */
	public Object getPrevious() {
		return previous;
	}

	/**
	 * Changes the root of the model.
	 */
	public void execute() {
		value = previous;
		previous = valueForCellChanged(previous);
		((IFNetToolBar)editor.getEditorToolbar()).updateTokenlabelConfigurer();
		((IFNetToolBar)editor.getEditorToolbar()).updateSubjectClearanceConfigurer();
	}

	protected ACModel valueForCellChanged(ACModel value) {
		ACModel oldValue = graph.getCurrentAccessControlModel();
		graph.updateAccessControlModel(value);

		return oldValue;
	}

}
