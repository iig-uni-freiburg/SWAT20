package de.uni.freiburg.iig.telematik.swat.editor.graph.handler;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxCellHandler;
import com.mxgraph.swing.handler.mxEdgeHandler;
import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;

public class EdgeHandler extends mxEdgeHandler {

	@Override
	public String getToolTipText(MouseEvent e) {
		// TODO Auto-generated method stub
		return "<html>double-click to add waypoint <br>right-click on waypoint delete it</html>";
	}

	public EdgeHandler(mxGraphComponent graphComponent, mxCellState state) {
		super(graphComponent, state);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	/**
	 * 
	 */
	protected Rectangle createHandle(Point center)
	{
		return createHandle(center, MXConstants.EDGE_HANDLE_SIZE);
	}

}
