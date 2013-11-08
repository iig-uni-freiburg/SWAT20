package de.unifreiburg.iig.bpworkbench2.editor;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.swing.util.mxGraphActions.DeleteAction;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUndoableEdit.mxUndoableChange;
import com.mxgraph.view.mxGraphSelectionModel;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPNNode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.unifreiburg.iig.bpworkbench2.editor.actions.PrintAction;
import de.unifreiburg.iig.bpworkbench2.editor.actions.SaveAction;
import de.unifreiburg.iig.bpworkbench2.editor.actions.UndoRedoAction;
import de.unifreiburg.iig.bpworkbench2.editor.graph.Graph;
import de.unifreiburg.iig.bpworkbench2.editor.graph.GraphComponent;
import de.unifreiburg.iig.bpworkbench2.editor.graph.MXConstants;
import de.unifreiburg.iig.bpworkbench2.editor.properties.PNChangeEvent;
import de.unifreiburg.iig.bpworkbench2.editor.properties.PNProperties;
import de.unifreiburg.iig.bpworkbench2.editor.properties.PNPropertiesListener;
import de.unifreiburg.iig.bpworkbench2.editor.properties.PNProperty;
import de.unifreiburg.iig.bpworkbench2.editor.properties.PropertiesView;

public abstract class PNEditor extends JPanel implements PNPropertiesListener {

	private static final long serialVersionUID = 1023415244830760771L;
	private static final String scaleMessageFormat = "Scale: %s %%";
	
	protected JPanel statusPanel = null;
	protected PalettePanel palettePanel = null;
	protected mxGraphComponent graphComponent;
	protected ToolBar toolbar = null;
	protected mxRubberband rubberband;
	protected mxKeyboardHandler keyboardHandler;
	protected mxUndoManager undoManager;
	protected mxIEventListener undoHandler = new mxIEventListener() {
		public void invoke(Object source, mxEventObject evt) {
			// MatrixHolder.createMatrix();
			// ControlPanel.updateTables();
			undoManager.undoableEditHappened((mxUndoableEdit) evt.getProperty("edit"));
		}
	};
	protected mxIEventListener changeTracker = new mxIEventListener() {
		public void invoke(Object source, mxEventObject evt) {
			setModified(true);
		}
	};
	
	protected boolean modified = false;
	
	protected File fileReference = null;
	protected PNProperties properties = null;
	protected PropertiesView propertiesView = null;
	public AbstractGraphicalPN<?, ?, ?, ?, ?,?,?>  netContainer = null;
	@SuppressWarnings("rawtypes")
	Map<AbstractPNNode, Object> nodeReferences = new HashMap<AbstractPNNode, Object>();
	

	//------- Constructors --------------------------------------------------------------------


	public PNEditor(File fileReference) throws ParameterException{
		super();
		initialize(null, fileReference);
		setUpGUI();
	}
	
	public PNEditor(AbstractGraphicalPN<?, ?, ?, ?, ?,?,?> netContainer, File fileReference) throws ParameterException{
		super();
		Validate.notNull(netContainer);
		initialize(netContainer, fileReference);
		setUpGUI();
	}
	
	private void initialize(AbstractGraphicalPN<?, ?, ?, ?, ?,?,?> netContainer, File fileReference) throws ParameterException{
		if(netContainer == null){
			this.netContainer = createNetContainer();
		} else {
			this.netContainer = netContainer;
		}
		setFileReference(fileReference);
		properties = createPNProperties();
		properties.addPNPropertiesListener(this);
		propertiesView = new PropertiesView(properties);
		properties.addPNPropertiesListener(propertiesView);
	}
	
	protected abstract AbstractGraphicalPN<?, ?, ?, ?, ?,?,?> createNetContainer();
	
	protected abstract PNProperties createPNProperties();
	
	//------- Set Up GUI -----------------------------------------------------------------------
	
	private void setUpGUI() {
		setLayout(new BorderLayout());
		add(getToolbar(), BorderLayout.NORTH);
		add(getPalettePanel(), BorderLayout.LINE_START);
		add(getGraphComponent(), BorderLayout.CENTER);
//		add(getStatusPanel(), BorderLayout.SOUTH);
		
		rubberband = new mxRubberband(graphComponent);
		keyboardHandler = new KeyboardHandler(graphComponent);
	}
	
	private ToolBar getToolbar(){
		if(toolbar == null){
			try {
				toolbar = new ToolBar(this, JToolBar.HORIZONTAL);
			} catch (ParameterException e) {
				// Cannot happen, since this is not null.
				e.printStackTrace();
			}
		}
		return toolbar;
	}
	
