/**
 * $Id: mxConnectionHandler.java,v 1.1 2012/11/15 13:26:44 gaudenz Exp $
 * Copyright (c) 2008, Gaudenz Alder
 */
package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.event.MouseEvent;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

import de.uni.freiburg.iig.telematik.swat.editor.menu.EditorProperties;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;


/**
 * Connection handler creates new connections between cells. This control is used to display the connector icon, while the preview is used to draw the line.
 * 
 * mxEvent.CONNECT fires between begin- and endUpdate in mouseReleased. The <code>cell</code> property contains the inserted edge, the <code>event</code> and <code>target</code> properties contain the respective arguments that were passed to mouseReleased.
 */
public class ConnectionHandler extends mxConnectionHandler {

	public ConnectionHandler(PNGraphComponent arg0) {
		super(arg0);
	}
	
	private PNGraphCell getSource(){
		return (PNGraphCell) source.getCell();
	}
	
	private PNGraphComponent getGraphComponent(){
		return (PNGraphComponent) graphComponent;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void mouseReleased(MouseEvent e) {
		if (isActive()) {
			if (error == null && first != null) {
				mxGraph graph = graphComponent.getGraph();
				double dx = first.getX() - e.getX();
				double dy = first.getY() - e.getY();

				if (connectPreview.isActive() && (marker.hasValidState() || isCreateTarget() || graph.isAllowDanglingEdges())) {
					graph.getModel().beginUpdate();

					try {
						Object dropTarget = null;

						if (!marker.hasValidState() && isCreateTarget()) {
							Object vertex = null;

							switch(getSource().getType()){
							case PLACE:
								vertex = createTargetTransition(e);
								break;
							case TRANSITION:
								vertex = createTargetPlace(e);
								break;
							}
								
							dropTarget = graph.getDropTarget(new Object[] { vertex }, e.getPoint(), graphComponent.getCellAt(e.getX(), e.getY()));

							if (vertex != null) {
								// Disables edges as drop targets if the target cell was created
								if (dropTarget == null || !graph.getModel().isEdge(dropTarget)) {
									mxCellState pstate = graph.getView().getState(dropTarget);

									if (pstate != null) {
										mxGeometry geo = graph.getModel().getGeometry(vertex);

										mxPoint origin = pstate.getOrigin();
										geo.setX(geo.getX() - origin.getX());
										geo.setY(geo.getY() - origin.getY());
									}
								} else {
									dropTarget = graph.getDefaultParent();
								}

								graph.addCells(new Object[] { vertex }, dropTarget);
							}

							// FIXME: Here we pre-create the state for the vertex to be
							// inserted in order to invoke update in the connectPreview.
							// This means we have a cell state which should be created
							// after the model.update, so this should be fixed.
							mxCellState targetState = graph.getView().getState(vertex, true);
							connectPreview.update(e, targetState, e.getX(), e.getY());
						}

						Object cell = connectPreview.stop(graphComponent.isSignificant(dx, dy), e);

						if (cell != null) {
							graphComponent.getGraph().setSelectionCell(cell);
							eventSource.fireEvent(new mxEventObject(mxEvent.CONNECT, "cell", cell, "event", e, "target", dropTarget));
						}

						e.consume();
					} finally {
						graph.getModel().endUpdate();
					}
				}
			}
		}

		reset();
	}

	private Object createTargetPlace(MouseEvent e) {
		System.out.println("createTargetPlace");
		mxPoint point = graphComponent.getPointForEvent(e);
		PNGraphCell cell = new PNGraphCell(null, getPlaceDimension(point),  MXConstants.DEFAULT_PLACE_SHAPE, PNComponent.PLACE);
		cell.setVertex(true);
		return cell;
	}
	
	private mxGeometry getPlaceDimension(mxPoint point){
		int placeSize = EditorProperties.getInstance().getDefaultPlaceSize();
		return new mxGeometry(graphComponent.getGraph().snap(point.getX() - placeSize / 2), graphComponent.getGraph().snap(point.getY() - placeSize / 2), placeSize, placeSize);
	}

	private Object createTargetTransition(MouseEvent e) {
		System.out.println("createTargetTransition");
		mxPoint point = graphComponent.getPointForEvent(e);
		PNGraphCell cell = new PNGraphCell(null, getTransitionDimension(point), MXConstants.DEFAULT_TRANSITION_SHAPE, PNComponent.TRANSITION);
		cell.setVertex(true);
		return cell;
	}
	
	private mxGeometry getTransitionDimension(mxPoint point){
		int transitionWidth = EditorProperties.getInstance().getDefaultTransitionWidth();
		int transitionHeight = EditorProperties.getInstance().getDefaultTransitionHeight();
		return new mxGeometry(graphComponent.getGraph().snap(point.getX() - transitionWidth / 2), graphComponent.getGraph().snap(point.getY() - transitionHeight / 2), transitionWidth, transitionHeight);
	}

}
