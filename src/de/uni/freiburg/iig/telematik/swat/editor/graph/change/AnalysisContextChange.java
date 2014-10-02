package de.uni.freiburg.iig.telematik.swat.editor.graph.change;

import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;

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
		editor.getEditorToolbar().updateTokenlabelConfigurer();
		editor.getEditorToolbar().updateSubjectClearanceConfigurer();
	}

	protected AnalysisContext valueForCellChanged(AnalysisContext value) {
		AnalysisContext oldValue = graph.getCurrentAnalysisContext();
		try {
			graph.updateAnalysisContext(value);
		} catch (ParameterException e) {
			e.printStackTrace();
		}

		return oldValue;
	}

}
