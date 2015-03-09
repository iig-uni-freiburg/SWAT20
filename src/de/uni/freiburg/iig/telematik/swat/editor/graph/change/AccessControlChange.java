package de.uni.freiburg.iig.telematik.swat.editor.graph.change;

import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.uni.freiburg.iig.telematik.seram.accesscontrol.AbstractACModel;
import de.uni.freiburg.iig.telematik.swat.editor.menu.IFNetToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.IFNetGraph;

public class AccessControlChange extends mxAtomicGraphModelChange {

	/**
	 *
	 */
	protected String name;
	AbstractACModel value;
	protected AbstractACModel previous;
	private IFNetGraph graph;
	private PNEditorComponent editor;

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

	public AccessControlChange(PNEditorComponent editor, AbstractACModel acModel) {
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
	public void setValue(AbstractACModel value) {
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
	public void setPrevious(AbstractACModel value) {
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

	protected AbstractACModel valueForCellChanged(AbstractACModel value) {
		//TODO: Adapt to new ACStructure 

//		AbstractACModel oldValue = graph.getCurrentAccessControlModel();
//		graph.updateAccessControlModel(value);

//		return oldValue;
		return null;
	}

}
