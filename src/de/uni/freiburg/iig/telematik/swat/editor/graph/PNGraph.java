package de.uni.freiburg.iig.telematik.swat.editor.graph;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.view.mxGraph;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNPropertiesListener;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperty;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNPropertyChangeEvent;
import de.unifreiburg.iig.bpworkbench2.editor.graph.GraphView;

public abstract class PNGraph extends mxGraph implements PNPropertiesListener{

	private AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> netContainer = null;
	private PNProperties properties = null;
	private String placeShape = null;
	private String transitionShape = null;

	public PNGraph(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> netContainer, PNProperties properties) throws ParameterException {
		super();
		Validate.notNull(netContainer);
		Validate.notNull(properties);
		
		this.netContainer = netContainer;
		this.properties = properties;
		this.properties.addPNPropertiesListener(this);
		
		setView(createGraphView());
		setAlternateEdgeStyle("edgeStyle=mxEdgeStyle.ElbowConnector;elbow=vertical");
		setMultigraph(true);
		setCellsEditable(false);
		setDisconnectOnMove(false);
		setExtendParents(false); // disables extending parents after adding
		setVertexLabelsMovable(true);
	}
	
	/**
	 * Constructs a new customized view to be used in this graph, which also
	 * writes label annotations in the PN-Model.
	 */
	@Override
	protected GraphView createGraphView() {
		view = new GraphView(this, netContainer);
		return (GraphView) view;
	}
	
	public AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> getNetContainer(){
		return netContainer;
	}
	
	protected PNProperties getPNProperties(){
		return properties;
	}
	
	public String getTransitionShape() {
		return transitionShape;
	}
	
	public void setPlaceShape(String placeShape) {
		this.placeShape = placeShape;
	}

	public void setTransitionShape(String transitionShape) {
		this.transitionShape = transitionShape;
	}

	public String getPlaceShape() {
		return placeShape;
	}
	
	public PNGraphCell insertPNPlace(String name, String label, NodeGraphics nodeGraphics){
		PNGraphCell newCell = createPlaceCell(name, label, nodeGraphics.getPosition().getX(), nodeGraphics.getPosition().getY(), nodeGraphics.getDimension().getX(), nodeGraphics.getDimension().getY(), getPlaceShape());
		addCell(newCell, getDefaultParent());
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
	
	public PNGraphCell insertPNTransition(String name, String label, NodeGraphics nodeGraphics){
		PNGraphCell newCell = createTransitionCell(name, label, nodeGraphics.getPosition().getX(), nodeGraphics.getPosition().getY(), nodeGraphics.getDimension().getX(), nodeGraphics.getDimension().getY(), getPlaceShape());
		addCell(newCell, getDefaultParent());
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
	
	public Object insertPNRelation(String name, String value, PNGraphCell source, PNGraphCell target, ArcGraphics arcGraphics){
		return insertEdge(getDefaultParent(), name, value, source, target);
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
