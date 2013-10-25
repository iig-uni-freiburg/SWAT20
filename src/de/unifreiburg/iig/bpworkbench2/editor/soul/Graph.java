package de.unifreiburg.iig.bpworkbench2.editor.soul;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.*;
import com.mxgraph.util.*;
import com.mxgraph.view.*;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Dimension;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Shape;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Style;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Position;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;

public class Graph extends mxGraph {

	private DataHolder dataHolder;
	AbstractGraphicalPN<?, ?, ?, ?, ?> n ;
	int i = 0;

	public Graph() {
		setAlternateEdgeStyle("edgeStyle=mxEdgeStyle.ElbowConnector;elbow=vertical");
		setMultigraph(true);
		setCellsEditable(false);
		setDisconnectOnMove(false);
		setExtendParents(false); // disables extending parents after adding
		setVertexLabelsMovable(true);
		  BasicStroke selectionStroke = new BasicStroke(1.0f);
		    Color selectionColor = Color.RED;


	}



	@Override
	public void cellConnected(Object edge, Object terminal, boolean source,
			mxConnectionConstraint constraint) {
		super.cellConnected(edge, terminal, source, constraint);

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
				
				AbstractGraphicalPN<?, ?, ?, ?, ?> n = (AbstractGraphicalPN<?, ?, ?, ?, ?>) (edgeSource)
						.getParent().getValue();
				boolean isDNDgenerated = edgeSource.getId() == edgeTarget.getId();
				if(isInteger(edgeTarget.getId())) {
					edgeTarget.setId(edgeSource.getId());
				isDNDgenerated = true;	
				}
				if (edgeSource.getStyle().contentEquals(Constants.PNPlaceShape) && !isDNDgenerated )
					addPTArcToPN(edge, edgeSource, edgeTarget, n);
				if (edgeTarget.getStyle().contentEquals(Constants.PNTransitionShape) && isDNDgenerated) {
					String prefix = Constants.TransitionNamePrefix;
					String nodeName = null;
					try {nodeName = addPNNode(edgeTarget, n, prefix);
					} catch (ParameterException e) {e.printStackTrace();}
					addLabelToNode(edgeTarget, n, nodeName);
					addPTArcToPN(edge, edgeSource, edgeTarget, n);
					addGraphicalInfoToPN(edgeTarget, n, nodeName);

				}
				if (edgeSource.getStyle().contentEquals(Constants.PNTransitionShape)&& !isDNDgenerated)
					addTPArcToPN(edge, edgeSource, edgeTarget, n);				
				if (edgeTarget.getStyle().contentEquals(Constants.PNPlaceShape)&& isDNDgenerated) {
					String prefix = Constants.PlaceNamePrefix;
					String nodeName = null;
					try {nodeName = addPNNode(edgeTarget, n, prefix);
					} catch (ParameterException e) {e.printStackTrace();}
					addLabelToNode(edgeTarget, n, nodeName);
					addTPArcToPN(edge, edgeSource, edgeTarget, n);
					addGraphicalInfoToPN(edgeTarget, n, nodeName);
				}
			}
		}
			
	}

	@Override
	public void cellsAdded(Object[] cells, Object parent, Integer index,
			Object source, Object target, boolean absolute) {
		super.cellsAdded(cells, parent, index, source, target, absolute);
		// creating names
		for (Object object : cells) {
			if (object instanceof mxCell) {
				mxCell cell = (mxCell) object;
				Object value = cell.getValue();
				if (cell.getParent().getValue() instanceof AbstractGraphicalPN<?, ?, ?, ?, ?>) {
					if ((value == null) && (cell.getParent() != null)) {
						n = (AbstractGraphicalPN<?, ?, ?, ?, ?>) cell.getParent().getValue();

						if (cell.getStyle().contentEquals(
								Constants.PNPlaceShape)) {
							String prefix = Constants.PlaceNamePrefix;
							String nodeName = null;
							try {nodeName = addPNNode(cell, n, prefix);
							} catch (ParameterException e) {e.printStackTrace();}
							
							
								PTMarking initialMarking = new PTMarking();
								int ressources = 1;
//								n.getPetriNet().getPlace(nodeName)
//										.setCapacity(ressources);
//								initialMarking.set(nodeName, ressources);
//
//								if (n.getPetriNet() instanceof PTNet) {
//									PTNet ptNet = (PTNet) n.getPetriNet();
//									ptNet.setInitialMarking(initialMarking);
//								}
//								;

								
							
						
							
							
								addLabelToNode(cell, n, nodeName);
								addGraphicalInfoToPN(cell, n, nodeName);
								


						}

						if (cell.getStyle().contentEquals(
								Constants.PNTransitionShape)) {
							String prefix = Constants.TransitionNamePrefix;
							String nodeName = null;
							try {nodeName = addPNNode(cell, n, prefix);
							} catch (ParameterException e) {e.printStackTrace();}
							
							addLabelToNode(cell, n, nodeName);
							addGraphicalInfoToPN(cell, n, nodeName);

						
						}
					}
				}
				refresh();
			}
		}
	}



	/**
	 * @param cell
	 * @param n
	 * @param nodeName
	 */
	public void addGraphicalInfoToPN(mxCell cell,
			AbstractGraphicalPN<?, ?, ?, ?, ?> n, String nodeName) {
		
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
		
		
		if(cell.getStyle().contentEquals(Constants.PNPlaceShape)){
			if(n.getPetriNetGraphics().getPlaceGraphics() == null){
				n.getPetriNetGraphics().setPlaceGraphics(
						 new HashMap<String, NodeGraphics>());
			}
			n.getPetriNetGraphics().getPlaceGraphics().put(nodeName, nodeGraphics);
			
		}
		if(cell.getStyle().contentEquals(Constants.PNTransitionShape)){
			if(n.getPetriNetGraphics().getTransitionGraphics() == null){
				n.getPetriNetGraphics().setTransitionGraphics(
						 new HashMap<String, NodeGraphics>());
			}
			n.getPetriNetGraphics().getTransitionGraphics().put(nodeName, nodeGraphics);
			
		}
		
	}

	// }

	@Override
	public void cellsRemoved(Object[] cells) {
		super.cellsRemoved(cells);
		for (Object object : cells) {
			mxCell cell = (mxCell) object;
			if (cell.getStyle().contentEquals(Constants.PNPlaceShape)) {	
						try {
							n.getPetriNet().removePlace(cell.getId());
							n.getPetriNetGraphics().getPlaceGraphics().remove(cell.getId());
						} catch (ParameterException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
				}
				if (cell.getStyle().contentEquals(Constants.PNTransitionShape)) {
						try {
							System.out.println(cell.getId());
							n.getPetriNet().removeTransition(cell.getId());
							n.getPetriNetGraphics().getTransitionGraphics().remove(cell.getId());
						} catch (ParameterException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
				if (cell.getId().startsWith("arc")) {	
						try {
							n.getPetriNet().removeFlowRelation(cell.getId());
//							n.getPetriNetGraphics().getArcGraphics().remove(cell.getId());
						} catch (ParameterException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			}
		}
	

//			cell =(mxCell) graph.getSelectionCell();
//			Object value = cell.getValue();
//			if(cell.getParent().getValue() != null){
//				AbstractGraphicalPN<?, ?, ?, ?, ?> n = (AbstractGraphicalPN<?, ?, ?, ?, ?>) cell.getParent().getValue();
//				String cellID = (String) cell.getId();
//				if(cellID.startsWith("p"))
//			try {
//				n.getPetriNet().removePlace(cellID);
//			} catch (ParameterException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//				
//				if(cellID.startsWith("t"))
//			try {
//				n.getPetriNet().removeTransition(cellID);
//			} catch (ParameterException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//				
//				if(cellID.startsWith("arc")){
//					
//					try {
//						n.getPetriNet().removeFlowRelation(cellID);
//					} catch (ParameterException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					
//				}
//			}
			
		
//		dataHolder.updateData();
//	}

	@Override
	public void cellsResized(Object[] cells, mxRectangle[] bounds) {
		super.cellsResized(cells, bounds);

		// handling label position
		for (Object object : cells) {
			mxCell cell = (mxCell) object;
			// TODO: Kein labelposition auffindbar
			mxCell child = (mxCell) cell.getChildAt(0);
			mxGeometry geometry = new mxGeometry(0, 0, cell.getGeometry()
					.getWidth(), 10);
			geometry.setRelative(true); // important!
			geometry.setOffset(new mxPoint(0,
					cell.getGeometry().getHeight() + 5));
			child.setGeometry(geometry);

			if (cell.getChildCount() > 1) {
				child = (mxCell) cell.getChildAt(1);
				geometry = new mxGeometry(0, 0, cell.getGeometry().getWidth(),
						10);
				geometry.setRelative(true); // important!
				geometry.setOffset(new mxPoint(0, -10));
				child.setGeometry(geometry);
			}
		}
	}
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
						if (cell.getParent() != null
								&& cell.getParent().getValue() instanceof AbstractGraphicalPN<?, ?, ?, ?, ?>) {
							AbstractGraphicalPN<?, ?, ?, ?, ?> n = (AbstractGraphicalPN<?, ?, ?, ?, ?>) cell
									.getParent().getValue();
							NodeGraphics pG = null;
							if (cell.getStyle().contentEquals(
									Constants.PNPlaceShape)) {
								pG = n.getPetriNetGraphics()
										.getPlaceGraphics().get(cell.getId());
								
								updatePositionInPN(cell, pG);}
							
						
						if (cell.getStyle().contentEquals(
								Constants.PNTransitionShape)) {
							 pG = n.getPetriNetGraphics()
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
//					AbstractGraphicalPN<?, ?, ?, ?, ?> n = (AbstractGraphicalPN<?, ?, ?, ?, ?>) ((mxCell) cell)
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
			AbstractGraphicalPN<?, ?, ?, ?, ?> n) {
		try {
			AbstractFlowRelation<?, ?, ?> fr = n.getPetriNet()
					.addFlowRelationTP(edgeSource.getId(),
							edgeTarget.getId());
			((mxCell) edge).setId(fr.getName());
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addPTArcToPN(Object edge, mxCell edgeSource, mxCell edgeTarget,
			AbstractGraphicalPN<?, ?, ?, ?, ?> n) {
		try {
			AbstractFlowRelation<?, ?, ?> fr = n.getPetriNet()
					.addFlowRelationPT(edgeSource.getId(),
							edgeTarget.getId());
			((mxCell) edge).setId(fr.getName());
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addLabelToNode(mxCell edgeTarget,
			AbstractGraphicalPN<?, ?, ?, ?, ?> n, String nodeName) {
		mxGeometry geom = new mxGeometry(0, 0, edgeTarget.getGeometry()
				.getWidth(), 10);

		geom.setOffset(new mxPoint(0, edgeTarget.getGeometry().getHeight() + 5));
		geom.setRelative(true);
		mxCell label = null;

		if (edgeTarget.getStyle().contentEquals(Constants.PNTransitionShape))
			label = new mxCell(n.getPetriNet().getTransition(nodeName)
					.getLabel(), geom, Constants.PNLabelStyle);
		if (edgeTarget.getStyle().contentEquals(Constants.PNPlaceShape))
			label = new mxCell(n.getPetriNet().getPlace(nodeName).getLabel(),
					geom, Constants.PNLabelStyle);

		label.setVertex(true);
		label.setConnectable(false);
		
		edgeTarget.insert(label);
	}

	public String addPNNode(mxCell edgeTarget, AbstractGraphicalPN<?, ?, ?, ?, ?> n, String prefix)
			throws ParameterException {
		SortedMap<Integer, String> sortedMap = new TreeMap<Integer, String>();
		Collection<?> nodeCollection = null;
		if (edgeTarget.getStyle().contentEquals(Constants.PNTransitionShape)) {
			nodeCollection = n.getPetriNet().getTransitions();
			for (Object o : nodeCollection) {
				if (o instanceof PTTransition) {
					PTTransition transition = (PTTransition) o;
					addNodeToMap(sortedMap, transition.getName(), prefix);
				}
			}
			n.getPetriNet().addTransition(prefix + getLowestIndex(sortedMap));
		}

		if (edgeTarget.getStyle().contentEquals(Constants.PNPlaceShape)) {
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



	public void setPN(AbstractGraphicalPN<?, ?, ?, ?, ?> netContainer) {
		this.n = netContainer;
		
	}

}
