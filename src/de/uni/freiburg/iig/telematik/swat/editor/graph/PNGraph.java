package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxImageCanvas;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.shape.mxIShape;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

import de.invation.code.toval.graphic.CircularPointGroup;
import de.invation.code.toval.graphic.GraphicUtils;
import de.invation.code.toval.graphic.PColor;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.TokenGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Offset;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Position;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPNNode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
//import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphComponent.GCMouseAdapter;
import de.uni.freiburg.iig.telematik.swat.editor.menu.EditorProperties;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNPropertiesListener;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperty;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNPropertyChangeEvent;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreeNode;

public abstract class PNGraph extends mxGraph implements PNPropertiesListener, TreeSelectionListener {

	private AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> netContainer = null;
	private PNProperties properties = null;
	@SuppressWarnings("rawtypes")
	protected Map<AbstractPNNode, PNGraphCell> nodeReferences = new HashMap<AbstractPNNode, PNGraphCell>();
	private Map<AbstractFlowRelation, PNGraphCell> arcReferences = new HashMap<AbstractFlowRelation, PNGraphCell>();

	public PNGraph(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> netContainer, PNProperties properties) throws ParameterException {
		super();
		Validate.notNull(netContainer);
		Validate.notNull(properties);

		this.netContainer = netContainer;
		this.properties = properties;
		this.properties.addPNPropertiesListener(this);
		this.properties.getPropertiesView().addTreeSelectionListener(this);
		this.getSelectionModel().addListener(mxEvent.CHANGE, this.properties.getPropertiesView());
		this.addListener(mxEvent.CELLS_ADDED, this.properties.getPropertiesView());
		this.addListener(mxEvent.CELLS_REMOVED, this.properties.getPropertiesView());
		this.addListener(mxEvent.REPAINT, this.properties.getPropertiesView());
		setAlternateEdgeStyle("edgeStyle=mxEdgeStyle.ElbowConnector;elbow=vertical");
		setMultigraph(true);
		setCellsEditable(false);
		setDisconnectOnMove(false);
		setExtendParents(false); // disables extending parents after adding
		setVertexLabelsMovable(true);
		initialize();

	}
	

	@SuppressWarnings("rawtypes")
	private void initialize() {
		// Check if net container is empty.
		// If not, add all PN components to the graph.
		if (!netContainer.getPetriNet().isEmpty()) {
			getModel().beginUpdate();

			for (AbstractPlace place : getNetContainer().getPetriNet().getPlaces()) {
				insertPNPlace(place, netContainer.getPetriNetGraphics().getPlaceGraphics().get(place.getName()),
						netContainer.getPetriNetGraphics().getPlaceLabelAnnotationGraphics().get(place.getName()));
			}
			for (AbstractTransition transition : getNetContainer().getPetriNet().getTransitions()) {
				insertPNTransition(transition, netContainer.getPetriNetGraphics().getTransitionGraphics().get(transition.getName()), netContainer.getPetriNetGraphics()
						.getTransitionLabelAnnotationGraphics().get(transition.getName()));
			}
			for (AbstractFlowRelation relation : getNetContainer().getPetriNet().getFlowRelations()) {
				insertPNRelation(relation, getArcConstraint(relation), netContainer.getPetriNetGraphics().getArcGraphics().get(relation.getName()), netContainer.getPetriNetGraphics()
						.getArcAnnotationGraphics().get(relation.getName()));
			}
			getModel().endUpdate();

			updateGraphicalInfosFromCells();

		}
	}

	@SuppressWarnings("rawtypes")
	protected abstract String getArcConstraint(AbstractFlowRelation relation);

	@SuppressWarnings("rawtypes")
	public
	void addNodeReference(AbstractPNNode pnNode, PNGraphCell cell) {
		nodeReferences.put(pnNode, cell);
	}

	@SuppressWarnings("rawtypes")
	void addArcReference(AbstractFlowRelation pnArc, PNGraphCell cell) {
		arcReferences.put(pnArc, cell);
	}

	@SuppressWarnings("rawtypes")
	PNGraphCell getCell(AbstractPNNode pnNode) {
		return nodeReferences.get(pnNode);
	}

	public AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> getNetContainer() {
		return netContainer;
	}

	protected PNProperties getPNProperties() {
		return properties;
	}

