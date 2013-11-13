/**
 * $Id: mxGraphView.java,v 1.1 2012/11/15 13:26:46 gaudenz Exp $
 * Copyright (c) 2007-2010, Gaudenz Alder, David Benson
 */
package de.unifreiburg.iig.bpworkbench2.editor.graph;

import java.util.HashMap;
import java.util.Map;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Offset;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;

/**
 * Implements a view for the graph. This class is in charge of computing the absolute coordinates for the relative child geometries, the points for perimeters and edge styles and keeping them cached in cell states for faster retrieval. The states are updated whenever the model or the view state (translate, scale) changes. The scale and translate are honoured in the bounds.
 * 
 * This class fires the following events:
 * 
 * mxEvent.UNDO fires after the root was changed in setCurrentRoot. The <code>edit</code> property contains the mxUndoableEdit which contains the mxCurrentRootChange.
 * 
 * mxEvent.SCALE_AND_TRANSLATE fires after the scale and transle have been changed in scaleAndTranslate. The <code>scale</code>, <code>previousScale</code>, <code>translate</code> and <code>previousTranslate</code> properties contain the new and previous scale and translate, respectively.
 * 
 * mxEvent.SCALE fires after the scale was changed in setScale. The <code>scale</code> and <code>previousScale</code> properties contain the new and previous scale.
 * 
 * mxEvent.TRANSLATE fires after the translate was changed in setTranslate. The <code>translate</code> and <code>previousTranslate</code> properties contain the new and previous value for translate.
 * 
 * mxEvent.UP and mxEvent.DOWN fire if the current root is changed by executing a mxCurrentRootChange. The event name depends on the location of the root in the cell hierarchy with respect to the current root. The <code>root</code> and <code>previous</code> properties contain the new and previous root, respectively.
 */
public class GraphView extends mxGraphView {

	private double horizontalOffset = 0;
	private double verticalOffset = 0;
	private boolean isOffsetChangeable = false;

	public GraphView(mxGraph graph) {
		super(graph);
		System.out.println("---------> GraphView wird erzeugt!!!");
	}
	
	

	@Override
	public void validateBounds(mxCellState parentState, Object cell) {
		PNGraphCell graphCell = null;
		if(cell instanceof PNGraphCell){
		graphCell = (PNGraphCell) cell;
		if(graphCell.getId().equals("p2")){
			System.out.println("vorher: " + parentState.getAbsoluteOffset());
		}
		}
		
		mxIGraphModel model = graph.getModel();
		mxCellState state = getState(cell, true);

		if (state != null && state.isInvalid()) {
			if (!graph.isCellVisible(cell)) {
				removeState(cell);
			} else if (cell != currentRoot && parentState != null) {
				state.getAbsoluteOffset().setX(0);
				state.getAbsoluteOffset().setY(0);
				state.setOrigin(new mxPoint(parentState.getOrigin()));
				mxGeometry geo = graph.getCellGeometry(cell);

				if (geo != null) {
					if (!model.isEdge(cell)) {
						mxPoint origin = state.getOrigin();
						mxPoint offset = geo.getOffset();

						if (offset == null) {
							offset = new mxPoint();
						}

						if (geo.isRelative()) {
							origin.setX(origin.getX() + geo.getX() * parentState.getWidth() / scale + offset.getX());
							origin.setY(origin.getY() + geo.getY() * parentState.getHeight() / scale + offset.getY());
						} else {
							state.setAbsoluteOffset(new mxPoint(scale * offset.getX(), scale * offset.getY()));
							origin.setX(origin.getX() + geo.getX());
							origin.setY(origin.getY() + geo.getY());
						}
					}

					// Updates the cell state's bounds
					state.setX(scale * (translate.getX() + state.getOrigin().getX()));
					state.setY(scale * (translate.getY() + state.getOrigin().getY()));
					state.setWidth(scale * geo.getWidth());
					state.setHeight(scale * geo.getHeight());

					if (model.isVertex(cell)) {
						updateVertexLabelOffset(state);
					}

					// Updates the cached label
					updateLabel(state);
				}
			}

			// Applies child offset to origin
			mxPoint offset = graph.getChildOffsetForCell(cell);

			if (offset != null) {
				state.getOrigin().setX(state.getOrigin().getX() + offset.getX());
				state.getOrigin().setY(state.getOrigin().getY() + offset.getY());
			}
		}

		// Recursively validates the child bounds
		if (state != null && (!graph.isCellCollapsed(cell) || cell == currentRoot)) {
			int childCount = model.getChildCount(cell);

			for (int i = 0; i < childCount; i++) {
				validateBounds(state, model.getChildAt(cell, i));
			}
		}
		
		if(cell instanceof PNGraphCell){
		if(graphCell.getId().equals("p2")){
			System.out.println("nachher: " + parentState.getAbsoluteOffset());
		}
		}
	}



//	@Override
//	/**
//	 * Updates the absoluteOffset of the given vertex cell state. This takes
//	 * into account the label position styles.
//	 * 
//	 * @param state Cell state whose absolute offset should be updated.
//	 */
//	public void updateVertexLabelOffset(mxCellState state) {
////		mxCell cell = (mxCell) state.getCell();
////
////		String horizontal = mxUtils.getString(state.getStyle(), mxConstants.STYLE_LABEL_POSITION, mxConstants.ALIGN_CENTER);
////
////		if (horizontal.equals(mxConstants.ALIGN_LEFT)) {
////			state.getAbsoluteOffset().setX(state.getAbsoluteOffset().getX() - state.getWidth());
////		} else if (horizontal.equals(mxConstants.ALIGN_RIGHT)) {
////			state.getAbsoluteOffset().setX(state.getAbsoluteOffset().getX() + state.getWidth());
////		}
////
////		String vertical = mxUtils.getString(state.getStyle(), mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_MIDDLE);
////
////		if (vertical.equals(mxConstants.ALIGN_TOP)) {
////			state.getAbsoluteOffset().setY(state.getAbsoluteOffset().getY() - state.getHeight());
////		} else if (vertical.equals(mxConstants.ALIGN_BOTTOM)) {
////			state.getAbsoluteOffset().setY(state.getAbsoluteOffset().getY() + state.getHeight());
////		}
////
////		if (cell.getStyle() != null && cell instanceof mxPlace) {
////			AnnotationGraphics placeLabel = netContainer.getPetriNetGraphics().getPlaceLabelAnnotationGraphics().get(cell.getId());
////			if (placeLabel != null) {
////				if (isOffsetChangeable) {
////					state.getAbsoluteOffset().setX(state.getAbsoluteOffset().getX() + getHorizontalOffset());
////					state.getAbsoluteOffset().setY(state.getAbsoluteOffset().getY() + getVerticalOffset());
////				} else {
////					state.getAbsoluteOffset().setX(placeLabel.getOffset().getX());
////					state.getAbsoluteOffset().setY(placeLabel.getOffset().getY());
////				}
////				;
////			}
////		}
////		if (cell instanceof mxTransition) {
////			AnnotationGraphics transitionLabel = netContainer.getPetriNetGraphics().getTransitionLabelAnnotationGraphics().get(cell.getId());
////			if (transitionLabel != null) {
////				if (isOffsetChangeable) {
////					state.getAbsoluteOffset().setX(state.getAbsoluteOffset().getX() + getHorizontalOffset());
////					state.getAbsoluteOffset().setY(state.getAbsoluteOffset().getY() + getVerticalOffset());
////				} else {
////					state.getAbsoluteOffset().setX(transitionLabel.getOffset().getX());
////					state.getAbsoluteOffset().setY(transitionLabel.getOffset().getY());
////				}
////				;
////			}
////		}
////
////		addAnnotationGraphics(state);
//
//	}

