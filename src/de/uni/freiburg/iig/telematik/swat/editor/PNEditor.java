package de.uni.freiburg.iig.telematik.swat.editor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.mxPartitionLayout;
import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.swing.util.mxGraphActions.DeleteAction;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxUndoableEdit.mxUndoableChange;
import com.mxgraph.view.mxGraph;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.swat.editor.actions.PrintAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.RedoAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.SaveAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.UndoAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphComponent;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphListener;
import de.uni.freiburg.iig.telematik.swat.editor.menu.EditorPopupMenu;
import de.uni.freiburg.iig.telematik.swat.editor.menu.PalettePanel;
import de.uni.freiburg.iig.telematik.swat.editor.menu.ToolBar;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PropertiesView;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponent;

public abstract class PNEditor extends JPanel implements SwatComponent, TreeSelectionListener, PNGraphListener {

	private static final long serialVersionUID = 1023415244830760771L;
	private static final String scaleMessageFormat = "Scale: %s %%";

	protected JPanel statusPanel = null;
	protected PalettePanel palettePanel = null;
	protected PNGraphComponent graphComponent;
	protected ToolBar toolbar = null;
	protected mxRubberband rubberband;
	protected mxKeyboardHandler keyboardHandler;
	protected mxUndoManager undoManager;
	protected mxIEventListener undoHandler = new mxIEventListener() {
		public void invoke(Object source, mxEventObject evt) {
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
	public AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> netContainer = null;

	// ------- Constructors --------------------------------------------------------------------

	public PNEditor(File fileReference) throws ParameterException {
		super();
		initialize(null, fileReference);
		setUpGUI();
	}

	public PNEditor(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> netContainer, File fileReference) throws ParameterException {
		super();
		Validate.notNull(netContainer);
		initialize(netContainer, fileReference);
		setUpGUI();
	}

	public String getName() {
		return fileReference.getName();
	}

	private void initialize(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> netContainer, File fileReference) throws ParameterException {
		if (netContainer == null) {
			this.netContainer = createNetContainer();
		} else {
			this.netContainer = netContainer;
		}
		setFileReference(fileReference);
		properties = createPNProperties();
		propertiesView = new PropertiesView(properties);
		propertiesView.addTreeSelectionListener(this);
		properties.addPNPropertiesListener(propertiesView);
		properties.setPropertiesView(propertiesView);
	}

	protected abstract AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> createNetContainer();

	protected abstract PNProperties createPNProperties();

	// ------- Set Up GUI -----------------------------------------------------------------------

	private void setUpGUI() throws ParameterException {
		setLayout(new BorderLayout());
		add(getToolbar(), BorderLayout.NORTH);
		add(getPalettePanel(), BorderLayout.LINE_START);
		add(getGraphComponent(), BorderLayout.CENTER);
		// add(getStatusPanel(), BorderLayout.SOUTH);

		rubberband = new mxRubberband(graphComponent);
		keyboardHandler = new KeyboardHandler(graphComponent);

	}

	private ToolBar getToolbar() {
		if (toolbar == null) {
			try {
				toolbar = new ToolBar(this, JToolBar.HORIZONTAL);
			} catch (ParameterException e) {
				// Cannot happen, since this is not null.
				e.printStackTrace();
			}
		}
		return toolbar;
	}

	public PNGraphComponent getGraphComponent() {
		if (graphComponent == null) {
			graphComponent = createGraphComponent();
			graphComponent.setPopupMenu(getPopupMenu());
			graphComponent.getViewport().setOpaque(true);
			graphComponent.getViewport().setBackground(MXConstants.blueBG);

			Map<String, Object> style = getGraph().getStylesheet().getDefaultEdgeStyle();
			style.put("strokeWidth", 2.0);
			style.put("strokeColor", mxUtils.hexString(MXConstants.bluelow));

			addGraphComponentListeners();
			setUpUndo();
		}
		return graphComponent;
	}

	protected abstract PNGraphComponent createGraphComponent();

	private void addGraphComponentListeners() {

		graphComponent.getGraphControl().addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
			}

			public void mouseMoved(MouseEvent e) {
				displayStatusMessage(e.getX() + ", " + e.getY());
			}
		});
		
