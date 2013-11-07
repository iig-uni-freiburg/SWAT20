package de.unifreiburg.iig.bpworkbench2.editor.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxConnectionConstraint;
import com.mxgraph.view.mxGraph;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Dimension;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Position;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;

public class Graph extends mxGraph {

	AbstractGraphicalPN<?, ?, ?, ?, ?, ? , ?> netContainer = null;

	public Graph(AbstractGraphicalPN<?, ?, ?, ?, ?, ? , ?> netContainer) throws ParameterException {
		Validate.notNull(netContainer);
		
		setAlternateEdgeStyle("edgeStyle=mxEdgeStyle.ElbowConnector;elbow=vertical");
		setMultigraph(true);
		setCellsEditable(false);
		setDisconnectOnMove(false);
		setExtendParents(false); // disables extending parents after adding
		setVertexLabelsMovable(true);
	}

	@Override
	public void cellConnected(Object edge, Object terminal, boolean source,
			mxConnectionConstraint constraint) {
		super.cellConnected(edge, terminal, source, constraint);
		System.out.println(isCellBendable(edge)+"#########Bend");
		System.out.println(isCellEditable(edge));
		//setCellE
		// making edges parallel
		mxParallelEdgeLayout layout = new mxParallelEdgeLayout(this);
		layout.execute(getDefaultParent());

		// removing edges in place-to-place and transition-to-transition
		// connections
		mxCell edgeSource = (mxCell) ((mxCell) edge).getSource();
		mxCell edgeTarget = (mxCell) ((mxCell) edge).getTarget();
		if (edgeSource != null && edgeTarget != null) {
			if (edgeSource.getStyle().contentEquals(
					edgeTarget.getStyle().toString())) {
				removeCells(new Object[] { edge });
			}
			else {
				

				boolean isDNDgenerated = edgeSource.getId() == edgeTarget.getId();
				if(isInteger(edgeTarget.getId())) {
					edgeTarget.setId(edgeSource.getId());
				isDNDgenerated = true;	
				}
				if (edgeSource.getStyle().contentEquals(MXConstants.PNPlaceShape) && !isDNDgenerated && (((mxCell) edge).getValue() == ""))
					addPTArcToPN((mxCell) edge, edgeSource, edgeTarget, netContainer);
				if (edgeTarget.getStyle().contentEquals(MXConstants.PNTransitionShape) && isDNDgenerated) {
					String prefix = MXConstants.TransitionNamePrefix;
					String nodeName = null;
					try {nodeName = addPNNode(edgeTarget, netContainer, prefix);
					} catch (ParameterException e) {e.printStackTrace();}
					addLabelToNode(edgeTarget, netContainer, nodeName);
					addPTArcToPN((mxCell) edge, edgeSource, edgeTarget, netContainer);
					addGraphicalInfoToPN(edgeTarget, netContainer, nodeName);
					addGraphicalInfoToPN((mxCell) edge, netContainer, ((mxCell)edge).getId());


				}
				if (edgeSource.getStyle().contentEquals(MXConstants.PNTransitionShape)&& !isDNDgenerated && (((mxCell) edge).getValue() == ""))
					addTPArcToPN(edge, edgeSource, edgeTarget, netContainer);				
				if (edgeTarget.getStyle().contentEquals(MXConstants.PNPlaceShape)&& isDNDgenerated) {
					String prefix = MXConstants.PlaceNamePrefix;
					String nodeName = null;
					try {nodeName = addPNNode(edgeTarget, netContainer, prefix);
					} catch (ParameterException e) {e.printStackTrace();}
					addLabelToNode(edgeTarget, netContainer, nodeName);
					addTPArcToPN(edge, edgeSource, edgeTarget, netContainer);
					addGraphicalInfoToPN(edgeTarget, netContainer, nodeName);
					addGraphicalInfoToPN((mxCell) edge, netContainer, ((mxCell)edge).getId());
				}
			}
		}			
	}

	@Override
	public void cellsAdded(Object[] cells, Object parent, Integer index,
			Object source, Object target, boolean absolute) {
		super.cellsAdded(cells, parent, index, source, target, absolute);
		
		addLabelAndInfo(cells);
	}



	/**
	 * @param cells
	 */
	public void addLabelAndInfo(Object[] cells) {
		for (Object object : cells) {
			addLabelAndInfo(object);
		}
	}


