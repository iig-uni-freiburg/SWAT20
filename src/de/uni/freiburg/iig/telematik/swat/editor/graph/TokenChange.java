package de.uni.freiburg.iig.telematik.swat.editor.graph;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxGraphModel.mxValueChange;
import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;

public class TokenChange extends mxAtomicGraphModelChange {

	/**
	 *
	 */
	protected PNGraphCell cell;
	protected Multiset value, previous;
	private PNGraph graph;

	/**
	 * 
	 */
	public TokenChange()
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

	public TokenChange(PNGraph graph, PNGraphCell cell, Multiset<String> multiSet) {
		this.graph = graph;
		this.cell = cell;
		this.value = multiSet;
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
		previous = valueForCellChanged(cell,
				previous);
	}
	
	protected Multiset valueForCellChanged(PNGraphCell cell, Multiset value)
	{
		Multiset<String> oldValue = graph.getPlaceStateForCell(cell, null);
		try {
			graph.updatePlaceState(cell, value);
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

		return oldValue;
	}



}
