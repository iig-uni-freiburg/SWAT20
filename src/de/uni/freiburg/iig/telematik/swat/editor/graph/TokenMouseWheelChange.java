package de.uni.freiburg.iig.telematik.swat.editor.graph;

import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;

public class TokenMouseWheelChange extends mxAtomicGraphModelChange {

	
	protected PNGraphCell cell;
	int value;
	protected int previous;
	private PNGraph graph;

	/**
	 * 
	 */
	public TokenMouseWheelChange()
	{
		this(null, null, (Integer) null);
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

	public TokenMouseWheelChange(PNGraph graph, PNGraphCell cell, int i) {
		this.graph = graph;
		this.cell = cell;
		this.value = i;
		this.previous = this.value;	
		}

	/**
	 * 
	 */
	public void setCell(PNGraphCell value)
	{
		cell = (PNGraphCell) value;
	}

	/**
	 * @return the cell
	 */
	public Object getCell()
	{
		return cell;
	}

	/**
	 * 
	 */
	public void setValue(int value)
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
	public void setPrevious(int value)
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
		previous = valueForCellChanged(cell,
				previous);
	}
	
	protected int valueForCellChanged(PNGraphCell cell, int previous)
	{
		System.out.println(previous);
	int oldValue = (previous == 1)? -1:1;
		try {
			graph.inOrDecrementPlaceState(cell,previous);
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

		return oldValue;
	}

}