	@SuppressWarnings("rawtypes")
	public PNGraphCell insertPNPlace(AbstractPlace place, NodeGraphics nodeGraphics, AnnotationGraphics annotationGraphics) {
		PNGraphCell newCell = createPlaceCell(place.getName(), place.getLabel(), nodeGraphics.getPosition().getX(), nodeGraphics.getPosition().getY(), nodeGraphics.getDimension().getX(), nodeGraphics
				.getDimension().getY(), MXConstants.getStyle(PNComponent.PLACE, nodeGraphics, annotationGraphics));
		addCell(newCell, getDefaultParent());
		addNodeReference(place, newCell);
		return newCell;
	}

	/**
	 * 
	 * @param name
	 *            The place name.
	 * @param label
	 *            The place label.
	 * @param posX
	 *            The x coordinate of the vertex.
	 * @param posY
	 *            The y coordinate of the vertex.
	 * @param width
	 *            The width of the vertex.
	 * @param height
	 *            The width of the vertex.
	 * @param style
	 *            The place style.
	 * @param object
	 * @param map
	 * @return
	 */
	public PNGraphCell createPlaceCell(String name, String label, double posX, double posY, double width, double height, String style) {
		mxGeometry geometry = new mxGeometry(posX, posY, width, height);
		geometry.setRelative(false);
		PNGraphCell vertex = new PNGraphCell(label, geometry, style, PNComponent.PLACE);
		vertex.setId(name);
		vertex.setVertex(true);
		vertex.setConnectable(true);
		return vertex;
	}

	// protected void addNewPlace(Point point){
	// String prefix = MXConstants.PlaceNamePrefix;
	// Integer index = 0;
	// while(getNetContainer().getPetriNet().containsPlace(prefix+index)){
	// index++;
	// }
	// String nodeName = prefix+index;
	//
	// if(getNetContainer().getPetriNet().addPlace(nodeName)){
	// AbstractPlace newPlace =
	// addNodeReference(newPlace, newCell);
	//
	// }
	// }

	@SuppressWarnings("rawtypes")
	public PNGraphCell insertPNTransition(AbstractTransition transition, NodeGraphics nodeGraphics, AnnotationGraphics annotationGraphics) {
		PNGraphCell newCell = createTransitionCell(transition.getName(), transition.getLabel(), nodeGraphics.getPosition().getX(), nodeGraphics.getPosition().getY(), nodeGraphics.getDimension()
				.getX(), nodeGraphics.getDimension().getY(), MXConstants.getStyle(PNComponent.TRANSITION, nodeGraphics, annotationGraphics));
		addCell(newCell, getDefaultParent());
		addNodeReference(transition, newCell);
		return newCell;
	}

	/**
	 * 
	 * @param name
	 *            The transition name.
	 * @param label
	 *            The transition label.
	 * @param posX
	 *            The x coordinate of the vertex.
	 * @param posY
	 *            The y coordinate of the vertex.
	 * @param width
	 *            The width of the vertex.
	 * @param height
	 *            The width of the vertex.
	 * @param style
	 *            The transition style.
	 * @return
	 */
	public PNGraphCell createTransitionCell(String name, String label, double posX, double posY, double width, double height, String style) {
		mxGeometry geometry = new mxGeometry(posX, posY, width, height);
		geometry.setRelative(false);
		PNGraphCell vertex = new PNGraphCell(label, geometry, style, PNComponent.TRANSITION);
		vertex.setId(name);
		vertex.setVertex(true);
		vertex.setConnectable(true);
		return vertex;
	}

	@SuppressWarnings("rawtypes")
	public Object insertPNRelation(AbstractFlowRelation relation, String value, ArcGraphics arcGraphics, AnnotationGraphics annotationGraphics) {
		Vector<Position> positions = arcGraphics == null ? new Vector<Position>() : arcGraphics.getPositions();
		PNGraphCell newCell = createArcCell(relation.getName(), getArcConstraint(relation), positions, MXConstants.getStyle(arcGraphics, annotationGraphics));
		addEdge(newCell, getDefaultParent(), getCell(relation.getSource()), getCell(relation.getTarget()), null);
		addArcReference(relation, newCell);
		return newCell;
	}

