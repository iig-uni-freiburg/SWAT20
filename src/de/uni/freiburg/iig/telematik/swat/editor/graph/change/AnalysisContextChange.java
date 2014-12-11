package de.uni.freiburg.iig.telematik.swat.editor.graph.change;

import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.menu.IFNetToolBar;

public class AnalysisContextChange extends mxAtomicGraphModelChange {

	/**
	 *
	 */
	protected String name;
	AnalysisContext value;
	protected AnalysisContext previous;
	private IFNetGraph graph;
	private PNEditor editor;

	/**
	 * 
	 */
	public AnalysisContextChange() {
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

	public AnalysisContextChange(PNEditor editor, AnalysisContext ac) {
		this.editor = editor;
		this.graph = (IFNetGraph) editor.getGraphComponent().getGraph();
		this.name = name;
		this.value = ac;
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
	public void setValue(AnalysisContext value) {
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
	public void setPrevious(AnalysisContext value) {
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

	protected AnalysisContext valueForCellChanged(AnalysisContext value) {
		AnalysisContext oldValue = graph.getCurrentAnalysisContext();
		graph.updateAnalysisContext(value);

		return oldValue;
	}

}
