package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxImageCanvas;
import com.mxgraph.shape.mxEllipseShape;
import com.mxgraph.shape.mxIShape;
import com.mxgraph.shape.mxStencilRegistry;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraphSelectionModel;

import de.invation.code.toval.graphic.CircularPointGroup;
import de.invation.code.toval.graphic.GraphicUtils;
import de.invation.code.toval.graphic.PColor;
import de.invation.code.toval.graphic.Position;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.CPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.TokenGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.swat.editor.properties.CPNProperties;
import de.uni.freiburg.iig.telematik.swat.editor.properties.CPNProperties;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreeNode;

/**
 * @author julius
 * 
 */
public class CPNGraph extends PNGraph {

	public CPNGraph(GraphicalCPN graphicalCPN, CPNProperties cpnProperties) throws ParameterException {
		super(graphicalCPN, cpnProperties);
	}

	@Override
	public GraphicalCPN getNetContainer() {
		return (GraphicalCPN) super.getNetContainer();
	}

	@Override
	protected CPNProperties getPNProperties() {
		return (CPNProperties) super.getPNProperties();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected String getArcConstraint(AbstractFlowRelation relation) throws ParameterException {
		Map<String, Color> colors = getNetContainer().getPetriNetGraphics().getColors();
		String arcString = "";
		for (Entry<String, Color> color : colors.entrySet()) {
			int colorNumber = ((CPNFlowRelation) relation).getConstraint(color.getKey());
			if (colorNumber > 0)
				arcString += color.getKey() + ": " + String.valueOf(((CPNFlowRelation) relation).getConstraint(color.getKey())) + "\n";
		}
		return arcString;
	}

	@Override
	public void updatePlaceState(PNGraphCell cell, Multiset<String> state) throws ParameterException {
		CPNMarking initialMarking = getNetContainer().getPetriNet().getInitialMarking();
		initialMarking.set(cell.getId(), state);
		getNetContainer().getPetriNet().setInitialMarking(initialMarking);
	}

	@Override
	/**
	 * @param cell
	 * @param circularPointGroup
	 * @return
	 */
	protected Multiset<String> getPlaceStateForCell(PNGraphCell cell, CircularPointGroup circularPointGroup) {
		CPNPlace place = (CPNPlace) getNetContainer().getPetriNet().getPlace(cell.getId());
		return place.getState();
	}

	/**
	 * Method for incrementing or decrementing the current #PTMarking of the
	 * given #PTPlace
	 * 
	 * @param cell
	 * @param wheelRotation
	 * @throws ParameterException
	 */
	public void inOrDecrementPlaceState(PNGraphCell cell, int wheelRotation) throws ParameterException {
		// TODO adapt to CPN
	}

	@Override
	protected String getPlaceToolTip(PNGraphCell cell) {
		CPNPlace ptPlace = getNetContainer().getPetriNet().getPlace(cell.getId());
		return "Cap:" + ((ptPlace.getCapacity() == -1) ? "\u221e" : ptPlace.getCapacity());
	}

	@Override
	protected String getTransitionToolTip(PNGraphCell cell) {
		return "";
	}

	@Override
	protected String getArcToolTip(PNGraphCell cell) {
		return "";
	}

}
