package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.util.HashMap;

import com.mxgraph.io.graphml.mxGraphMlUtils;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxGraphModel.mxStyleChange;
import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;
import com.mxgraph.util.mxConstants;

import de.invation.code.toval.validate.ParameterException;

public class StyleChange extends mxStyleChange {
	private String key;
	private PNGraph graph;

	/**
	 * @param value 
	 * @param key 
	 * @param style 
	 * @param cell 
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
			Utils.updateGraphics(graph, ((PNGraphCell) cell), key, value, key.contains("label") || key.equals(mxConstants.STYLE_FONTSIZE) || key.equals(mxConstants.STYLE_FONTFAMILY) || key.equals(mxConstants.STYLE_ALIGN));
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

//public static class mxStyleChange extends mxAtomicGraphModelChange
//{
//
//	/**
//	 *
//	 */
//	protected Object cell;
//
//	/**
//	 * 
//	 */
//	protected String style, previous;
//
//	private String key;
//
//	/**
//	 * 
//	 */
//	public mxStyleChange()
//	{
//		this(null, null, null, null);
//	}
//
//	/**
//	 * @param key 
//	 * 
//	 */
//	public mxStyleChange(mxGraphModel model, Object cell, String style, String key)
//	{
//		super(model);
//		System.out.println("MXSTYLECHANGE");
//		this.cell = cell;
//		this.style = style;
//		this.previous = this.style;
//		this.key = key;
//	}
//
//	/**
//	 * 
//	 */
//	public void setCell(Object value)
//	{
//		cell = value;
//	}
//
//	/**
//	 * @return the cell
//	 */
//	public Object getCell()
//	{
//		return cell;
//	}
//
//	/**
//	 * 
//	 */
//	public void setStyle(String value)
//	{
//		style = value;
//	}
//
//	/**
//	 * @return the style
//	 */
//	public String getStyle()
//	{
//		return style;
//	}
//
//	/**
//	 * 
//	 */
//	public void setPrevious(String value)
//	{
//		previous = value;
//	}
//
//	/**
//	 * @return the previous
//	 */
//	public String getPrevious()
//	{
//		return previous;
//	}
//
//	/**
//	 * Changes the root of the model.
//	 */
//	public void execute()
//	{
//		style = previous;
//		previous = ((mxGraphModel) model).styleForCellChanged(cell,
//				previous);
//		System.out.println("EXECUTE" + "\n"+style + "\n" + previous + "\n/////////" + key);
//	}
//
//}