	PNGraphCell createArcCell(String name, String label, Collection<Position> positions, String style) {
		mxGeometry geometry = new mxGeometry();
		List<mxPoint> points = new ArrayList<mxPoint>();
		for (Position position : positions) {
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

	public abstract void updatePlaceState(PNGraphCell cell, Object tokenInput) throws ParameterException;

	// Needs to boe overriden for Token-Painting
	@Override
	/**
	 * Draws the cell state with the given label onto the canvas. No
	 * children or descendants are painted here. This method invokes
	 * cellDrawn after the cell, but not its descendants have been
	 * painted.
	 * 
	 * @param canvas Canvas onto which the cell should be drawn.
	 * @param state State of the cell to be drawn.
	 * @param drawLabel Indicates if the label should be drawn.
	 */
	public void drawState(mxICanvas canvas, mxCellState state, boolean drawLabel) {

		Object cell = (state != null) ? state.getCell() : null;

		if (cell != null && cell != view.getCurrentRoot() && cell != model.getRoot() && (model.isVertex(cell) || model.isEdge(cell))) {

			PNGraphCell customcell;
			Object  obj;
if(canvas instanceof mxImageCanvas)
	obj = canvas.drawCell(state);
		else
		obj = drawCell((mxGraphics2DCanvas) canvas, state);

			Object lab = null;

			// Holds the current clipping region in case the label will
			// be clipped
			Shape clip = null;
			Rectangle newClip = state.getRectangle();

			// Indirection for image canvas that contains a graphics canvas
			mxICanvas clippedCanvas = (isLabelClipped(state.getCell())) ? canvas : null;

			if (clippedCanvas instanceof mxImageCanvas) {
				clippedCanvas = ((mxImageCanvas) clippedCanvas).getGraphicsCanvas();
				// TODO: Shift newClip to match the image offset
				// Point pt = ((mxImageCanvas) canvas).getTranslate();
				// newClip.translate(-pt.x, -pt.y);
			}

			if (clippedCanvas instanceof mxGraphics2DCanvas) {
				Graphics g = ((mxGraphics2DCanvas) clippedCanvas).getGraphics();
				clip = g.getClip();

				// Ensure that our new clip resides within our old clip
				if (clip instanceof Rectangle) {
					g.setClip(newClip.intersection((Rectangle) clip));
				}
				// Otherwise, default to original implementation
				else {
					g.setClip(newClip);
				}
			}

			if (drawLabel) {
				String label = state.getLabel();

				if (label != null && state.getLabelBounds() != null) {

					lab = canvas.drawLabel(label, state, isHtmlLabel(cell));

				}
			}

			// Restores the previous clipping region
			if (clippedCanvas instanceof mxGraphics2DCanvas) {
				((mxGraphics2DCanvas) clippedCanvas).getGraphics().setClip(clip);
			}

			// Invokes the cellDrawn callback with the object which was created
			// by the canvas to represent the cell graphically
			if (obj != null) {
				cellDrawn(canvas, state, obj, lab);
			}
		}
	}

	public Object drawCell(mxGraphics2DCanvas canvas, mxCellState state) {
		Map<String, Object> style = state.getStyle();
		mxIShape shape = canvas.getShape(style);
		Graphics2D g;
		if (canvas.getGraphics() != null && shape != null) {
			// Creates a temporary graphics instance for drawing this shape
			float opacity = mxUtils.getFloat(style, mxConstants.STYLE_OPACITY, 100);
			Graphics2D previousGraphics = canvas.getGraphics();
			g = ((mxGraphics2DCanvas) canvas).createTemporaryGraphics(style, opacity, state);

			// Paints the shape and restores the graphics object
			shape.paintShape(canvas, state);
			if (state.getCell() instanceof PNGraphCell) {
				PNGraphCell customcell = (PNGraphCell) state.getCell();
				if (customcell.getType() == PNComponent.PLACE) {
					try {
						drawAdditionalPlaceGrahpics(canvas, state);
					} catch (ParameterException e) {
						System.out.println("PlaceGraphics could not be drawn");
						e.printStackTrace();
					}
				}
			}

			g.dispose();
			g = previousGraphics;
		}

		return shape;
	}


	protected void drawAdditionalPlaceGrahpics(mxGraphics2DCanvas canvas, mxCellState state) throws ParameterException {
		Rectangle temp = state.getRectangle();
		PNGraphCell cell = (PNGraphCell) state.getCell();

		int minDistance = (int) (EditorProperties.getInstance().getDefaultTokenDistance() * getView().getScale());
		int pointDiameter = (int) (EditorProperties.getInstance().getDefaultTokenSize() * getView().getScale());
		CircularPointGroup circularPointGroup = new CircularPointGroup(minDistance, pointDiameter);
		Integer k = getPlaceStateForCell(cell, circularPointGroup);

		Point center = new Point(temp.x + temp.width / 2, temp.y + temp.height / 2);
		int requiredWidth = 0;
		if (k == 1)
			requiredWidth = circularPointGroup.getPointDiameter();
		if (k == 2 || k == 3)
			requiredWidth = (circularPointGroup.getPointDiameter() + minDistance) * 2;
		if (k == 4)
			requiredWidth = (circularPointGroup.getPointDiameter() + minDistance * 2) * 2;
		if (k == 2)
			requiredWidth = (circularPointGroup.getPointDiameter() + minDistance) * 2;
		if (k >= 5)
			requiredWidth = circularPointGroup.getRequiredDiameter();
		if (state.getWidth() >= requiredWidth)
			drawPoints(canvas, temp, circularPointGroup, center);
		else
			drawNumbers(k.toString(), canvas, temp, center);

	}


	/**
	 * @param cell
	 * @param circularPointGroup
	 * @return
	 */
	protected abstract Integer getPlaceStateForCell(PNGraphCell cell, CircularPointGroup circularPointGroup);

	private void drawNumbers(String numbers, mxGraphics2DCanvas canvas, Rectangle temp, Point center) {
		Graphics g = canvas.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(new Font("Serif", Font.PLAIN, (int) (10 * getView().getScale())));
		g2.setPaint(Color.black);
		drawString(g2, numbers + "\n", center.x - (int) (temp.width * 0.1), center.y - (int) (g.getFontMetrics().getHeight() * 0.8));
	}

private void drawString(Graphics g, String text, int x, int y) {
    for (String line : text.split("\n"))
        g.drawString(line, x, y+= g.getFontMetrics().getHeight());
}


	protected void drawPoints(mxGraphics2DCanvas canvas, Rectangle temp, CircularPointGroup circularPointGroup, Point center) throws ParameterException {
		Graphics g = canvas.getGraphics();
		Iterator<PColor> iter = circularPointGroup.getColors().iterator();
		PColor actColor;
		Set<TokenGraphics> tgSet = new HashSet<TokenGraphics>();

		while (iter.hasNext()) {
			actColor = iter.next();
			g.setColor(new Color(actColor.getRGB()));
			for (de.invation.code.toval.graphic.Position p : circularPointGroup.getCoordinatesFor(actColor)) {
				GraphicUtils.fillCircle(g, (int) (center.getX() + p.getX()),(int) (center.getY() + p.getY()),circularPointGroup.getPointDiameter());
			}
		}
	}
	/**
	 * Sets the positions of place and transition labels according to the<br>
	 * information contained in the corresponding annotation graphics.<br>
	 * This method is called when a graph is created with a non-empty Petri net.
	 * 
	 * @param pnGraphics
	 *            The Petri net graphics
	 */
	public void updateGraphicalInfosFromCells() {
		for (PNGraphCell cell : nodeReferences.values()) {
			mxCellState state = getView().getState(cell);

			try {
				setGraphics(state);
			} catch (ParameterException e) {
				System.out.println("Graphics could not be set");
				e.printStackTrace();
			}
		}


	}


	/**
	 * Sets the annotation graphics of a Petri net place/transition<br>
	 * according to the properties of the corresponding graph cell.
	 * 
	 * @param state
	 *            The cell state
	 * @throws ParameterException
	 */
	public void setGraphics(mxCellState state) throws ParameterException {
		PNGraphCell cell = (PNGraphCell) state.getCell();
		if (cell.getType() == PNComponent.PLACE) {
			netContainer.getPetriNetGraphics().getPlaceGraphics().put(cell.getId(), MXConstants.getNodeGraphics(state));
			netContainer.getPetriNetGraphics().getPlaceLabelAnnotationGraphics().put(cell.getId(), MXConstants.getAnnotationGraphics(state));
		} else if (cell.getType() == PNComponent.TRANSITION) {
			netContainer.getPetriNetGraphics().getTransitionGraphics().put(cell.getId(), MXConstants.getNodeGraphics(state));
			netContainer.getPetriNetGraphics().getTransitionLabelAnnotationGraphics().put(cell.getId(), MXConstants.getAnnotationGraphics(state));
		} else if (cell.getType() == PNComponent.ARC) {
			netContainer.getPetriNetGraphics().getArcGraphics().put(cell.getId(), MXConstants.getArcGraphics(state));
			netContainer.getPetriNetGraphics().getArcAnnotationGraphics().put(cell.getId(), MXConstants.getArcAnnotationGraphics(state));
		}

	}

	@Override
	/**
	 * Fires a repaint event. The optional region is the rectangle that needs
	 * to be repainted.
	 */
	public void repaint(mxRectangle region) {
		fireEvent(new mxEventObject(mxEvent.REPAINT, "region", region));
		Object[] cells = getSelectionCells();
		for (Object object : cells) {
			if (object instanceof PNGraphCell) {
				PNGraphCell cell = (PNGraphCell) object;
				mxCellState state = getView().getState(cell);
				if (state == null)
					state = getView().getState(cell, true);
				Offset offset = null;
				double x = 0;
				double y = 0;
				if (state.getStyle().get(mxConstants.STYLE_SPACING_LEFT) != null) {
					x = Double.parseDouble((String) getView().getState(cell).getStyle().get(mxConstants.STYLE_SPACING_LEFT));
					y = Double.parseDouble((String) getView().getState(cell).getStyle().get(mxConstants.STYLE_SPACING_TOP));
				}
				state.getAbsoluteOffset().setX(state.getAbsoluteOffset().getX() + x);
				state.getAbsoluteOffset().setY(state.getAbsoluteOffset().getY() + y);

				switch (cell.getType()) {
				case PLACE:
					if (getNetContainer().getPetriNet().containsPlace(cell.getId())) {
						offset = netContainer.getPetriNetGraphics().getPlaceLabelAnnotationGraphics().get(((PNGraphCell) cell).getId()).getOffset();
					}
					break;
				case TRANSITION:
					if (getNetContainer().getPetriNet().containsTransition(cell.getId())) {
						offset = netContainer.getPetriNetGraphics().getTransitionLabelAnnotationGraphics().get(((PNGraphCell) cell).getId()).getOffset();
					}
					break;
				case ARC:
					if (netContainer.getPetriNetGraphics().getArcAnnotationGraphics().get(((PNGraphCell) cell).getId()) != null)
						offset = netContainer.getPetriNetGraphics().getArcAnnotationGraphics().get(((PNGraphCell) cell).getId()).getOffset();
					break;
				}
				if (offset != null) {
					offset.setX(state.getAbsoluteOffset().getX());
					offset.setY(state.getAbsoluteOffset().getY());
				}
				try {
					setGraphics(state);
				} catch (ParameterException e) {
					System.out.println("Problem while setting state graphics!");
					e.printStackTrace();
				}
			}
		}
	}

//	protected void setOffset(mxCellState state, AnnotationGraphics annotationGraphics) {
//		mxGeometry geometry = getModel().getGeometry(state.getCell());
//
//		if (geometry != null) {
//			double scale = getView().getScale();
//			double dx = annotationGraphics.getOffset().getX();
//			double dy = annotationGraphics.getOffset().getY();
//
//			mxPoint offset = geometry.getOffset();
//
//			if (offset == null) {
//				offset = new mxPoint();
//			}
//
//			dx += offset.getX();
//			dy += offset.getY();
//
//			geometry = (mxGeometry) geometry.clone();
//			geometry.setOffset(new mxPoint(Math.round(dx), Math.round(dy)));
//			getModel().setGeometry(state.getCell(), geometry);
//		}
//	}

	// ------- GUI events that represent changes on Petri net components
	// ----------------------------------------



	protected void addTransitionToPetriNet(String name, String label) {

	}

	// ------- Property change handling
	// -------------------------------------------------------------------------
	// ------- These methods are called when some Petri net properties changed
	// by other classes. ----------------

	/**
	 * This method notifies a PNPropertiesListener that a PN component was
	 * added.<br>
	 * This can be a place, transition or arc.<br>
	 * In the
	 */
	@Override
	public void componentAdded(PNComponent component, String name) {
	}

	@Override
	public void componentRemoved(PNComponent component, String name) {
	}

	@Override
	public void propertyChange(PNPropertyChangeEvent event) {
		if (event.getSource() != this) {
			switch (event.getFieldType()) {
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
		PNGraphCell placeCell = null;
		for (Entry<AbstractPNNode, PNGraphCell> nr : nodeReferences.entrySet()) {
			if (nr.getKey().getName() == name) {
				placeCell = nr.getValue();
				break;
			}
		}

		mxRectangle bounds;
		double dy;
		switch (property) {
		case PLACE_LABEL:
			placeCell.setValue(newValue);
			return true;
		case PLACE_SIZE:
			bounds = getView().getState(placeCell).getBoundingBox();
			bounds.setWidth(new Integer((Integer) newValue).doubleValue());
			bounds.setHeight(new Integer((Integer) newValue).doubleValue());
			resizeCell(placeCell, bounds);
			return true;
		case PLACE_POSITION_X:
			moveCells(new Object[] { placeCell }, new Integer((Integer) newValue).doubleValue() - new Integer((Integer) oldValue).doubleValue(), 0);
			return true;
		case PLACE_POSITION_Y:
			moveCells(new Object[] { placeCell }, 0, new Integer((Integer) newValue).doubleValue() - new Integer((Integer) oldValue).doubleValue());
			return true;

		}
		return false;
	}

	protected boolean handleTransitionPropertyChange(String name, PNProperty property, Object oldValue, Object newValue) {
		PNGraphCell transitionCell = null;
		for (Entry<AbstractPNNode, PNGraphCell> nr : nodeReferences.entrySet()) {
			if (nr.getKey().getName() == name) {
				transitionCell = nr.getValue();
				break;
			}
		}
		switch (property) {
		case TRANSITION_LABEL:
			transitionCell.setValue(newValue);
			return true;
		case TRANSITION_POSITION_X:
			transitionCell.getGeometry().setX(new Integer((Integer) newValue).doubleValue());
			return true;
		case TRANSITION_POSITION_Y:
			transitionCell.getGeometry().setY(new Integer((Integer) newValue).doubleValue());
			return true;
		case TRANSITION_SIZE_X:
			transitionCell.getGeometry().setWidth(new Integer((Integer) newValue).doubleValue());
			return true;
		case TRANSITION_SIZE_Y:
			transitionCell.getGeometry().setHeight(new Integer((Integer) newValue).doubleValue());
			return true;
		}
		return false;
	}

	protected boolean handleArcPropertyChange(String name, PNProperty property, Object oldValue, Object newValue) {
		PNGraphCell transitionCell = null;
		for (Entry<AbstractFlowRelation, PNGraphCell> nr : arcReferences.entrySet()) {
			if (nr.getKey().getName() == name) {
				transitionCell = nr.getValue();
				break;
			}
		}
		switch (property) {
		case ARC_WEIGHT:
			transitionCell.setValue(newValue);
			break;

		}

		return false;
	}
	
	//TREESELECTIONLISTENER - This Method is called when nodes of the PropertiesView Tree are selected

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		getSelectionModel().setEventSource(e.getSource());
		PNTreeNode node = (PNTreeNode) ((JTree) e.getSource()).getLastSelectedPathComponent();

		PNGraphCell currentSelectionCell;
		boolean executeSelection = true;
		if (getSelectionCell() instanceof PNGraphCell) {
			currentSelectionCell = (PNGraphCell) getSelectionCell();
			if (node != null && currentSelectionCell.getId() == node.toString())
				executeSelection = false;
		}

		selectCellbyTreeSelection(node, executeSelection);
		// setSelectionCell(nodeReferences);

		// reset Event source to default
		getSelectionModel().setEventSource(getSelectionModel());

	}

	/**
	 * @param node
	 * @param executeSelection
	 */
	public void selectCellbyTreeSelection(PNTreeNode node, boolean executeSelection) {
		if (node != null && executeSelection) {
			PNGraphCell cell = null;
			switch (node.getFieldType()) {
			case PLACE:
				cell = nodeReferences.get(getNetContainer().getPetriNet().getPlace(node.toString()));
				setSelectionCell(cell);
				break;
			case TRANSITION:
				cell = nodeReferences.get(getNetContainer().getPetriNet().getTransition(node.toString()));
				setSelectionCell(cell);
				break;
			case ARC:
				cell = arcReferences.get(getNetContainer().getPetriNet().getFlowRelation(node.toString()));
				setSelectionCell(cell);
				break;

			case LEAF:

				// Select Parent when working on Leafs Properties
				PNTreeNode parent = (PNTreeNode) node.getParent();
				switch (parent.getFieldType()) {
				case PLACE:
					selectCellbyTreeSelection(parent, executeSelection);
					break;
				case TRANSITION:
					selectCellbyTreeSelection(parent, executeSelection);
					break;
				case ARC:
					selectCellbyTreeSelection(parent, executeSelection);
					break;
				}
				break;
			case PLACES:
				// TODO Select all Places by clicking on Root of Places
				break;
			case TRANSITIONS:
				break;
			case ARCS:
				break;
			case ROOT:
				// Root is currently hidden
				break;

			}
		}
	}


	@Override
	public void cellsMoved(Object[] cells, double dx, double dy, boolean disconnect, boolean constrain) {
		super.cellsMoved(cells, dx, dy, disconnect, constrain);
		for (Object object : cells) {
			if (object instanceof PNGraphCell) {
				PNGraphCell cell = (PNGraphCell) object;
				try {
					switch (cell.getType()) {
					case ARC:
						break;
					case PLACE:
						if(getNetContainer().getPetriNet().containsPlace(cell.getId())){
						properties.setPlacePositionX(this, cell.getId(), (int) cell.getGeometry().getX());
						properties.setPlacePositionY(this, cell.getId(), (int) cell.getGeometry().getY());}
						break;
					case TRANSITION:
						properties.setTransitionPositionX(this, cell.getId(), (int) cell.getGeometry().getX());
						properties.setTransitionPositionY(this, cell.getId(), (int) cell.getGeometry().getY());
						break;
					}

				} catch (ParameterException e) {
					System.out.println("Cells could not be moved");
					e.printStackTrace();
				}
			}
		}
	}
	
	

	/**
	 * This method notifies the graph, that some cells have been added.<br>
	 * Note: Only by copy/pase actions on graph canvas!<br>
	 * In case these cells stand for new places or transitions, they have to be
	 * added to the Petri net.
	 */
	// @Override
	// public void cellsAdded(Object[] cells, Object parent, Integer index,
	// Object source, Object target, boolean absolute) {
	//
	// }
	//
	@Override
	public void cellsResized(Object[] cells, mxRectangle[] bounds) {
		// TODO Auto-generated method stub
		super.cellsResized(cells, bounds);
		for (Object object : cells) {
			if (object instanceof PNGraphCell) {
				PNGraphCell cell = (PNGraphCell) object;
				try {
					switch (cell.getType()) {
					case ARC:
						break;
					case PLACE:
						properties.setPlaceSize(this, cell.getId(), (int) cell.getGeometry().getWidth());
						break;
					case TRANSITION:
						properties.setTransitionSizeX(this, cell.getId(), (int) cell.getGeometry().getWidth());
						properties.setTransitionSizeY(this, cell.getId(), (int) cell.getGeometry().getHeight());
						break;

					}

				} catch (ParameterException e) {
					System.out.println("Placesize could not be changed");
					e.printStackTrace();
				}
			}
		}
	}


	@Override
	public void cellsRemoved(Object[] cells) {
		super.cellsRemoved(cells);
		
		for (Object object : cells) {
			if (object instanceof PNGraphCell) {
				PNGraphCell cell = (PNGraphCell) object;
				try {
					switch (cell.getType()) {
					case ARC:
						netContainer.getPetriNet().removeFlowRelation(cell.getId());
						break;
					case PLACE:
						netContainer.getPetriNet().removePlace(cell.getId());
						netContainer.getPetriNetGraphics().getPlaceGraphics().remove(cell.getId());
						netContainer.getPetriNetGraphics().getPlaceLabelAnnotationGraphics().remove(cell.getId());
						break;
					case TRANSITION:
						netContainer.getPetriNet().removeTransition(cell.getId());
						netContainer.getPetriNetGraphics().getTransitionGraphics().remove(cell.getId());
						netContainer.getPetriNetGraphics().getTransitionLabelAnnotationGraphics().remove(cell.getId());
						break;

					}

				} catch (ParameterException e) {
					System.out.println("Cells could not be removed from PN");
					e.printStackTrace();
				}
			}
			
		}
	}

}
