/**
 * $Id: mxConnectionHandler.java,v 1.1 2012/11/15 13:26:44 gaudenz Exp $
 * Copyright (c) 2008, Gaudenz Alder
 */
package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import java.util.Vector;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.handler.mxConnectPreview;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Position;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.swat.editor.menu.EditorProperties;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;

/**
 * Connection handler creates new connections between cells. This control is
 * used to display the connector icon, while the preview is used to draw the
 * line.
 * 
 * mxEvent.CONNECT fires between begin- and endUpdate in mouseReleased. The
 * <code>cell</code> property contains the inserted edge, the <code>event</code>
 * and <code>target</code> properties contain the respective arguments that were
 * passed to mouseReleased.
 */
public class ConnectionHandler extends mxConnectionHandler {

	public ConnectionHandler(PNGraphComponent arg0) {
		super(arg0);
	}

	private PNGraphCell getSource() {
		return (PNGraphCell) source.getCell();
	}

	private PNGraphComponent getGraphComponent() {
		return (PNGraphComponent) graphComponent;
	}

	@Override
	/**
	 * 
	 */
	protected mxConnectPreview createConnectPreview() {
		return new mxConnectPreview(getGraphComponent()) {
			@Override
			/**
			 *
			 */
			public Object stop(boolean commit, MouseEvent e) {
				Object result = (sourceState != null) ? sourceState.getCell() : null;

				if (previewState != null) {
					PNGraph graph = getGraphComponent().getGraph();

					graph.getModel().beginUpdate();
					try {
						mxICell cell = (mxICell) previewState.getCell();
						Object src = cell.getTerminal(true);
						Object trg = cell.getTerminal(false);

						if (src != null) {
							((mxICell) src).removeEdge(cell, true);
						}

						if (trg != null) {
							((mxICell) trg).removeEdge(cell, false);
						}

						if (commit) {
							// result = graph.addCell(cell, null, null, src,
							// trg);
							AbstractFlowRelation relation = null;
							try {
								switch (getSource().getType()) {
								case PLACE:
									relation = getGraphComponent().getGraph().getNetContainer().getPetriNet().addFlowRelationPT(((mxICell) src).getId(), ((mxICell) trg).getId());
									break;
								case TRANSITION:
									relation = getGraphComponent().getGraph().getNetContainer().getPetriNet().addFlowRelationTP(((mxICell) src).getId(), ((mxICell) trg).getId());
									break;
								}
							} catch (ParameterException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							PNGraphCell newCell = getGraphComponent().getGraph().createArcCell(relation.getName(), getGraphComponent().getGraph().getArcConstraint(relation), new Vector<Position>(),
									MXConstants.getStyle(PNComponent.ARC, null, null));
							getGraphComponent().getGraph().addArcReference(relation, newCell);
							result = graph.addEdge(newCell, graph.getDefaultParent(), graph.getCell(relation.getSource()), graph.getCell(relation.getTarget()), null);

						}

						fireEvent(new mxEventObject(mxEvent.STOP, "event", e, "commit", commit, "cell", (commit) ? result : null));

						// Clears the state before the model commits
						if (previewState != null) {
							Rectangle dirty = getDirtyRect();
							graph.getView().clear(cell, false, true);
							previewState = null;

							if (!commit && dirty != null) {
								getGraphComponent().getGraphControl().repaint(dirty);
							}
						}
					} finally {
						graph.getModel().endUpdate();
					}
				}

				sourceState = null;
				startPoint = null;

				return result;
			}

		};
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void mouseReleased(MouseEvent e) {
		if (isActive()) {
			if (error == null && first != null) {
				PNGraph graph = getGraphComponent().getGraph();
				double dx = first.getX() - e.getX();
				double dy = first.getY() - e.getY();

				Object edge = null;
				if (connectPreview.isActive() && (marker.hasValidState() || isCreateTarget() || graph.isAllowDanglingEdges())) {
					graph.getModel().beginUpdate();

					try {
						Object dropTarget = null;

						if (!marker.hasValidState() && isCreateTarget()) {
							Object vertex = null;

							switch (getSource().getType()) {
							case PLACE:
								vertex = createTargetTransition(e);
								break;
							case TRANSITION:
								vertex = createTargetPlace(e);
								break;
							}

							dropTarget = graph.getDropTarget(new Object[] { vertex }, e.getPoint(), graphComponent.getCellAt(e.getX(), e.getY()));

							if (vertex != null) {
								// Disables edges as drop targets if the target
								// cell was created
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

							// FIXME: Here we pre-create the state for the
							// vertex to be
							// inserted in order to invoke update in the
							// connectPreview.
							// This means we have a cell state which should be
							// created
							// after the model.update, so this should be fixed.
							mxCellState targetState = graph.getView().getState(vertex, true);
							connectPreview.update(e, targetState, e.getX(), e.getY());

						}

						edge = connectPreview.stop(graphComponent.isSignificant(dx, dy), e);

						if (edge != null) {
							graphComponent.getGraph().setSelectionCell(edge);
							eventSource.fireEvent(new mxEventObject(mxEvent.CONNECT, "cell", edge, "event", e, "target", dropTarget));
						}

						e.consume();
					} catch (ParameterException e1) {
						System.out.println(getSource().getType() + "-Vertex could not be created");
						e1.printStackTrace();
					} finally {
						graph.getModel().endUpdate();
					}
				}

			}
		}

		reset();
	}

	private Object createTargetPlace(MouseEvent e) throws ParameterException {
		mxPoint point = graphComponent.getPointForEvent(e);
		String prefix = MXConstants.PlaceNamePrefix;
		PNGraphCell newCell = null;
		Integer index = 0;
		while (getGraphComponent().getGraph().getNetContainer().getPetriNet().containsPlace(prefix + index)) {
			index++;
		}
		String nodeName = prefix + index;
		if (getGraphComponent().getGraph().getNetContainer().getPetriNet().addPlace(nodeName)) {
			AbstractPlace place = getGraphComponent().getGraph().getNetContainer().getPetriNet().getPlace(nodeName);
			newCell = getGraphComponent().getGraph().createPlaceCell(place.getName(), place.getLabel(), point.getX(), point.getY(), EditorProperties.getInstance().getDefaultPlaceSize(),
					EditorProperties.getInstance().getDefaultPlaceSize(), MXConstants.getStyle(PNComponent.PLACE, null, null));
			getGraphComponent().getGraph().addNodeReference(place, newCell);
			setGraphicsOfNewCell(point, newCell);

		}
		newCell.setVertex(true);
		return newCell;
	}

	private mxGeometry getPlaceDimension(mxPoint point) {
		int placeSize = EditorProperties.getInstance().getDefaultPlaceSize();
		return new mxGeometry(graphComponent.getGraph().snap(point.getX() - placeSize / 2), graphComponent.getGraph().snap(point.getY() - placeSize / 2), placeSize, placeSize);
	}

	private Object createTargetTransition(MouseEvent e) throws ParameterException {
		mxPoint point = graphComponent.getPointForEvent(e);
		String prefix = MXConstants.TransitionNamePrefix;
		PNGraphCell newCell = null;
		Integer index = 0;
		while (getGraphComponent().getGraph().getNetContainer().getPetriNet().containsTransition(prefix + index)) {
			index++;
		}
		String nodeName = prefix + index;
		if (getGraphComponent().getGraph().getNetContainer().getPetriNet().addTransition(nodeName)) {
			AbstractTransition transition = getGraphComponent().getGraph().getNetContainer().getPetriNet().getTransition(nodeName);
			newCell = getGraphComponent().getGraph().createTransitionCell(transition.getName(), transition.getLabel(), point.getX(), point.getY(),
					EditorProperties.getInstance().getDefaultTransitionWidth(), EditorProperties.getInstance().getDefaultTransitionHeight(), MXConstants.getStyle(PNComponent.TRANSITION, null, null));
			getGraphComponent().getGraph().addNodeReference(transition, newCell);
			setGraphicsOfNewCell(point, newCell);

		}
		newCell.setVertex(true);

		return newCell;
	}

	/**
	 * @param point
	 * @param newCell
	 * @throws ParameterException
	 */
	protected void setGraphicsOfNewCell(mxPoint point, PNGraphCell newCell) throws ParameterException {
		mxCellState state = getGraphComponent().getGraph().getView().getState(newCell, true);
		state.setX(point.getX());
		state.setY(point.getY());
		state.setWidth(EditorProperties.getInstance().getDefaultTransitionWidth());
		state.setHeight(EditorProperties.getInstance().getDefaultTransitionHeight());
		getGraphComponent().getGraph().setGraphics(state);
	}

	private mxGeometry getTransitionDimension(mxPoint point) {
		int transitionWidth = EditorProperties.getInstance().getDefaultTransitionWidth();
		int transitionHeight = EditorProperties.getInstance().getDefaultTransitionHeight();
		return new mxGeometry(graphComponent.getGraph().snap(point.getX() - transitionWidth / 2), graphComponent.getGraph().snap(point.getY() - transitionHeight / 2), transitionWidth,
				transitionHeight);
	}

}
