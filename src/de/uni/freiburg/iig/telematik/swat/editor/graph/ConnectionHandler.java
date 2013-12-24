/**
 * $Id: mxConnectionHandler.java,v 1.1 2012/11/15 13:26:44 gaudenz Exp $
 * Copyright (c) 2008, Gaudenz Alder
 */
package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import com.mxgraph.model.mxICell;
import com.mxgraph.swing.handler.mxConnectPreview;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.validate.ParameterException;

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
//					try {
						mxICell cell = (mxICell) previewState.getCell();
						Object src = cell.getTerminal(true);
						Object trg = cell.getTerminal(false);

						if (src != null) {
							((mxICell) src).removeEdge(cell, true);
						}

						if (trg != null) {
							((mxICell) trg).removeEdge(cell, false);
						}

//						if (commit) {
//							AbstractFlowRelation relation = null;
//
//							switch (getSource().getType()) {
//							case PLACE:
//								relation = getGraphComponent().getGraph().getNetContainer().getPetriNet().addFlowRelationPT(((mxICell) src).getId(), ((mxICell) trg).getId());
//								break;
//							case TRANSITION:
//								relation = getGraphComponent().getGraph().getNetContainer().getPetriNet().addFlowRelationTP(((mxICell) src).getId(), ((mxICell) trg).getId());
//								break;
//							}
//							PNGraphCell newCell = getGraphComponent().getGraph().createArcCell(relation.getName(), getGraphComponent().getGraph().getArcConstraint(relation), new Vector<Position>(), MXConstants.getNodeStyle(PNComponent.ARC, null, null));
//							getGraphComponent().getGraph().addArcReference(relation, newCell);
//							result = graph.addEdge(newCell, graph.getDefaultParent(), graph.getCell(relation.getSource()), graph.getCell(relation.getTarget()), null);
//
//						}

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
//					} catch (ParameterException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					} finally {
//						graph.getModel().endUpdate();
//					}
						graph.getModel().endUpdate();
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
				
				PNGraphCell selectionCell = null;


				if (connectPreview.isActive() && (marker.hasValidState() || isCreateTarget() || graph.isAllowDanglingEdges())) {
					graph.getModel().beginUpdate();

					try {

						if (!marker.hasValidState() && isCreateTarget()) {
							
							PNGraphCell targetCell = null;
							switch (getSource().getType()) {
							case PLACE:
								targetCell = (PNGraphCell) ((PNGraph) graphComponent.getGraph()).addNewTransition(graphComponent.getPointForEvent(e));
								break;
							case TRANSITION:
								targetCell = (PNGraphCell) ((PNGraph) graphComponent.getGraph()).addNewPlace(graphComponent.getPointForEvent(e));
								break;
							}
							selectionCell = targetCell;
							
							Object edge = ((PNGraph) graphComponent.getGraph()).addNewFlowRelation(getSource(), targetCell);
							
//							dropTarget = graph.getDropTarget(new Object[] { targetCell }, e.getPoint(), graphComponent.getCellAt(e.getX(), e.getY()));
//							if (targetCell != null) {
//								// Disables edges as drop targets if the target
//								// cell was created
//								if (dropTarget == null || !graph.getModel().isEdge(dropTarget)) {
//									mxCellState pstate = graph.getView().getState(dropTarget);
//
//									if (pstate != null) {
//										mxGeometry geo = graph.getModel().getGeometry(targetCell);
//
//										mxPoint origin = pstate.getOrigin();
//										geo.setX(geo.getX() - origin.getX());
//										geo.setY(geo.getY() - origin.getY());
//									}
//								} else {
//									dropTarget = graph.getDefaultParent();
//								}
//
//								graph.addCells(new Object[] { targetCell }, dropTarget);
//							}

							// FIXME: Here we pre-create the state for the
							// vertex to be
							// inserted in order to invoke update in the
							// connectPreview.
							// This means we have a cell state which should be
							// created
							// after the model.update, so this should be fixed.
							mxCellState targetState = graph.getView().getState(targetCell, true);
							connectPreview.update(e, targetState, e.getX(), e.getY());

							eventSource.fireEvent(new mxEventObject(mxEvent.CONNECT, "cell", edge, "event", e, "target", targetCell));
						}

						connectPreview.stop(graphComponent.isSignificant(dx, dy), e);
						e.consume();
					} catch (ParameterException e1) {
						System.out.println(getSource().getType() + "-Vertex could not be created");
						e1.printStackTrace();
					} finally {
						graph.getModel().endUpdate();
						if(selectionCell != null){
							((PNGraph) graphComponent.getGraph()).setSelectionCell(selectionCell);
						}
					}
				}

			}
		}

		reset();
	}

}
