package de.uni.freiburg.iig.telematik.swat.editor.graph.change;

import java.awt.Color;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxGraphModel.mxValueChange;
import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;

public class TokenColorChange extends mxAtomicGraphModelChange {

	/**
	 *
	 */
	protected String name;
	Color value;
	protected Color previous;
	private PNGraph graph;
	private PNEditor editor;

	/**
	 * 
	 */
	public TokenColorChange()
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

	public TokenColorChange(PNEditor editor, String name, Color color) {
		this.editor = editor;
		this.graph = editor.getGraphComponent().getGraph();
		this.name = name;
		this.value = color;
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
	public void setValue(Color value)
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
	public void setPrevious(Color value)
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
		editor.getEditorToolbar().updateGlobalTokenConfigurer();
		editor.getGraphComponent().getGraph().updateViews();
	}
	
	protected Color valueForCellChanged(String name, Color value)
	{
		Color oldValue = graph.getTokenColorForName(name);
		try {
			graph.updateTokenColor(name, value);
			
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

		return oldValue;
	}



}
