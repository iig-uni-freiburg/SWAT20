/**
 * $Id: mxConnectionHandler.java,v 1.1 2012/11/15 13:26:44 gaudenz Exp $
 * Copyright (c) 2008, Gaudenz Alder
 */
package de.unifreiburg.iig.bpworkbench2.editor.graph;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.swing.mxGraphComponent.mxGraphControl;
import com.mxgraph.swing.util.mxMouseAdapter;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;

/**
 * Connection handler creates new connections between cells. This control is used to display the connector
 * icon, while the preview is used to draw the line.
 * 
 * mxEvent.CONNECT fires between begin- and endUpdate in mouseReleased. The <code>cell</code>
 * property contains the inserted edge, the <code>event</code> and <code>target</code> 
 * properties contain the respective arguments that were passed to mouseReleased.
 */
public class ConnectionHandler extends mxConnectionHandler
{

	public ConnectionHandler(mxGraphComponent arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
@Override
	public void mouseReleased(MouseEvent e)
	{
		if (isActive())
		{
			if (error != null)
			{
				if (error.length() > 0)
				{
					JOptionPane.showMessageDialog(graphComponent, error);
				}
			}
			else if (first != null)
			{
				mxGraph graph = graphComponent.getGraph();
				double dx = first.getX() - e.getX();
				double dy = first.getY() - e.getY();
	
				if (connectPreview.isActive()
						&& (marker.hasValidState() || isCreateTarget() || graph
								.isAllowDanglingEdges()))
				{
					graph.getModel().beginUpdate();
	
					try
					{
						Object dropTarget = null;
	
						if (!marker.hasValidState() && isCreateTarget())
						{
							Object vertex = null;
						
							if(source.getCell() instanceof mxPlace)
							vertex = createTargetTransition(e);
							
							if(source.getCell() instanceof mxTransition)
								 vertex = createTargetPlace(e);

							dropTarget = graph.getDropTarget(
									new Object[] { vertex }, e.getPoint(),
									graphComponent.getCellAt(e.getX(), e.getY()));
	
							if (vertex != null)
							{
								// Disables edges as drop targets if the target cell was created
								if (dropTarget == null
										|| !graph.getModel().isEdge(dropTarget))
								{
									mxCellState pstate = graph.getView().getState(
											dropTarget);
	
									if (pstate != null)
									{
										mxGeometry geo = graph.getModel()
												.getGeometry(vertex);
	
										mxPoint origin = pstate.getOrigin();
										geo.setX(geo.getX() - origin.getX());
										geo.setY(geo.getY() - origin.getY());
									}
								}
								else
								{
									dropTarget = graph.getDefaultParent();
								}
	
								graph.addCells(new Object[] { vertex }, dropTarget);
							}
	
							// FIXME: Here we pre-create the state for the vertex to be
							// inserted in order to invoke update in the connectPreview.
							// This means we have a cell state which should be created
							// after the model.update, so this should be fixed.
							mxCellState targetState = graph.getView().getState(
									vertex, true);
							connectPreview.update(e, targetState, e.getX(),
									e.getY());
						}
	
						Object cell = connectPreview.stop(
								graphComponent.isSignificant(dx, dy), e);
	
						if (cell != null)
						{
							graphComponent.getGraph().setSelectionCell(cell);
							eventSource.fireEvent(new mxEventObject(
									mxEvent.CONNECT, "cell", cell, "event", e,
									"target", dropTarget));
						}
	
						e.consume();
					}
					finally
					{
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
		mxPlace cell = new mxPlace(null, new mxGeometry(graphComponent
				.getGraph().snap(point.getX() - 30 / 2),  graphComponent
						.getGraph().snap(point.getY() - 30 / 2), 30, 30),
						  ((Graph)graphComponent.getGraph()).getPlaceShape());
		cell.setVertex(true);
		return cell;
	}
private Object createTargetTransition(MouseEvent e) {
	System.out.println("createTargetTransition");
	mxPoint point = graphComponent.getPointForEvent(e);
	mxTransition cell = new mxTransition(null, new mxGeometry(graphComponent
			.getGraph().snap(point.getX() - 30 / 2),  graphComponent
			.getGraph().snap(point.getY() - 30 / 2), 30, 30),
            ((Graph)graphComponent.getGraph()).getTransitionShape());
    cell.setVertex(true);
	return cell;
}

}
