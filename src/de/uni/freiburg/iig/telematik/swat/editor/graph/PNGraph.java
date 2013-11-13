package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Position;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPNNode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNPropertiesListener;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperty;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNPropertyChangeEvent;
import de.unifreiburg.iig.bpworkbench2.editor.graph.GraphView;

public abstract class PNGraph extends mxGraph implements PNPropertiesListener{

	private AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> netContainer = null;
	private PNProperties properties = null;
	@SuppressWarnings("rawtypes")
	protected Map<AbstractPNNode, PNGraphCell> nodeReferences = new HashMap<AbstractPNNode, PNGraphCell>();

	public PNGraph(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> netContainer, PNProperties properties) throws ParameterException {
		super();
		Validate.notNull(netContainer);
		Validate.notNull(properties);
		
		this.netContainer = netContainer;
		this.properties = properties;
		this.properties.addPNPropertiesListener(this);

		setAlternateEdgeStyle("edgeStyle=mxEdgeStyle.ElbowConnector;elbow=vertical");
		setMultigraph(true);
		setCellsEditable(false);
		setDisconnectOnMove(false);
		setExtendParents(false); // disables extending parents after adding
		setVertexLabelsMovable(true);
		
		initialize();
	}
	
	@SuppressWarnings("rawtypes")
	private void initialize(){
		// Check if net container is empty.
		// If not, add all PN components to the graph.
		if(!netContainer.getPetriNet().isEmpty()){
			getModel().beginUpdate();
			
			for(AbstractPlace place: getNetContainer().getPetriNet().getPlaces()){
				insertPNPlace(place, netContainer.getPetriNetGraphics().getPlaceGraphics().get(place.getName()));
			}
			for(AbstractTransition transition: getNetContainer().getPetriNet().getTransitions()){
				insertPNTransition(transition, netContainer.getPetriNetGraphics().getTransitionGraphics().get(transition.getName()));
			}
			for(AbstractFlowRelation relation: getNetContainer().getPetriNet().getFlowRelations()){
				insertPNRelation(relation, getArcConstraint(relation), netContainer.getPetriNetGraphics().getArcGraphics().get(relation.getName()));
			}
			getModel().endUpdate();
			
			setLabelPositions();
		}
	}
	
	@SuppressWarnings("rawtypes")
	protected abstract String getArcConstraint(AbstractFlowRelation relation);
	
	@SuppressWarnings("rawtypes")
	private void addNodeReference(AbstractPNNode pnNode, PNGraphCell cell){
		nodeReferences.put(pnNode, cell);
	}
	
	@SuppressWarnings("rawtypes")
	private PNGraphCell getCell(AbstractPNNode pnNode){
		return nodeReferences.get(pnNode);
	}
	
	/**
	 * Constructs a new customized view to be used in this graph, which also
	 * writes label annotations in the PN-Model.
	 */
	@Override
	protected GraphView createGraphView() {
		view = new GraphView(this);
		return (GraphView) view;
	}
	
	public AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> getNetContainer(){
		return netContainer;
	}
	
	protected PNProperties getPNProperties(){
		return properties;
	}

	
	@SuppressWarnings("rawtypes")
	public PNGraphCell insertPNPlace(AbstractPlace place, NodeGraphics nodeGraphics){
		PNGraphCell newCell = createPlaceCell(place.getName(), place.getName(), nodeGraphics.getPosition().getX(), nodeGraphics.getPosition().getY(), nodeGraphics.getDimension().getX(), nodeGraphics.getDimension().getY(), MXConstants.getStyle(PNComponent.PLACE, nodeGraphics));
		addCell(newCell, getDefaultParent());
		addNodeReference(place, newCell);
		return newCell;
	}
	
	/**
	 * 
	 * @param name The place name.
	 * @param label The place label.
	 * @param posX The x coordinate of the vertex.
	 * @param posY The y coordinate of the vertex.
	 * @param width The width of the vertex.
	 * @param height The width of the vertex.
	 * @param style The place style.
	 * @return
	 */
	private PNGraphCell createPlaceCell(String name, String label, double posX, double posY, double width, double height, String style) {
		mxGeometry geometry = new mxGeometry(posX, posY, width, height);
		geometry.setRelative(false);
		PNGraphCell vertex = new PNGraphCell(label, geometry, style, PNComponent.PLACE);
		vertex.setId(name);
		vertex.setVertex(true);
		vertex.setConnectable(true);
		return vertex;
	}
	
