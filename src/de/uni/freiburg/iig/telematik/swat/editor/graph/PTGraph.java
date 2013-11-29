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
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.TokenGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PTProperties;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreeNode;

/**
 * @author julius
 *
 */
public class PTGraph extends PNGraph {

	public PTGraph(GraphicalPTNet netContainer, PTProperties properties) throws ParameterException {
		super(netContainer, properties);
	}

	@Override
	public GraphicalPTNet getNetContainer() {
		return (GraphicalPTNet) super.getNetContainer();
	}

	@Override
	protected PTProperties getPNProperties() {
		return (PTProperties) super.getPNProperties();
	}
	
	@SuppressWarnings("rawtypes") 
	@Override
	protected String getArcConstraint(AbstractFlowRelation relation) {
		return String.valueOf(((PTFlowRelation) relation).getWeight());
	}

	@Override
	public void updatePlaceState(PNGraphCell cell, Object state) throws ParameterException {
		Integer tokens =  new Integer((String) state);
		PTMarking initialMarking = getNetContainer().getPetriNet().getInitialMarking();
		initialMarking.set(cell.getId(), new Integer(tokens));
		getNetContainer().getPetriNet().setInitialMarking(initialMarking);
	}
	
	@Override
	/**
	 * @param cell
	 * @param circularPointGroup
	 * @return
	 */
	protected Integer getPlaceStateForCell(PNGraphCell cell, CircularPointGroup circularPointGroup) {
		PTPlace place = (PTPlace) getNetContainer().getPetriNet().getPlace(cell.getId());
		circularPointGroup.addPoints(PColor.black, place.getState());
		return place.getState() ;
	}

	

	/** Method for incrementing or decrementing the current #PTMarking of the given #PTPlace
	 * @param cell
	 * @param wheelRotation
	 * @throws ParameterException
	 */
	public void inOrDecrementPlaceState(PNGraphCell cell, int wheelRotation) throws ParameterException {
		PTMarking initialMarking = getNetContainer().getPetriNet().getInitialMarking();
		Integer current = initialMarking.get(cell.getId());
		int capacity = getNetContainer().getPetriNet().getPlace(cell.getId()).getCapacity();	
		if(current != null && (capacity<0 ^ capacity>=(current - wheelRotation))) // null means here that the value "zero" has been set before / capacity -1 means infinite
		initialMarking.set(cell.getId(),  current - wheelRotation);
		else if(current == null && wheelRotation < 0 ) // create new Marking
		initialMarking.set(cell.getId(),  1);
	
		getNetContainer().getPetriNet().setInitialMarking(initialMarking);
		
	}



		  

}
