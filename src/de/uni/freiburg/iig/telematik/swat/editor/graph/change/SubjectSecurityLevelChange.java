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
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.swat.editor.IFNetEditor;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;

public class SubjectSecurityLevelChange extends mxAtomicGraphModelChange {

	/**
	 *
	 */
	protected String name;
	SecurityLevel value;
	protected SecurityLevel previous;
	private IFNetGraph graph;
	private IFNetEditor editor;

	/**
	 * 
	 */
	public SubjectSecurityLevelChange()
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

	public SubjectSecurityLevelChange(PNEditor editor2, String name, SecurityLevel sl) {
		this.editor = (IFNetEditor) editor2;
		this.graph = (IFNetGraph) editor2.getGraphComponent().getGraph();
		this.name = name;
		this.value = sl;
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
	public void setValue(SecurityLevel value)
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
	public void setPrevious(SecurityLevel value)
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
		editor.getEditorToolbar().updateSubjectClearanceConfigurer();
//		editor.getGraphComponent().getGraph().updateViews();

	}
	
	protected SecurityLevel valueForCellChanged(String label, SecurityLevel value)
	{
		SecurityLevel oldValue = graph.getSecurityLevelForSubject(label);
		try {
			graph.updateSecurityLevelForSubject(label, value);
			
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

		return oldValue;
	}



}