	@SuppressWarnings("rawtypes")
	public PNGraphCell insertPNTransition(AbstractTransition transition, NodeGraphics nodeGraphics){
		PNGraphCell newCell = createTransitionCell(transition.getName(), transition.getLabel(), nodeGraphics.getPosition().getX(), nodeGraphics.getPosition().getY(), nodeGraphics.getDimension().getX(), nodeGraphics.getDimension().getY(), MXConstants.getStyle(PNComponent.TRANSITION, nodeGraphics));
		addCell(newCell, getDefaultParent());
		addNodeReference(transition, newCell);
		return newCell;
	}
	
	/**
	 * 
	 * @param name The transition name.
	 * @param label The transition label.
	 * @param posX The x coordinate of the vertex.
	 * @param posY The y coordinate of the vertex.
	 * @param width The width of the vertex.
	 * @param height The width of the vertex.
	 * @param style The transition style.
	 * @return
	 */
	private PNGraphCell createTransitionCell(String name, String label, double posX, double posY, double width, double height, String style) {
		mxGeometry geometry = new mxGeometry(posX, posY, width, height);
		geometry.setRelative(false);
		PNGraphCell vertex = new PNGraphCell(label, geometry, style, PNComponent.TRANSITION);
		vertex.setId(name);
		vertex.setVertex(true);
		vertex.setConnectable(true);
		return vertex;
	}
	
	@SuppressWarnings("rawtypes")
	public Object insertPNRelation(AbstractFlowRelation relation, String value, ArcGraphics arcGraphics){
		Vector<Position> positions = arcGraphics == null ? new Vector<Position>() : arcGraphics.getPositions();
		PNGraphCell newCell = createArcCell(relation.getName(), getArcConstraint(relation), positions, MXConstants.getStyle(arcGraphics));
		addEdge(newCell, getDefaultParent(), getCell(relation.getSource()), getCell(relation.getTarget()), null);
		return newCell;
	}
	
	private PNGraphCell createArcCell(String name, String label, Collection<Position> positions, String style) {
		mxGeometry geometry = new mxGeometry();
		List<mxPoint> points = new ArrayList<mxPoint>();
		for(Position position: positions){
			points.add(new mxPoint(position.getX(), position.getY()));
		}
		geometry.setPoints(points);
		geometry.setRelative(true);
		PNGraphCell vertex = new PNGraphCell(label, geometry, style, PNComponent.ARC);
		vertex.setId(name);
		vertex.setVertex(false);
		vertex.setEdge(true);
		vertex.setConnectable(true);
		return vertex;
	}
	
	
	public abstract void updatePlaceState(PNGraphCell cell, Object state) throws ParameterException;
	
	/**
	 * Sets the positions of place and transition labels according to the<br>
	 * information contained in the corresponding annotation graphics.<br>
	 * This method is called when a graph is created with a non-empty Petri net.
	 * 
	 * @param pnGraphics The Petri net graphics
	 */
	public void setLabelPositions(){
		for(PNGraphCell cell: nodeReferences.values()){
			mxCellState state = getView().getState(cell);
			
//			String horizontal = mxUtils.getString(state.getStyle(), mxConstants.STYLE_LABEL_POSITION, mxConstants.ALIGN_CENTER);
//			if (horizontal.equals(mxConstants.ALIGN_LEFT)) {
//				state.getAbsoluteOffset().setX(state.getAbsoluteOffset().getX() - state.getWidth());
//			} else if (horizontal.equals(mxConstants.ALIGN_RIGHT)) {
//				state.getAbsoluteOffset().setX(state.getAbsoluteOffset().getX() + state.getWidth());
//			}
//			
//			String vertical = mxUtils.getString(state.getStyle(), mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_MIDDLE);
//			if (vertical.equals(mxConstants.ALIGN_TOP)) {
//				state.getAbsoluteOffset().setY(state.getAbsoluteOffset().getY() - state.getHeight());
//			} else if (vertical.equals(mxConstants.ALIGN_BOTTOM)) {
//				state.getAbsoluteOffset().setY(state.getAbsoluteOffset().getY() + state.getHeight());
//			}
			
			AnnotationGraphics annotationGraphics = null;
			if (cell.getType() == PNComponent.PLACE) {
				annotationGraphics = netContainer.getPetriNetGraphics().getPlaceLabelAnnotationGraphics().get(cell.getId());
			} else if (cell.getType() == PNComponent.TRANSITION) {
				annotationGraphics = netContainer.getPetriNetGraphics().getTransitionLabelAnnotationGraphics().get(cell.getId());
			} else if(cell.getType() == PNComponent.ARC){
				annotationGraphics = netContainer.getPetriNetGraphics().getArcAnnotationGraphics().get(cell.getId());
			}
			if(annotationGraphics != null){
				state.setAbsoluteOffset(new mxPoint(annotationGraphics.getOffset().getX(), annotationGraphics.getOffset().getY()));
			}
			if(cell.getId().equals("p2")){
				System.out.println(state.getAbsoluteOffset());
			}
		}
		System.out.println("refresh start");
		repaint();
		System.out.println("refresh end");
		System.out.println(getView().getState(nodeReferences.get(netContainer.getPetriNet().getPlace("p2"))).getAbsoluteOffset());
	}
	
