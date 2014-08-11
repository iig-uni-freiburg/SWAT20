package de.uni.freiburg.iig.telematik.swat.editor.graph.change;

import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;

public class TransitionSilentChange extends mxAtomicGraphModelChange {

	/**
	 *
	 */
	protected String name;
	boolean value;
	protected boolean previous;
	private PNGraph graph;

	/**
	 * 
	 */
	public TransitionSilentChange()
	{
		this(null, null, false);
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

	public TransitionSilentChange(PNGraph graph, String name, boolean setSilent) {
		this.graph = graph;
		this.name = name;
		this.value = setSilent;
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
	public void setValue(boolean value)
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
	public void setPrevious(boolean value)
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
	
	protected boolean valueForCellChanged(String name, boolean value)
	{
		boolean oldValue = graph.getTransitionSilentState(name);
		try {
			graph.updateTransitionSilent(name, value);
		} catch (ParameterException e) {
			e.printStackTrace();
		}		

		return oldValue;
	}



}
