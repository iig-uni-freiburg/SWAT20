package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxCellHandler;
import com.mxgraph.swing.handler.mxVertexHandler;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

public class VertexHandler extends mxVertexHandler {

	public VertexHandler(mxGraphComponent graphComponent, mxCellState state) {
		super(graphComponent, state);
		// TODO Auto-generated constructor stub
	}
	//TODO: Right behaviour when moving labels
@Override
/**
 * 
 */
public void mouseDragged(MouseEvent e)
{
	if (!e.isConsumed() && first != null)
	{
		gridEnabledEvent = graphComponent.isGridEnabledEvent(e);
		constrainedEvent = graphComponent.isConstrainedEvent(e);

		double dx = e.getX() - first.x;
		double dy = e.getY() - first.y;
		double size = Math.min(dx, dy);
		dx = size;
		dy = size;
		System.out.println("1");
		if (isLabel(index))
		{
			System.out.println("index");
			mxPoint pt = new mxPoint(e.getPoint());

			if (gridEnabledEvent)
			{
				System.out.println("grid");
				pt = graphComponent.snapScaledPoint(pt);
			}

			int idx = (int) Math.round(pt.getX() - first.x);
			int idy = (int) Math.round(pt.getY() - first.y);

			if (constrainedEvent)
				System.out.println("constr");
			{
				if (Math.abs(idx) > Math.abs(idy))
				{
					idy = 0;
				}
				else
				{
					idx = 0;
				}
			}

			Rectangle rect = state.getLabelBounds().getRectangle();
			rect.translate(idx, idy);
			preview.setBounds(rect);
		}
		else
		{
			System.out.println("else");
			mxGraph graph = graphComponent.getGraph();
			double scale = graph.getView().getScale();

			if (gridEnabledEvent)
			{
				dx = graph.snap(dx / scale) * scale;
				dy = graph.snap(dy / scale) * scale;
			}

			mxRectangle bounds = union(getState(), dx, dy, index);
			bounds.setWidth(bounds.getWidth() + 1);
			bounds.setHeight(bounds.getHeight() + 1);
			preview.setBounds(bounds.getRectangle());
		}

		if (!preview.isVisible() && graphComponent.isSignificant(dx, dy))
		{
			preview.setVisible(true);
		}

		e.consume();
	}
}

@Override
protected void resizeCell(MouseEvent e)
{
	mxGraph graph = graphComponent.getGraph();
	double scale = graph.getView().getScale();

	Object cell = state.getCell();
	mxGeometry geometry = graph.getModel().getGeometry(cell);

	if (geometry != null)
	{
		double dx = (e.getX() - first.x) / scale;
		double dy = (e.getY() - first.y) / scale;
		double size = Math.min(dx, dy);
		dx = size;
		dy = size;
		if (isLabel(index))
		{
			geometry = (mxGeometry) geometry.clone();

			if (geometry.getOffset() != null)
			{
				dx += geometry.getOffset().getX();
				dy += geometry.getOffset().getY();
			}

			if (gridEnabledEvent)
			{
				dx = graph.snap(dx);
				dy = graph.snap(dy);
			}

			geometry.setOffset(new mxPoint(dx, dy));
			graph.getModel().setGeometry(cell, geometry);
		}
		else
		{
			mxRectangle bounds = union(geometry, dx, dy, index);
			Rectangle rect = bounds.getRectangle();

			// Snaps new bounds to grid (unscaled)
			if (gridEnabledEvent)
			{
				int x = (int) graph.snap(rect.x);
				int y = (int) graph.snap(rect.y);
				rect.width = (int) graph.snap(rect.width - x + rect.x);
				rect.height = (int) graph.snap(rect.height - y + rect.y);
				rect.x = x;
				rect.y = y;
			}

			graph.resizeCell(cell, new mxRectangle(rect));
		}
	}
}
}