	public mxGraphComponent getGraphComponent(){
		if(graphComponent == null){
			try {
				System.out.println(netContainer + "#PNEditor160");
				graphComponent = new GraphComponent(new Graph(netContainer));
			} catch (ParameterException e) {
				// Should not happen, since netContainer is not null
				e.printStackTrace();
			}
			visualizeGraph();
			graphComponent.getViewport().setOpaque(true);
			graphComponent.getViewport().setBackground(MXConstants.blueBG);

			Map<String, Object> style = getGraph().getStylesheet().getDefaultEdgeStyle();
			style.put("strokeWidth", 2.0);
			style.put("strokeColor", Integer.toHexString(MXConstants.bluelow.getRGB()));
			
			addGraphComponentListeners();
			setUpUndo();
		}
		return graphComponent;
	}
	
	private void addGraphComponentListeners() {
		graphComponent.addMouseWheelListener(new MouseWheelListener() {

			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getSource() instanceof mxGraphOutline || e.isControlDown()) {
					if (e.getWheelRotation() < 0) {
						graphComponent.zoomIn();
					} else {
						graphComponent.zoomOut();
					}
					displayStatusMessage(String.format(scaleMessageFormat, (int) (100 * getGraph().getView().getScale())));
				}
			}
		});

		graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), graphComponent);
					PopupMenu menu = null;
					try {
						menu = new PopupMenu(PNEditor.this);
					} catch (ParameterException e1) {
						// Cannot happen, since this is not null
						e1.printStackTrace();
					}
					menu.show(graphComponent, pt.x, pt.y);
					e.consume();
				}
			}
		});

		graphComponent.getGraphControl().addMouseMotionListener(
				new MouseMotionListener() {
					public void mouseDragged(MouseEvent e) {
						displayStatusMessage(e.getX() + ", " + e.getY());
					}
					public void mouseMoved(MouseEvent e) {
						mouseDragged(e);
					}
		});

		// Add SelectionListener for graph
