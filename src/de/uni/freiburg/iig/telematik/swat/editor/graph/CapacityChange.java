package de.uni.freiburg.iig.telematik.swat.editor.graph;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxGraphModel.mxValueChange;
import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;

public class CapacityChange extends mxAtomicGraphModelChange {

	/**
	 *
	 */
	protected String name;
	int value;
	protected int previous;
	private PNGraph graph;
	private String color;

	/**
	 * @param newCapacity 
	 * @param color 
	 * @param string 
	 * @param graph2 
	 * 
	 */

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

	public CapacityChange(PNGraph graph, String name, String color, int newCapacity) {
		this.graph = graph;
		this.name = name;
		this.color = color;
		this.value = newCapacity;
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
		System.out.println("I AM CapacityCHANGE");
		value = previous;
		previous = valueForCellChanged(name,color,
				previous);
		System.out.println(color+": " +value + "#p: " + previous);
		
	}
	
	protected int valueForCellChanged(String name, String color, int newCapacity)
	{
		int oldValue = graph.getCapacityforPlace(name,color);
		try {
			graph.updatePlaceCapacity(name, color,newCapacity);
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

		return oldValue;
	}



}