	/**
	 * @param cell
	 * @param n
	 * @param nodeName
	 */
	public void addGraphicalInfoToPN(mxCell cell,
			AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> n, String nodeName) {
		
		Line line = new Line();
		Fill fill = new Fill();
		Dimension dimension = new Dimension(
				Dimension.DEFAULT_DIMENSION_X,
				Dimension.DEFAULT_DIMENSION_Y);
		double x = cell.getGeometry().getCenterX();
		double y = cell.getGeometry().getCenterY();
		Position position = new Position(x, y);
		NodeGraphics nodeGraphics = null;
		try {nodeGraphics = new NodeGraphics(position, dimension, fill, line);
		} catch (ParameterException e) {e.printStackTrace();}
		
		
		if(cell.getStyle().contentEquals(MXConstants.PNPlaceShape)){
			if(n.getPetriNetGraphics().getPlaceGraphics() == null){
				n.getPetriNetGraphics().setPlaceGraphics(
						 new HashMap<String, NodeGraphics>());
			}
			n.getPetriNetGraphics().getPlaceGraphics().put(nodeName, nodeGraphics);
			
		}
		if(cell.getStyle().contentEquals(MXConstants.PNTransitionShape)){
			if(n.getPetriNetGraphics().getTransitionGraphics() == null){
				n.getPetriNetGraphics().setTransitionGraphics(
						 new HashMap<String, NodeGraphics>());
			}
			n.getPetriNetGraphics().getTransitionGraphics().put(nodeName, nodeGraphics);
			
		}
//		try {Vector<Position> positions = new Vector<Position>();
		
//		positions.add(new Position(cell.getGeometry()))
//		arcGraphics = new ArcGraphics(positions, line);
//		} catch (ParameterException e) {e.printStackTrace();}
		if(cell.getId().startsWith("arc")){
		List<mxPoint> list = cell.getGeometry().getPoints();
		list = new ArrayList<mxPoint>();
//		list.add(new mxPoint(50,50));
		cell.getGeometry().setPoints(list);
		Vector<Position> positions = new Vector<Position>();
		if(list !=null)
		for(mxPoint p:list){
			positions.add(new Position(p.getX(),p.getY()));
		}

		ArcGraphics arcGraphics = null;
		try {arcGraphics = new ArcGraphics(positions, new Line());
		} catch (ParameterException e) {e.printStackTrace();}
		
			if(n.getPetriNetGraphics().getArcGraphics() == null){
				n.getPetriNetGraphics().setArcGraphics(new HashMap<String, ArcGraphics>());
			}
			n.getPetriNetGraphics().getArcGraphics().put(cell.getId(), arcGraphics);
			Map<String, AnnotationGraphics> arcAnno = n.getPetriNetGraphics().getArcAnnotationGraphics();
		}
		
	}

	// }