	/**
	 * Sets the annotation graphics of a Petri net place/transition<br>
	 * according to the properties of the corresponding graph cell.
	 * @param state The cell state
	 * @throws ParameterException 
	 */
	public void setGraphics(mxCellState state) throws ParameterException {
		PNGraphCell cell = (PNGraphCell) state.getCell();

		if(cell.getType() == PNComponent.PLACE){
			netContainer.getPetriNetGraphics().getPlaceGraphics().put(cell.getId(), MXConstants.getNodeGraphics(cell));
			//TODO: Set annotation graphics
		} else if(cell.getType() == PNComponent.TRANSITION){
			netContainer.getPetriNetGraphics().getTransitionGraphics().put(cell.getId(), MXConstants.getNodeGraphics(cell));
			//TODO: Set annotation graphics
		} else if(cell.getType() == PNComponent.ARC){
			netContainer.getPetriNetGraphics().getArcGraphics().put(cell.getId(), MXConstants.getArcGraphics(cell));
			//TODO: Set annotation graphics
		}
	}
	
	//------- GUI events that represent changes on Petri net components ----------------------------------------
	
	/**
	 * This method notifies the graph, that some cells have been added.<br>
	 * Note: Only by copy/pase actions on graph canvas!<br>
	 * In case these cells stand for new places or transitions, they have to be added to the Petri net.
	 */
	@Override
	public void cellsAdded(Object[] cells, Object parent, Integer index, Object source, Object target, boolean absolute) {

	}
	
	protected void addTransitionToPetriNet(String name, String label){
		
	}
	
	//------- Property change handling -------------------------------------------------------------------------
	//------- These methods are called when some Petri net properties changed by other classes. ----------------
	
	/**
	 * This method notifies a PNPropertiesListener that a PN component was added.<br>
	 * This can be a place, transition or arc.<br>
	 * In the 
	 */
	@Override
	public void componentAdded(PNComponent component, String name) {}

	@Override
	public void componentRemoved(PNComponent component, String name) {}

	@Override
	public void propertyChange(PNPropertyChangeEvent event) {
		if(event.getSource() != this){
			switch(event.getFieldType()){
			case PLACE:
				handlePlacePropertyChange(event.getName(), event.getProperty(), event.getOldValue(), event.getNewValue());
				break;
			case TRANSITION:
				handleTransitionPropertyChange(event.getName(), event.getProperty(), event.getOldValue(), event.getNewValue());
				break;
			case ARC:
				handleArcPropertyChange(event.getName(), event.getProperty(), event.getOldValue(), event.getNewValue());
				break;
			}
			refresh();
		}
	}

	private boolean handlePlacePropertyChange(String name, PNProperty property, Object oldValue, Object newValue) {
		switch(property){
		case PLACE_LABEL:
			//TODO:
			return true;
		case PLACE_SIZE:
			//TODO:
			return true;
		}
		return false;
	}

	protected boolean handleTransitionPropertyChange(String name, PNProperty property, Object oldValue, Object newValue) {
		switch(property){
		case TRANSITION_LABEL:
			//TODO:
			return true;
		case TRANSITION_SIZE:
			//TODO:
			return true;
		}
		return false;
	}
	
	protected boolean handleArcPropertyChange(String name, PNProperty property, Object oldValue, Object newValue) {
		return false;
	}

}
