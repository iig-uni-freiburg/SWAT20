/**
 * $Id: mxGraphView.java,v 1.1 2012/11/15 13:26:46 gaudenz Exp $
 * Copyright (c) 2007-2010, Gaudenz Alder, David Benson
 */
package de.unifreiburg.iig.bpworkbench2.editor.soul;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUndoableEdit.mxUndoableChange;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxEdgeStyle.mxEdgeStyleFunction;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;
import com.mxgraph.view.mxPerimeter.mxPerimeterFunction;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Offset;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Constants;

/**
 * Implements a view for the graph. This class is in charge of computing the
 * absolute coordinates for the relative child geometries, the points for
 * perimeters and edge styles and keeping them cached in cell states for
 * faster retrieval. The states are updated whenever the model or the view
 * state (translate, scale) changes. The scale and translate are honoured in
 * the bounds.
 * 
 * This class fires the following events:
 * 
 * mxEvent.UNDO fires after the root was changed in setCurrentRoot. The
 * <code>edit</code> property contains the mxUndoableEdit which contains the
 * mxCurrentRootChange.
 * 
 * mxEvent.SCALE_AND_TRANSLATE fires after the scale and transle have been
 * changed in scaleAndTranslate. The <code>scale</code>, <code>previousScale</code>,
 * <code>translate</code> and <code>previousTranslate</code> properties contain
 * the new and previous scale and translate, respectively.
 * 
 * mxEvent.SCALE fires after the scale was changed in setScale. The
 * <code>scale</code> and <code>previousScale</code> properties contain the
 * new and previous scale.
 * 
 * mxEvent.TRANSLATE fires after the translate was changed in setTranslate. The
 * <code>translate</code> and <code>previousTranslate</code> properties contain
 * the new and previous value for translate.
 * 
 * mxEvent.UP and mxEvent.DOWN fire if the current root is changed by executing
 * a mxCurrentRootChange. The event name depends on the location of the root
 * in the cell hierarchy with respect to the current root. The
 * <code>root</code> and <code>previous</code> properties contain the new and
 * previous root, respectively.
 */
public class GraphView extends mxGraphView
{

	public GraphView(mxGraph graph) {
		super(graph);
		// TODO Auto-generated constructor stub
	}
@Override
/**
 * Updates the absoluteOffset of the given vertex cell state. This takes
 * into account the label position styles.
 * 
 * @param state Cell state whose absolute offset should be updated.
 */
/**
 * Updates the absoluteOffset of the given vertex cell state. This takes
 * into account the label position styles.
 * 
 * @param state Cell state whose absolute offset should be updated.
 */
public void updateVertexLabelOffset(mxCellState state)
{
	String horizontal = mxUtils.getString(state.getStyle(),
			mxConstants.STYLE_LABEL_POSITION, mxConstants.ALIGN_CENTER);

	if (horizontal.equals(mxConstants.ALIGN_LEFT))
	{
		state.getAbsoluteOffset().setX(state.getAbsoluteOffset().getX()
				- state.getWidth());
	}
	else if (horizontal.equals(mxConstants.ALIGN_RIGHT))
	{
		state.getAbsoluteOffset().setX(state.getAbsoluteOffset().getX()
				+ state.getWidth());
	}

	String vertical = mxUtils.getString(state.getStyle(),
			mxConstants.STYLE_VERTICAL_LABEL_POSITION,
			mxConstants.ALIGN_MIDDLE);

	if (vertical.equals(mxConstants.ALIGN_TOP))
	{
		state.getAbsoluteOffset().setY(state.getAbsoluteOffset().getY()
				- state.getHeight());
	}
	else if (vertical.equals(mxConstants.ALIGN_BOTTOM))
	{
		state.getAbsoluteOffset().setY(state.getAbsoluteOffset().getY()
				+ state.getHeight());
	}
	


		addAnnotationGraphics(state);
	
	
	
	
	
	
	
}

/**
 * @param state
 * @param n
 * @param annotation
 */
public void addAnnotationGraphics(mxCellState state) {
	mxCell cell = (mxCell) state.getCell();
	if (cell.getParent() != null) {
		AbstractGraphicalPN<?, ?, ?, ?, ?> n = (AbstractGraphicalPN<?, ?, ?, ?, ?>) cell
				.getParent().getValue();
		mxPoint offset = state.getAbsoluteOffset();
		AnnotationGraphics annotation = null;
		try {
			annotation = new AnnotationGraphics(new Offset((int) offset.getX(),
					(int) offset.getY()), new Fill(), new Line(), new Font());
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	Map<String, AnnotationGraphics> labelAnnotationGraphics = null;
	System.out.println(cell.getValue() + cell.getStyle());
	if (cell.getStyle()!=null && cell.getStyle().contentEquals(Constants.PNPlaceShape)) {
		if (n.getPetriNetGraphics().getPlaceLabelAnnotationGraphics() == null)
			labelAnnotationGraphics = new HashMap<String, AnnotationGraphics>();
	 else {
		labelAnnotationGraphics = n.getPetriNetGraphics()
				.getPlaceLabelAnnotationGraphics();}
		labelAnnotationGraphics.put(cell.getId(), annotation);
		n.getPetriNetGraphics().setPlaceLabelAnnotationGraphics(
				labelAnnotationGraphics);
	}
	
	if (cell.getStyle()!=null &&cell.getStyle().contentEquals(Constants.PNTransitionShape)) {
		if (n.getPetriNetGraphics().getTransitionLabelAnnotationGraphics() == null)
			labelAnnotationGraphics = new HashMap<String, AnnotationGraphics>();
	 else {
		labelAnnotationGraphics = n.getPetriNetGraphics()
				.getTransitionLabelAnnotationGraphics();}
		labelAnnotationGraphics.put(cell.getId(), annotation);
		n.getPetriNetGraphics().setTransitionLabelAnnotationGraphics(
				labelAnnotationGraphics);
	}
	
	
	if (cell.getId()!=null && cell.getId().startsWith("arc")) {
		if (n.getPetriNetGraphics().getArcAnnotationGraphics() == null)
			labelAnnotationGraphics = new HashMap<String, AnnotationGraphics>();
	 else {
		labelAnnotationGraphics = n.getPetriNetGraphics()
				.getArcAnnotationGraphics();}
		labelAnnotationGraphics.put(cell.getId(), annotation);
		n.getPetriNetGraphics().setArcAnnotationGraphics(
				labelAnnotationGraphics);
	}
}
		
}



}
