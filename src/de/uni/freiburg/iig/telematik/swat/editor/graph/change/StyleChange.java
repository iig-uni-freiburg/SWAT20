package de.uni.freiburg.iig.telematik.swat.editor.graph.change;

import java.util.HashMap;

import com.mxgraph.io.graphml.mxGraphMlUtils;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxGraphModel.mxStyleChange;
import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;
import com.mxgraph.util.mxConstants;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.graph.Utils;

public class StyleChange extends mxStyleChange {
	private String key;
	private PNGraph graph;

	/**
	 * @param value 
	 * @param key 
	 * @param style 
	 * @param name 
	 * @param model 
	 * 
	 */
	public StyleChange()
	{
		this(null, null, null, null);
	}
	public StyleChange(PNGraph pnGraph, Object cell, String style, String key) {
		super((mxGraphModel) pnGraph.getModel(),cell,style);
		this.graph = pnGraph;
		this.key = key;
	}
	/**
	 * Changes the root of the model.
	 */
	public void execute()
	{
		style = previous;
		previous = styleForCellChanged(cell,
				previous);
		HashMap<String, Object> styleMap = mxGraphMlUtils.getStyleMap(style,"=");
		Object value = styleMap.get(key);
		try {
			Utils.updateGraphics(graph, ((PNGraphCell) cell), key, value, key.contains("label") || key.equals(mxConstants.STYLE_FONTSIZE) || key.equals(mxConstants.STYLE_FONTFAMILY) || key.equals(mxConstants.STYLE_ALIGN) || key.equals(mxConstants.STYLE_NOLABEL) );
		} catch (ParameterException e) {
		}
	}
	/**
	 * Inner callback to update the style of the given mxCell
	 * using mxCell.setStyle and return the previous style.
	 */
	protected String styleForCellChanged(Object cell, String style)
	{
		String previous = model.getStyle(cell);
		((mxICell) cell).setStyle(style);
		return previous;
	}

}