		graphComponent.getGraph().addPNGraphListener(this);
	}

	private void setUpUndo() {
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

	@Override
	public JComponent getMainComponent() {
		return this;
	}

	private JPanel getPalettePanel() throws ParameterException {
		if (palettePanel == null) {
			palettePanel = new PalettePanel();
		}
		return palettePanel;
	}

	private JPanel getStatusPanel() {
		if (statusPanel == null) {
			statusPanel = new JPanel();
			JLabel statusBar = new JLabel();
			statusBar.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
			statusPanel.add(statusBar);
		}
		return statusPanel;
	}

	// ------- Functionality
	// --------------------------------------------------------------------

	public AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> getNetContainer() {
		return netContainer;
	}

	public PropertiesView getPropertiesView() {
		return propertiesView;
	}

	protected PNProperties getPNProperties() {
		return properties;
	}

	private PNGraph getGraph() {
		return graphComponent.getGraph();
	}

	public void setPlaceLabel(String placeName, String placeLabel) throws ParameterException {
		Validate.notNull(placeName);
		Validate.notNull(placeLabel);
		Validate.notEmpty(placeName);
		Validate.notEmpty(placeLabel);
		if (!getNetContainer().getPetriNet().containsPlace(placeName))
			throw new ParameterException("Unknown place.");

		properties.setPlaceLabel(this, placeName, placeLabel);
	}

	public abstract EditorPopupMenu getPopupMenu();

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


	public void displayStatusMessage(String msg) {
		// TODO: Do something
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
				map.put("undo", new UndoAction(PNEditor.this));
				map.put("redo", new RedoAction(PNEditor.this));
				map.put("printNet", new PrintAction(PNEditor.this));
			} catch (Exception e) {
				// Cannot happen, since this is not null
				e.printStackTrace();
			}

			map.put("selectVertices", mxGraphActions.getSelectVerticesAction());
			map.put("selectEdges", mxGraphActions.getSelectEdgesAction());
			map.put("delete", new DeleteAction("delete"));
			return map;
		}
	}

	/**
	 * Creates an action that executes the specified layout.
	 * 
	 * @param key
	 *            Key to be used for getting the label from mxResources and also
	 *            to create the layout instance for the commercial graph editor
	 *            example.
	 * @return an action that executes the specified layout
	 */
	@SuppressWarnings("serial")
	public Action graphLayout(final String key, boolean animate) {
		final mxIGraphLayout layout = createLayout(key, animate);

		if (layout != null) {
			return new AbstractAction(mxResources.get(key)) {
				public void actionPerformed(ActionEvent e) {
					final mxGraph graph = graphComponent.getGraph();
					Object cell = graph.getSelectionCell();

					if (cell == null || graph.getModel().getChildCount(cell) == 0) {
						cell = graph.getDefaultParent();
					}

					graph.getModel().beginUpdate();
					try {
						long t0 = System.currentTimeMillis();
						layout.execute(cell);
						status("Layout: " + (System.currentTimeMillis() - t0) + " ms");
					} finally {
						mxMorphing morph = new mxMorphing(graphComponent, 20, 1.2, 20);

						morph.addListener(mxEvent.DONE, new mxIEventListener() {

							public void invoke(Object sender, mxEventObject evt) {
								graph.getModel().endUpdate();
								getGraph().updatePositionPropertiesFromCells();
							}

						});

						morph.startAnimation();
					}

				}

			};
		} else {
			return new AbstractAction(mxResources.get(key)) {

				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(graphComponent, mxResources.get("noLayout"));
				}

			};
		}
	}

	/**
	 * Creates a layout instance for the given identifier.
	 */
	protected mxIGraphLayout createLayout(String ident, boolean animate) {
		mxIGraphLayout layout = null;

		if (ident != null) {
			mxGraph graph = graphComponent.getGraph();

			if (ident.equals("verticalHierarchical")) {
				layout = new mxHierarchicalLayout(graph);
			} else if (ident.equals("horizontalHierarchical")) {
				layout = new mxHierarchicalLayout(graph, JLabel.WEST);
			} else if (ident.equals("verticalTree")) {
				layout = new mxCompactTreeLayout(graph, false);
			} else if (ident.equals("horizontalTree")) {
				layout = new mxCompactTreeLayout(graph, true);
			} else if (ident.equals("parallelEdges")) {
				layout = new mxParallelEdgeLayout(graph);
			} else if (ident.equals("placeEdgeLabels")) {
				layout = new mxEdgeLabelLayout(graph);
			} else if (ident.equals("organicLayout")) {
				layout = new mxOrganicLayout(graph);
			}
			if (ident.equals("verticalPartition")) {
				layout = new mxPartitionLayout(graph, false) {
					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
					public mxRectangle getContainerSize() {
						return graphComponent.getLayoutAreaSize();
					}
				};
			} else if (ident.equals("horizontalPartition")) {
				layout = new mxPartitionLayout(graph, true) {
					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
					public mxRectangle getContainerSize() {
						return graphComponent.getLayoutAreaSize();
					}
				};
			} else if (ident.equals("verticalStack")) {
				layout = new mxStackLayout(graph, false) {
					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
					public mxRectangle getContainerSize() {
						return graphComponent.getLayoutAreaSize();
					}
				};
			} else if (ident.equals("horizontalStack")) {
				layout = new mxStackLayout(graph, true) {
					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
					public mxRectangle getContainerSize() {
						return graphComponent.getLayoutAreaSize();
					}
				};
			} else if (ident.equals("circleLayout")) {
				layout = new mxCircleLayout(graph);
			}
		}

		return layout;
	}
	
	

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if(e.getSource() == null || e.getSource() == this)
			return;
		JTree sourceTree = null;
		try{
			sourceTree = (JTree) e.getSource();
		} catch(Exception ex){
			return;
		}
		PNTreeNode node = null;
		try{
			node = (PNTreeNode) sourceTree.getLastSelectedPathComponent();
		}catch(Exception ex){
			return;
		}
		treeNodeSelected(node);
	}
	
	private void treeNodeSelected(PNTreeNode node){
		if(node == null){
			return;
		}
		switch(node.getFieldType()){
		case ARC:
			getGraph().selectArc(node.toString());
			break;
		case TRANSITION:
			getGraph().selectTransition(node.toString());
			break;
		case PLACE:
			getGraph().selectPlace(node.toString());
			break;
		case LEAF:
			PNTreeNode parentNode = null;
			try{
				parentNode = (PNTreeNode) node.getParent();
			}catch(Exception ex){
				return;
			}
			treeNodeSelected(parentNode);
			break;
		default:
			getGraph().clearSelection();
		}
	}
	
	

	@Override
	public void placeAdded(AbstractPlace place) {
		propertiesView.componentAdded(PNComponent.PLACE, place.getName());
	}

	@Override
	public void transitionAdded(AbstractTransition transition) {
		propertiesView.componentAdded(PNComponent.TRANSITION, transition.getName());
	}

	@Override
	public void relationAdded(AbstractFlowRelation relation) {
		propertiesView.componentAdded(PNComponent.ARC, relation.getName());
	}

	@Override
	public void placeRemoved(AbstractPlace place) {
		propertiesView.componentRemoved(PNComponent.PLACE, place.getName());
	}

	@Override
	public void transitionRemoved(AbstractTransition transition) {
		propertiesView.componentRemoved(PNComponent.TRANSITION, transition.getName());
	}

	@Override
	public void relationRemoved(AbstractFlowRelation relation) {
		propertiesView.componentRemoved(PNComponent.ARC, relation.getName());
	}

	@Override
	public void componentsSelected(Set<PNGraphCell> selectedComponents) {
		if(selectedComponents == null || selectedComponents.isEmpty() || selectedComponents.size() > 1){
			propertiesView.deselect();
		} else {
			PNGraphCell selectedCell = selectedComponents.iterator().next();
			propertiesView.selectNode(selectedCell.getId());
		}
		toolbar.updateView(selectedComponents);
	}

	/**
	 * 
	 * @param msg
	 */
	public void status(String msg) {
		// statusBar.setText(msg);
	}

}