	public void setVerticalOffset(double verticalOffset) {
		this.verticalOffset = verticalOffset;
	}

	private double getVerticalOffset() {
		// TODO Auto-generated method stub
		return verticalOffset;
	}

	public void setHorizontalOffset(double horizontalOffset) {
		this.horizontalOffset = horizontalOffset;
	}

	private double getHorizontalOffset() {
		return horizontalOffset;
	}

//	/**
//	 * @param state
//	 * @param n
//	 * @param annotation
//	 */
//	public void addAnnotationGraphics(mxCellState state) {
//		mxCell cell = (mxCell) state.getCell();
//
//		if (cell.getParent() != null) {
//			cell.getParent().setValue(netContainer);
//			mxPoint offset = state.getAbsoluteOffset();
//			AnnotationGraphics annotation = null;
//			try {
//				annotation = new AnnotationGraphics(new Offset((int) offset.getX(), (int) offset.getY()), new Fill(), new Line(), new Font());
//			} catch (ParameterException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			Map<String, AnnotationGraphics> labelAnnotationGraphics = null;
//			if (cell.getStyle() != null && cell instanceof mxPlace) {
//
//				labelAnnotationGraphics = (netContainer.getPetriNetGraphics().getPlaceLabelAnnotationGraphics() == null) ? new HashMap<String, AnnotationGraphics>() : netContainer.getPetriNetGraphics().getPlaceLabelAnnotationGraphics();
//				labelAnnotationGraphics.put(cell.getId(), annotation);
//				netContainer.getPetriNetGraphics().setPlaceLabelAnnotationGraphics(labelAnnotationGraphics);
//			}
//			if (cell instanceof mxTransition) {
//				labelAnnotationGraphics = (netContainer.getPetriNetGraphics().getTransitionLabelAnnotationGraphics() == null) ? new HashMap<String, AnnotationGraphics>() : netContainer.getPetriNetGraphics().getTransitionLabelAnnotationGraphics();
//				labelAnnotationGraphics.put(cell.getId(), annotation);
//				netContainer.getPetriNetGraphics().setTransitionLabelAnnotationGraphics(labelAnnotationGraphics);
//			}
//			if (cell.getId() != null && cell.getId().startsWith("arc")) {
//				labelAnnotationGraphics = (netContainer.getPetriNetGraphics().getArcAnnotationGraphics() == null) ? new HashMap<String, AnnotationGraphics>() : netContainer.getPetriNetGraphics().getArcAnnotationGraphics();
//				labelAnnotationGraphics.put(cell.getId(), annotation);
//				netContainer.getPetriNetGraphics().setArcAnnotationGraphics(labelAnnotationGraphics);
//			}
//
//		}
//
//	}
//
//	public void isOffsetChangeable(boolean isChangeable) {
//		isOffsetChangeable = isChangeable;
//
//	}

}