//		getGraph().getSelectionModel().addListener(mxEvent.CHANGE, new mxIEventListener() {
//
//			@Override
//			public void invoke(Object sender, mxEventObject evt) {
//				mxCell cell = (mxCell) ((mxGraphSelectionModel) sender).getCell();
//
//			}
//		});
	}

	private void setUpUndo(){
		getGraph().getModel().addListener(mxEvent.CHANGE, changeTracker); // S.T.A.R.S.
		getGraph().getModel().addListener(mxEvent.UNDO, undoHandler);
		getGraph().getView().addListener(mxEvent.UNDO, undoHandler);

		mxIEventListener handler = new mxIEventListener() {
			public void invoke(Object source, mxEventObject evt) {
				List<mxUndoableChange> changes = ((mxUndoableEdit) evt.getProperty("edit")).getChanges();
				getGraph().setSelectionCells(getGraph().getSelectionCellsForChanges(changes));
			}
		};
		undoManager = new mxUndoManager();
		undoManager.addListener(mxEvent.UNDO, handler);
		undoManager.addListener(mxEvent.REDO, handler);
	}
	
	private JPanel getPalettePanel(){
		if(palettePanel == null){
			palettePanel = new PalettePanel();
			palettePanel.addTemplate("Transition", new ImageIcon(PNEditor.class.getResource("/images/rectangle.png")), MXConstants.PNTransitionShape, 30, 30, null);
			palettePanel.addTemplate("Place", new ImageIcon(PNEditor.class.getResource("/images/ellipse.png")), MXConstants.PNPlaceShape, 30, 30, null);
		}
		return palettePanel;
	}
	
	private JPanel getStatusPanel(){
		if(statusPanel == null) {
			statusPanel = new JPanel();
			JLabel statusBar = new JLabel();
			statusBar.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
			statusPanel.add(statusBar);
		}
		return statusPanel;
	}
	
	//------- Functionality --------------------------------------------------------------------
	
	public AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> getNetContainer(){
		return netContainer;
	}

	public PropertiesView getPropertiesView() {
		return propertiesView;
	}
	
	protected PNProperties getPNProperties(){
		return properties;
	}
	
	private Graph getGraph(){
		return (Graph) graphComponent.getGraph();
	}
	
	public void setPlaceLabel(String placeName, String placeLabel) throws ParameterException{
		Validate.notNull(placeName);
		Validate.notNull(placeLabel);
		Validate.notEmpty(placeName);
		Validate.notEmpty(placeLabel);
		if(!getNetContainer().getPetriNet().containsPlace(placeName))
			throw new ParameterException("Unknown place.");
		
		properties.setPlaceLabel(this, placeName, placeLabel);
	}
	
	//TODO: Do same thing for transition label, place size, transition size

	protected void visualizeGraph() {
		if(netContainer.getPetriNet().isEmpty())
			return;
		
		getGraph().getModel().beginUpdate();
		traverseFlowRelation();
		getGraph().getModel().endUpdate();
	}

	@SuppressWarnings("rawtypes")
	private void traverseFlowRelation() {
		for(AbstractPlace place: getNetContainer().getPetriNet().getPlaces()){
			Object createdNode = addVertex(place.getName(), place.getLabel(), netContainer.getPetriNetGraphics().getPlaceGraphics().get(place.getName()), getPlaceShape(place));
			nodeReferences.put(place, createdNode);
			getGraph().addLabelAndInfo(createdNode);
		}
		for(AbstractTransition transition: getNetContainer().getPetriNet().getTransitions()){
			Object createdNode = addVertex(transition.getName(), transition.getLabel(), netContainer.getPetriNetGraphics().getTransitionGraphics().get(transition.getName()), getTransitionShape(transition));
			nodeReferences.put(transition, createdNode);
			getGraph().addLabelAndInfo(createdNode);
		}
		for(AbstractFlowRelation relation: getNetContainer().getPetriNet().getFlowRelations()){
			getGraph().insertEdge(null, relation.getName(), getArcConstraint(relation), nodeReferences.get(relation.getSource()), nodeReferences.get(relation.getTarget()));
		}

	}
	
	@SuppressWarnings("rawtypes")
	protected String getPlaceShape(AbstractPlace place){
		return MXConstants.PNPlaceShape;
	}
	
	@SuppressWarnings("rawtypes")
	protected String getTransitionShape(AbstractTransition transition){
		 return MXConstants.PNTransitionShape;
	}
	
	@SuppressWarnings("rawtypes")
	protected abstract String getArcConstraint(AbstractFlowRelation relation);
	
	private Object addVertex(String name, String label, NodeGraphics nodeGraphics, String shape) {
		mxCell vertex = (mxCell) getGraph().insertPNVertex(getGraph().getDefaultParent(), label, null, nodeGraphics.getPosition()
						.getX(), nodeGraphics.getPosition().getY(), nodeGraphics.getDimension().getX(), nodeGraphics.getDimension()
						.getY(), shape, false);
		vertex.setId(name);
		return vertex;
	}

	public void setFileReference(File fileReference) throws ParameterException {
		Validate.notNull(fileReference);
		Validate.noDirectory(fileReference);
		this.fileReference = fileReference;
	}

	public File getFileReference() {
		return fileReference;
	}

	public void setModified(boolean modified) {
		boolean oldValue = this.modified;
		this.modified = modified;

		firePropertyChange("modified", oldValue, modified);
	}

	public boolean isModified() {
		return modified;
	}

	public mxUndoManager getUndoManager() {
		return undoManager;
	}

	public Action bind(String name, final Action action) {
		return bind(name, action, null);
	}

	public Action bind(String name, final Action action, String iconUrl) {
		return new AbstractAction(name, (iconUrl != null) ? new ImageIcon(PNEditor.class.getResource(iconUrl)) : null) {
			public void actionPerformed(ActionEvent e) {
				action.actionPerformed(new ActionEvent(getGraphComponent(), e.getID(), e.getActionCommand()));
			}
		};
	}

	public void displayStatusMessage(String msg) {
		// TODO: Do something
	}
	
	
	@Override
	public void propertyChange(PNChangeEvent event) {
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
			getGraph().refresh();
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

	protected class KeyboardHandler extends mxKeyboardHandler {

		public KeyboardHandler(mxGraphComponent graphComponent) {
			super(graphComponent);
		}

		@Override
		protected InputMap getInputMap(int condition) {
			InputMap map = super.getInputMap(condition);
			if (condition == JComponent.WHEN_FOCUSED && map != null) {
				map.put(KeyStroke.getKeyStroke("control S"), "save");
				map.put(KeyStroke.getKeyStroke("control shift S"), "saveAs");
				map.put(KeyStroke.getKeyStroke("control N"), "new");
				map.put(KeyStroke.getKeyStroke("control O"), "open");
				map.put(KeyStroke.getKeyStroke("control Z"), "undo");
				map.put(KeyStroke.getKeyStroke("control Y"), "redo");
				map.put(KeyStroke.getKeyStroke("control shift V"), "selectVertices");
				map.put(KeyStroke.getKeyStroke("control shift E"), "selectEdges");
				map.put(KeyStroke.getKeyStroke("control P"), "printNet");
				map.put(KeyStroke.getKeyStroke("DELETE"), "delete");
			}
			return map;
		}

		@Override
		protected ActionMap createActionMap() {
			ActionMap map = super.createActionMap();
			try {
				map.put("save", new SaveAction(PNEditor.this));
				map.put("undo", new UndoRedoAction(PNEditor.this, true));
				map.put("redo", new UndoRedoAction(PNEditor.this, false));
				map.put("printNet", new PrintAction(PNEditor.this));
			} catch (ParameterException e) {
				// Cannot happen, since this is not null
				e.printStackTrace();
			}
			
			map.put("selectVertices", mxGraphActions.getSelectVerticesAction());
			map.put("selectEdges", mxGraphActions.getSelectEdgesAction());
			map.put("delete", new DeleteAction("delete"));
			return map;
		}
	}
	
//	private void setLayoutOrganic(final Graph graph) {
//	mxOrganicLayout layout = new mxOrganicLayout(graph);
//	// Execute layout
//	// final mxGraph graph =graph;
//	Object cell = graph.getSelectionCell();
//
//	if (cell == null || graph.getModel().getChildCount(cell) == 0) {
//		cell = graph.getDefaultParent();
//	}
//
//	graph.getModel().beginUpdate();
//	try {
//		long t0 = System.currentTimeMillis();
//		layout.execute(cell);
//		// status("Layout: " + (System.currentTimeMillis() - t0)
//		// + " ms");
//	} finally {
//		mxMorphing morph = new mxMorphing(graphComponent, 20, 1.2, 20);
//
//		morph.addListener(mxEvent.DONE, new mxIEventListener() {
//
//			public void invoke(Object sender, mxEventObject evt) {
//				graph.getModel().endUpdate();
//			}
//
//		});
//
//		morph.startAnimation();
//	}
//}
//	public Action graphLayout(final String key, boolean animate) {
//		final mxIGraphLayout layout = createLayout(key, animate);
//
//		if (layout != null) {
//			return new AbstractAction(mxResources.get(key)) {
//
//				public void actionPerformed(ActionEvent e) {
//					final Graph graph = (Graph) graphComponent.getGraph();
//					Object cell = graph.getSelectionCell();
//
//					if (cell == null
//							|| graph.getModel().getChildCount(cell) == 0) {
//						cell = graph.getDefaultParent();
//					}
//
//					graph.getModel().beginUpdate();
//					try {
//						long t0 = System.currentTimeMillis();
//						layout.execute(cell);
//						displayStatusMessage("Layout: " + (System.currentTimeMillis() - t0)
//								+ " ms");
//					} finally {
//						mxMorphing morph = new mxMorphing(graphComponent, 20,
//								1.2, 20);
//
//						morph.addListener(mxEvent.DONE, new mxIEventListener() {
//
//							public void invoke(Object sender, mxEventObject evt) {
//								graph.getModel().endUpdate();
//							}
//						});
//
//						morph.startAnimation();
//					}
//
//				}
//			};
//		} else {
//			return new AbstractAction(mxResources.get(key)) {
//
//				public void actionPerformed(ActionEvent e) {
//					JOptionPane.showMessageDialog(graphComponent, "No Layout");
//				}
//			};
//		}
//	}
//
//	protected mxIGraphLayout createLayout(String ident, boolean animate) {
//		mxIGraphLayout layout = null;
//
//		if (ident != null) {
//			Graph graph = (Graph) graphComponent.getGraph();
//
//			if (ident.equals("verticalHierarchical")) {
//				layout = new mxHierarchicalLayout(graph);
//			} else if (ident.equals("horizontalHierarchical")) {
//				layout = new mxHierarchicalLayout(graph, JLabel.WEST);
//			} else if (ident.equals("verticalTree")) {
//				layout = new mxCompactTreeLayout(graph, false);
//			} else if (ident.equals("horizontalTree")) {
//				layout = new mxCompactTreeLayout(graph, true);
//			} else if (ident.equals("parallelEdges")) {
//				layout = new mxParallelEdgeLayout(graph);
//			} else if (ident.equals("placeEdgeLabels")) {
//				layout = new mxEdgeLabelLayout(graph);
//			} else if (ident.equals("organicLayout")) {
//				layout = new mxOrganicLayout(graph);
//			}
//			if (ident.equals("verticalStack")) {
//				layout = new mxStackLayout(graph, false) {
//
//					/**
//					 * Overrides the empty implementation to return the size of
//					 * the graph control.
//					 */
//					@Override
//					public mxRectangle getContainerSize() {
//						return graphComponent.getLayoutAreaSize();
//					}
//				};
//			} else if (ident.equals("horizontalStack")) {
//				layout = new mxStackLayout(graph, true) {
//
//					/**
//					 * Overrides the empty implementation to return the size of
//					 * the graph control.
//					 */
//					@Override
//					public mxRectangle getContainerSize() {
//						return graphComponent.getLayoutAreaSize();
//					}
//				};
//			} else if (ident.equals("circleLayout")) {
//				layout = new mxCircleLayout(graph);
//			}
//		}
//
//		return layout;
//	}

}