	@Override
	public void cellsRemoved(Object[] cells) {
		super.cellsRemoved(cells);
		for (Object object : cells) {
			mxCell cell = (mxCell) object;
			if (cell.getStyle().contentEquals(MXConstants.PNPlaceShape)) {	
						try {
							netContainer.getPetriNet().removePlace(cell.getId());
							netContainer.getPetriNetGraphics().getPlaceGraphics().remove(cell.getId());
							netContainer.getPetriNetGraphics().getPlaceLabelAnnotationGraphics().remove(cell.getId());
						} catch (ParameterException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
				}
				if (cell.getStyle().contentEquals(MXConstants.PNTransitionShape)) {
						try {
							System.out.println(cell.getId());
							netContainer.getPetriNet().removeTransition(cell.getId());
							netContainer.getPetriNetGraphics().getTransitionGraphics().remove(cell.getId());
							netContainer.getPetriNetGraphics().getTransitionLabelAnnotationGraphics().remove(cell.getId());
						} catch (ParameterException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
				if (cell.getId().startsWith("arc")) {	
						try {
							netContainer.getPetriNet().removeFlowRelation(cell.getId());
//							n.getPetriNetGraphics().getArcGraphics().remove(cell.getId());
						} catch (ParameterException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			}
		}
	


//	@Override
//	public void cellsResized(Object[] cells, mxRectangle[] bounds) {
//		super.cellsResized(cells, bounds);
//
//		// handling label position
//		for (Object object : cells) {
//			mxCell cell = (mxCell) object;
//			// TODO: Kein labelposition auffindbar
//			mxCell child = (mxCell) cell.getChildAt(0);
//			mxGeometry geometry = new mxGeometry(0, 0, cell.getGeometry()
//					.getWidth(), 10);
//			geometry.setRelative(true); // important!
//			geometry.setOffset(new mxPoint(0,
//					cell.getGeometry().getHeight() + 5));
//			child.setGeometry(geometry);
//
//			if (cell.getChildCount() > 1) {
//				child = (mxCell) cell.getChildAt(1);
//				geometry = new mxGeometry(0, 0, cell.getGeometry().getWidth(),
//						10);
//				geometry.setRelative(true); // important!
//				geometry.setOffset(new mxPoint(0, -10));
//				child.setGeometry(geometry);
//			}
//		}
//	}
	@Override
	public void cellsMoved(Object[] cells, double dx, double dy,
			boolean disconnect, boolean constrain) {
		if (cells != null && (dx != 0 || dy != 0)) {
			model.beginUpdate();
			try {
				if (disconnect) {
					disconnectGraph(cells);
				}

				for (int i = 0; i < cells.length; i++) {
					translateCell(cells[i], dx, dy);

					if (constrain) {
						constrainChild(cells[i]);
					}
				}

				if (isResetEdgesOnMove()) {
					resetEdges(cells);
				}

				fireEvent(new mxEventObject(mxEvent.CELLS_MOVED, "cells",
						cells, "dx", dx, "dy", dy, "disconnect", disconnect));
				for (Object o : cells) {
					if (o instanceof mxCell) {
						mxCell cell = (mxCell) o;
						if (cell.getParent() != null) {
							NodeGraphics pG = null;
							if (cell.getStyle().contentEquals(
									MXConstants.PNPlaceShape)) {
								pG = netContainer.getPetriNetGraphics()
										.getPlaceGraphics().get(cell.getId());
								
								updatePositionInPN(cell, pG);}
							
						
						if (cell.getStyle().contentEquals(
								MXConstants.PNTransitionShape)) {
							 pG = netContainer.getPetriNetGraphics()
									.getTransitionGraphics().get(cell.getId());
							 updatePositionInPN(cell, pG);
						
						}
						}
				}
					
				}
				
			} finally {
				model.endUpdate();
			}
		}
	}
//	@Override
//	/**
//	 * Returns a string or DOM node that represents the label for the given
//	 * cell. This implementation uses <convertValueToString> if <labelsVisible>
//	 * is true. Otherwise it returns an empty string.
//	 * 
//	 * @param cell <mxCell> whose label should be returned.
//	 * @return Returns the label for the given cell.
//	 */
//	public String getLabel(Object cell) {
//		String result = "";
//
//		if (cell != null) {
//			mxCellState state = view.getState(cell);
//			Map<String, Object> style = (state != null) ? state.getStyle()
//					: getCellStyle(cell);
//
//			if (labelsVisible
//					&& !mxUtils.isTrue(style, mxConstants.STYLE_NOLABEL, false)) {
//				result = convertValueToString(cell);
//				if (result.contains("de.uni.freiburg.iig.telematik.sepia")) {
//					AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> n = (AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?>) ((mxCell) cell)
//							.getValue();
//					if (n.getPetriNet().getPlace(((mxCell) cell).getId()) != null) {
//						result = "";
//						getModel().beginUpdate();
//						{
//							mxGeometry geom = new mxGeometry(0, 0,
//									((mxCell) cell).getGeometry().getWidth(),
//									10);
//							geom.setOffset(new mxPoint(0, ((mxCell) cell)
//									.getGeometry().getHeight() + 5));
//							geom.setRelative(true);
//							mxCell label;
//							// if is for handling copy/paste
//							if (((mxCell) cell).getChildCount() == 0) {
//								label = new mxCell(n.getPetriNet()
//										.getPlace(((mxCell) cell).getId())
//										.getLabel(), geom,
//										"shape=none;fontSize=12");
//							} else {
//								label = (mxCell) ((mxCell) cell).getChildAt(0);
//								label.setValue(n.getPetriNet()
//										.getPlace(((mxCell) cell).getId())
//										.getLabel());
//							}
//							label.setVertex(true);
//							label.setConnectable(false);
//							((mxCell) cell).insert(label);
//						}
//						getModel().endUpdate();
//					}
//					if (n.getPetriNet().getTransition(((mxCell) cell).getId()) != null) {
//
//						result = "";
//						getModel().beginUpdate();
//						{
//							mxGeometry geom = new mxGeometry(0, 0,
//									((mxCell) cell).getGeometry().getWidth(),
//									10);
//							geom.setOffset(new mxPoint(0, ((mxCell) cell)
//									.getGeometry().getHeight() + 5));
//							geom.setRelative(true);
//							mxCell label;
//							// if is for handling copy/paste
//							if (((mxCell) cell).getChildCount() == 0) {
//								label = new mxCell(n.getPetriNet()
//										.getTransition(((mxCell) cell).getId())
//										.getLabel(), geom,
//										"shape=none;fontSize=12");
//							} else {
//								label = (mxCell) ((mxCell) cell).getChildAt(0);
//								label.setValue(n.getPetriNet()
//										.getTransition(((mxCell) cell).getId())
//										.getLabel());
//							}
//							label.setVertex(true);
//							label.setConnectable(false);
//							((mxCell) cell).insert(label);
//						}
//						getModel().endUpdate();
//					}
//				}
//			}
//
//		}
//
//		return result;
//	}

@Override
/**
 * Sets the new label for a cell. If autoSize is true then
 * <cellSizeUpdated> will be called.
 * 
 * @param cell Cell whose label should be changed.
 * @param value New label to be assigned.
 * @param autoSize Specifies if cellSizeUpdated should be called.
 */
public void cellLabelChanged(Object cell, Object value, boolean autoSize)
{
	model.beginUpdate();
	try
	{
		getModel().setValue(cell, value);
		
		mxCell mxcell = (mxCell) cell;
		if (mxcell.getStyle().contentEquals(MXConstants.PNPlaceShape)) {	
			netContainer.getPetriNet().getPlace(((mxCell) cell).getId()).setLabel((String) value);
			}
			if (mxcell.getStyle().contentEquals(MXConstants.PNTransitionShape)) {
				netContainer.getPetriNet().getTransition(((mxCell) cell).getId()).setLabel((String) value);
			}


//		 mxPoint offset = view.getState(cell).getAbsoluteOffset();
//		AnnotationGraphics annotation = new AnnotationGraphics(new Offset(offset.getX(), offset.getY()), new Fill(),new Line(),new Font());
//		Map<String, AnnotationGraphics> placeLabelAnnotationGraphics = null;
//		if(n.getPetriNetGraphics().getPlaceLabelAnnotationGraphics() == null)
//placeLabelAnnotationGraphics = new HashMap<String, AnnotationGraphics>() ;
//		else placeLabelAnnotationGraphics = n.getPetriNetGraphics().getPlaceLabelAnnotationGraphics();
//
//n.getPetriNetGraphics().setPlaceLabelAnnotationGraphics(placeLabelAnnotationGraphics );
		if (autoSize)
		{
			cellSizeUpdated(cell, false);
		}
	} catch (ParameterException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	finally
	{
		model.endUpdate();
	}
	System.out.println("Changed");
}

	public String getLabelPosition(Object cell) {
		String result = "";

		if (cell != null) {
			mxCellState state = view.getState(cell);
			System.out.println(state);
//			mxPoint offset = (state != null) ? state.getAbsoluteOffset()
//					: getChildOffsetForCell(cell);
//System.out.println(get + "offset");
//			if (labelsVisible
//					&& !mxUtils.isTrue(style, mxConstants.STYLE_NOLABEL, false)) {
//				System.out.println(getLabelPosition(cell) + "position");
//				result = convertValueToString(cell);
//				if (result.contains("de.uni.freiburg.iig.telematik.sepia")) {
//					AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> n = (AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?>) ((mxCell) cell)
//							.getValue();
//					if (n.getPetriNet().getPlace(((mxCell) cell).getId()) != null) {
//						result = "";
//						getModel().beginUpdate();
//						{
//							mxGeometry geom = new mxGeometry(0, 0,
//									((mxCell) cell).getGeometry().getWidth(),
//									10);
//							geom.setOffset(new mxPoint(0, ((mxCell) cell)
//									.getGeometry().getHeight() + 5));
//							geom.setRelative(true);
//							mxCell label;
//							// if is for handling copy/paste
//							if (((mxCell) cell).getChildCount() == 0) {
//								label = new mxCell(n.getPetriNet()
//										.getPlace(((mxCell) cell).getId())
//										.getLabel(), geom,
//										"shape=none;fontSize=12");
//							} else {
//								label = (mxCell) ((mxCell) cell).getChildAt(0);
//								label.setValue(n.getPetriNet()
//										.getPlace(((mxCell) cell).getId())
//										.getLabel());
//							}
//							label.setVertex(true);
//							label.setConnectable(false);
//							((mxCell) cell).insert(label);
//						}
//						getModel().endUpdate();
//					}
//					if (n.getPetriNet().getTransition(((mxCell) cell).getId()) != null) {
//
//						result = "";
//						getModel().beginUpdate();
//						{
//							mxGeometry geom = new mxGeometry(0, 0,
//									((mxCell) cell).getGeometry().getWidth(),
//									10);
//							geom.setOffset(new mxPoint(0, ((mxCell) cell)
//									.getGeometry().getHeight() + 5));
//							geom.setRelative(true);
//							mxCell label;
//							// if is for handling copy/paste
//							if (((mxCell) cell).getChildCount() == 0) {
//								label = new mxCell(n.getPetriNet()
//										.getTransition(((mxCell) cell).getId())
//										.getLabel(), geom,
//										"shape=none;fontSize=12");
//							} else {
//								label = (mxCell) ((mxCell) cell).getChildAt(0);
//								label.setValue(n.getPetriNet()
//										.getTransition(((mxCell) cell).getId())
//										.getLabel());
//							}
//							label.setVertex(true);
//							label.setConnectable(false);
//							((mxCell) cell).insert(label);
//						}
//						getModel().endUpdate();
//					}
//				}
//			}

		}

		return result;
	}


	/**
	 * @param cell
	 * @param pG
	 */
	public void updatePositionInPN(mxCell cell, NodeGraphics pG) {
		pG.getPosition().setX(
					cell.getGeometry().getCenterX());
			pG.getPosition().setY(
					cell.getGeometry().getCenterY());
	}

private void addNodeToMap(Map<Integer, String> a, String string, String prefix) {
		if (string.startsWith(prefix)
				&& isInteger(string.substring(prefix.length()))) {
			Integer integer = new Integer(string.substring(prefix.length()));
			a.put(integer, prefix + integer);
		}
	}
	public boolean isInteger(String string) {
		try {
			Integer.valueOf(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	public int getLowestIndex(SortedMap<Integer, String> a) {
		if (!a.isEmpty()) {
			for (int i = 1; i <= a.lastKey(); i++) {
				if (a.get(i) == null)
					return i;
			}
			;
			return a.lastKey() + 1;
		} else {
			return 1;
		}
	}

	

	public void addTPArcToPN(Object edge, mxCell edgeSource, mxCell edgeTarget,
			AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> n) {
		try {
			AbstractFlowRelation<?, ?, ?> fr = n.getPetriNet()
					.addFlowRelationTP(edgeSource.getId(),
							edgeTarget.getId());
			((mxCell) edge).setId(fr.getName());
			((mxCell) edge).setValue(((PTFlowRelation)fr).getWeight());
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addPTArcToPN(mxCell edge, mxCell edgeSource, mxCell edgeTarget,
			AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> n) {
		try {
			AbstractFlowRelation<?, ?, ?> fr = n.getPetriNet()
					.addFlowRelationPT(edgeSource.getId(),
							edgeTarget.getId());
			((mxCell) edge).setId(fr.getName());
			((mxCell) edge).setValue(((PTFlowRelation)fr).getWeight());
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void addLabelToNode(mxCell edgeTarget,
			AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> n, String nodeName) {
//		mxGeometry geom = new mxGeometry(0, 0, edgeTarget.getGeometry()
//				.getWidth(), 10);
////System.out.println(getLabel(edgeTarget) + "--------------------Label");
////setLabels
//
//System.out.println(getLabel(edgeTarget).getClass());
//
//		geom.setOffset(new mxPoint(0, edgeTarget.getGeometry().getHeight() + 5));
//		geom.setRelative(true);
//		mxCell label = null;
//
//		if (edgeTarget.getStyle().contentEquals(Constants.PNTransitionShape))
//			label = new mxCell(n.getPetriNet().getTransition(nodeName)
//					.getLabel(), geom, Constants.PNLabelStyle);
//		if (edgeTarget.getStyle().contentEquals(Constants.PNPlaceShape))
//			label = new mxCell(n.getPetriNet().getPlace(nodeName).getLabel(),
//					geom, Constants.PNLabelStyle);
//
//		label.setVertex(true);
//		label.setConnectable(false);
//		
//		edgeTarget.insert(label);
//		setVertexLabelsMovable(true);
		
		
		
		
	    Map<String, Object> vertexStyle = new HashMap<String, Object>();

	  vertexStyle = getStylesheet().getDefaultVertexStyle();
	  System.out.println(vertexStyle + "##########style");
	  vertexStyle.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);


	  getStylesheet().setDefaultVertexStyle(vertexStyle);
	System.out.println(getLabelPosition(edgeTarget));
	
	

	}

	public String addPNNode(mxCell edgeTarget, AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> n, String prefix)
			throws ParameterException {
		SortedMap<Integer, String> sortedMap = new TreeMap<Integer, String>();
		Collection<?> nodeCollection = null;
		if (edgeTarget.getStyle().contentEquals(MXConstants.PNTransitionShape)) {
			nodeCollection = n.getPetriNet().getTransitions();
			for (Object o : nodeCollection) {
				if (o instanceof PTTransition) {
					PTTransition transition = (PTTransition) o;
					addNodeToMap(sortedMap, transition.getName(), prefix);
				}
			}
			n.getPetriNet().addTransition(prefix + getLowestIndex(sortedMap));
		}

		if (edgeTarget.getStyle().contentEquals(MXConstants.PNPlaceShape)) {
			nodeCollection = n.getPetriNet().getPlaces();
			for (Object o : nodeCollection) {
				if (o instanceof PTPlace) {
					PTPlace place = (PTPlace) o;
					addNodeToMap(sortedMap, place.getName(), prefix);
				}
			}
			n.getPetriNet().addPlace(prefix + getLowestIndex(sortedMap));
		}

		return prefix +  getNodeName(edgeTarget, prefix, sortedMap);
	}


	public int getNodeName(mxCell edgeTarget, String prefix,
			SortedMap<Integer, String> sortedMap) {
		int id = getLowestIndex(sortedMap);
		addNodeToMap(sortedMap, prefix + id, prefix);
		edgeTarget.setValue(prefix + id);
		edgeTarget.setId(prefix + id);
		return id;
	}



	public void setPN(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> netContainer) {
		this.netContainer = netContainer;
		
	}

	/**
	 * Constructs a new customized view to be used in this graph, which also writes labelannotations in the PN-Model.
	 */
	@Override
	protected GraphView createGraphView()
	{
		return new GraphView(this);
	}

	/**
	 * Adds a new vertex into the given parent using value as the user object
	 * and the given coordinates as the geometry of the new vertex. The id and
	 * style are used for the respective properties of the new cell, which is
	 * returned.
	 * 
	 * @param parent Cell that specifies the parent of the new vertex.
	 * @param id Optional string that defines the Id of the new vertex.
	 * @param value Object to be used as the user object.
	 * @param x Integer that defines the x coordinate of the vertex.
	 * @param y Integer that defines the y coordinate of the vertex.
	 * @param width Integer that defines the width of the vertex.
	 * @param height Integer that defines the height of the vertex.
	 * @param style Optional string that defines the cell style.
	 * @param relative Specifies if the geometry should be relative.
	 * @return Returns the new vertex that has been inserted.
	 */
	public Object insertPNVertex(Object parent, String id, Object value,
								 double x, double y, double width, double height, String style, boolean relative){
		Object vertex = createVertex(parent, id, value, x, y, width, height, style, relative);
		Object result = addCell(vertex, parent);
		addLabelAndInfo(vertex);
		return result;
	}

	
	private void addLabelAndInfo(Object vertex) {
		if (vertex instanceof mxCell) {
			mxCell cell = (mxCell) vertex;


					if (cell.getStyle().contentEquals(
							MXConstants.PNPlaceShape)) {
						String prefix = MXConstants.PlaceNamePrefix;
						String nodeName = null;
						if(netContainer.getPetriNet().getPlace(cell.getId()) ==null){
						try {
							nodeName = addPNNode(cell, netContainer, prefix);
						} catch (ParameterException e) {e.printStackTrace();}
							addLabelToNode(cell, netContainer, nodeName);
							addGraphicalInfoToPN(cell, netContainer, nodeName);
						} else {
							cell.setValue(netContainer.getPetriNet().getPlace(cell.getId()).getLabel());
						}
							


					}

					if (cell.getStyle().contentEquals(
							MXConstants.PNTransitionShape)) {
						String prefix = MXConstants.TransitionNamePrefix;
						String nodeName = null;
						if(netContainer.getPetriNet().getTransition(cell.getId()) ==null){
						try {nodeName = addPNNode(cell, netContainer, prefix);
						} catch (ParameterException e) {e.printStackTrace();}
						
						addLabelToNode(cell, netContainer, nodeName);
						addGraphicalInfoToPN(cell, netContainer, nodeName);
						} else {
							cell.setValue(netContainer.getPetriNet().getTransition(cell.getId()).getLabel());
						}
					
					}
		
			
			refresh();

		}
	}
	
@Override
/**
 * Adds the cells to the parent at the given index, connecting each cell to
 * the optional source and target terminal. The change is carried out using
 * cellsAdded. This method fires mxEvent.ADD_CELLS while the transaction
 * is in progress.
 * 
 * @param cells Array of cells to be added.
 * @param parent Optional cell that represents the new parent. If no parent
 * is specified then the default parent is used.
 * @param index Optional index to insert the cells at. Default is to append.
 * @param source Optional source terminal for all inserted cells.
 * @param target Optional target terminal for all inserted cells.
 * @return Returns the cells that were added.
 */
public Object[] addCells(Object[] cells, Object parent, Integer index,
		Object source, Object target)
{
	if (parent == null)
	{
		parent = getDefaultParent();
	}

	if (index == null)
	{
		index = model.getChildCount(parent);
	}

	model.beginUpdate();
	try
	{
		cellsAdded(cells, parent, index, source, target, false, true);
		fireEvent(new mxEventObject(mxEvent.ADD_CELLS, "cells", cells,
				"parent", parent, "index", index, "source", source,
				"target", target));
	}
	finally
	{
		model.endUpdate();
	}

	return cells;
}

@Override
/**
 * Adds the specified cells to the given parent. This method fires
 * mxEvent.CELLS_ADDED while the transaction is in progress.
 */
public void cellsAdded(Object[] cells, Object parent, Integer index,
		Object source, Object target, boolean absolute, boolean constrain)
{
	if (cells != null && parent != null && index != null)
	{
		model.beginUpdate();
		try
		{
			mxCellState parentState = (absolute) ? view.getState(parent)
					: null;
			mxPoint o1 = (parentState != null) ? parentState.getOrigin()
					: null;
			mxPoint zero = new mxPoint(0, 0);

			for (int i = 0; i < cells.length; i++)
			{
				if (cells[i] == null)
				{
					index--;
				}
				else
				{
					Object previous = model.getParent(cells[i]);

					// Keeps the cell at its absolute location
					if (o1 != null && cells[i] != parent
							&& parent != previous)
					{
						mxCellState oldState = view.getState(previous);
						mxPoint o2 = (oldState != null) ? oldState
								.getOrigin() : zero;
						mxGeometry geo = model.getGeometry(cells[i]);

						if (geo != null)
						{
							double dx = o2.getX() - o1.getX();
							double dy = o2.getY() - o1.getY();

							geo = (mxGeometry) geo.clone();
							geo.translate(dx, dy);

							if (!geo.isRelative()
									&& model.isVertex(cells[i])
									&& !isAllowNegativeCoordinates())
							{
								geo.setX(Math.max(0, geo.getX()));
								geo.setY(Math.max(0, geo.getY()));
							}

							model.setGeometry(cells[i], geo);
						}
					}

					// Decrements all following indices
					// if cell is already in parent
					if (parent == previous)
					{
						index--;
					}

					model.add(parent, cells[i], index + i);

					// Extends the parent
					if (isExtendParentsOnAdd() && isExtendParent(cells[i]))
					{
						extendParent(cells[i]);
					}

					// Constrains the child
					if (constrain)
					{
						constrainChild(cells[i]);
					}

					// Sets the source terminal
					if (source != null)
					{
						cellConnected(cells[i], source, true, null);
					}

					// Sets the target terminal
					if (target != null)
					{
						cellConnected(cells[i], target, false, null);
					}
				}
			}

			fireEvent(new mxEventObject(mxEvent.CELLS_ADDED, "cells",
					cells, "parent", parent, "index", index, "source",
					source, "target", target, "absolute", absolute));

		}
		finally
		{
			model.endUpdate();
		}
	}
}

}
