package de.uni.freiburg.iig.telematik.swat.editor.graph.change;

import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;

public class SubjectDescriptorChange extends mxAtomicGraphModelChange {

	/**
	 *
	 */
	protected String name;
	String value;
	protected String previous;
	private IFNetGraph graph;

	/**
	 * 
	 */
	public SubjectDescriptorChange()
	{
		this(null, null, null);
	}

	/**
	 * 
	 */

	public SubjectDescriptorChange(IFNetGraph graph, String activity, String subject) {
		this.graph = graph;
		this.name = activity;
		this.value = subject;
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
	public void setValue(String value)
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
	public void setPrevious(String value)
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
	
	protected String valueForCellChanged(String activity, String subject)
	{
		String oldValue = graph.getCurrentSubjectDescriptorForTransition(activity);
		try {
			graph.updateSubjectDescriptorForTransition(activity, subject);
		} catch (ParameterException e) {
			e.printStackTrace();
		}		

		return oldValue;
	}



}
