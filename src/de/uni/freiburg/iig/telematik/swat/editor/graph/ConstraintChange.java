package de.uni.freiburg.iig.telematik.swat.editor.graph;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxGraphModel.mxValueChange;
import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;

public class ConstraintChange extends mxAtomicGraphModelChange {

	/**
	 *
	 */
	protected String name;
	protected Multiset value, previous;
	private PNGraph graph;

	/**
	 * 
	 */
	public ConstraintChange()
	{
		this(null, null, null);
	}

	/**
	 * 
	 */
//	public TokenChange(PNGraph pnGraph, PNGraphCell cell, Multiset value)
//	{
//		this.graph = pnGraph;
//		this.cell = cell;
//		this.value = value;
//		this.previous = this.value;
//	}

	public ConstraintChange(PNGraph graph, String name, Multiset<String> multiSet) {
		this.graph = graph;
		this.name = name;
		this.value = multiSet;
		this.previous = this.value;	
		}

	/**
	 * 
	 */
	public void setCell(String value)
	{
		name =  value;
	}

	/**
	 * @return the cell
	 */
	public Object getName()
	{
		return name;
	}

	/**
	 * 
	 */
	public void setValue(Multiset value)
	{
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public Object getValue()
	{
		return value;
	}

	/**
	 * 
	 */
	public void setPrevious(Multiset value)
	{
		previous = value;
	}

	/**
	 * @return the previous
	 */
	public Object getPrevious()
	{
		return previous;
	}

	/**
	 * Changes the root of the model.
	 */
	public void execute()
	{
		value = previous;
		previous = valueForCellChanged(name,
				previous);
	}
	
	protected Multiset valueForCellChanged(String name, Multiset value)
	{
		Multiset<String> oldValue = graph.getConstraintforArc(name);
		try {
			graph.updateConstraint(name, value);
		} catch (ParameterException e) {
		}		

		return oldValue;
	}



}
