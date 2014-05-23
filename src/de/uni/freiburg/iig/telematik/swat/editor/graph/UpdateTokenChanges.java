package de.uni.freiburg.iig.telematik.swat.editor.graph;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxGraphModel.mxValueChange;
import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;

public class UpdateTokenChanges extends mxAtomicGraphModelChange {

	/**
	 *
	 */
	protected String name;
	private PNGraph graph;

	/**
	 * 
	 */

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

	public UpdateTokenChanges(PNGraph graph, String name) {
		this.graph = graph;
		this.name = name;
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

	/**
	 * Changes the root of the model.
	 */
	public void execute() {
//		System.out.println(((mxGraphModel) getModel()).getUpdateLevel());
////		;
//		if (graph instanceof CPNGraph) {
//			((CPNGraph) graph).updateTokenConfigurer(name);
//		}
	}

}
