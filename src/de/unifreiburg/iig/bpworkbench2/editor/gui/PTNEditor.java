package de.unifreiburg.iig.bpworkbench2.editor.gui;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUndoableEdit.mxUndoableChange;
//import com.mxgraph.view.mxGraph;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.PTGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPNNode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;
import de.unifreiburg.iig.bpworkbench2.editor.gui.actions.DeleteAction;
import de.unifreiburg.iig.bpworkbench2.editor.gui.actions.HistoryAction;
import de.unifreiburg.iig.bpworkbench2.editor.gui.actions.NewAction;
import de.unifreiburg.iig.bpworkbench2.editor.gui.actions.OpenAction;
import de.unifreiburg.iig.bpworkbench2.editor.gui.actions.PrintAction;
import de.unifreiburg.iig.bpworkbench2.editor.gui.actions.SaveAction;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Constants;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Graph;
//import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPN;
//import de.uni.freiburg.iig.telematik.sepia.graphic.NodeGraphics;
//import de.uni.freiburg.iig.telematik.sepia.graphic.PNGraphics;
//import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
//import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
//import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
//import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPNNode;
//import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
//import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
//import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
//import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
//import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
//import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;

public class PTNEditor extends JPanel {

	private static HashMap<Object, Object> createdVertices = new HashMap<Object, Object>();
	SortedMap<Integer, String> placeSortedMap = new TreeMap<Integer, String>();
	SortedMap<Integer, String> transitionSortedMap = new TreeMap<Integer, String>();
	private static double size_x;
	private static double size_y;
	protected mxGraphComponent graphComponent;
	protected JPanel libraryPane;
	private ControlPanel controlPanel;
	protected mxUndoManager undoManager;
	protected String appTitle;
	protected JLabel statusBar;
	protected static File currentFile;
	private static AbstractPetriNet<?, ?, ?, ?, ?> pn;
	protected boolean modified = false;
	protected mxRubberband rubberband;
	protected mxKeyboardHandler keyboardHandler;
	protected mxIEventListener undoHandler = new mxIEventListener() {

		public void invoke(Object source, mxEventObject evt) {
			// MatrixHolder.createMatrix();
			// ControlPanel.updateTables();
			undoManager.undoableEditHappened((mxUndoableEdit) evt
					.getProperty("edit"));
		}
	};
	protected mxIEventListener changeTracker = new mxIEventListener() {

		public void invoke(Object source, mxEventObject evt) {
			setModified(true);
		}
	};
	public AbstractGraphicalPN<?, ?, ?, ?, ?>  netContainer;

	public AbstractGraphicalPN<?, ?, ?, ?, ?> getNetContainer() {
		return netContainer;
	}

	public void setNetContainer(AbstractGraphicalPN<?, ?, ?, ?, ?> netContainer) {
		this.netContainer = netContainer;
	}

	public PTNEditor() {
		this("PetriNet Editor", new GraphComponent(new Graph()));
	}

	/**
     *
     */
	public PTNEditor(String appTitle, mxGraphComponent component) {
		this.appTitle = appTitle;
		graphComponent = component;
		final Graph graph = (Graph) graphComponent.getGraph();
		


		undoManager = new mxUndoManager();
		graph.getModel().addListener(mxEvent.CHANGE, changeTracker); // S.T.A.R.S.
		graph.getModel().addListener(mxEvent.UNDO, undoHandler);
		graph.getView().addListener(mxEvent.UNDO, undoHandler);
		mxIEventListener handler = new mxIEventListener() {

			public void invoke(Object source, mxEventObject evt) {
				List<mxUndoableChange> changes = ((mxUndoableEdit) evt
						.getProperty("edit")).getChanges();
				graph.setSelectionCells(graph
						.getSelectionCellsForChanges(changes));
			}
		};
		undoManager.addListener(mxEvent.UNDO, handler);
		undoManager.addListener(mxEvent.REDO, handler);

		libraryPane = new JPanel();

		JSplitPane inner = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				libraryPane, graphComponent);
		inner.setDividerLocation(50);
		inner.setDividerSize(0);
		inner.setBorder(null);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(inner, BorderLayout.CENTER);
		panel.add(new ToolBar(this, JToolBar.HORIZONTAL), BorderLayout.NORTH);

		controlPanel = new ControlPanel(graph);
//		graph.getDataHolder().setDataWatcher(controlPanel);
		final JSplitPane outer = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				panel, controlPanel);
		outer.setDividerSize(9);
		outer.setOneTouchExpandable(true);
		outer.setDividerLocation(650);
		outer.setBorder(null);
		BasicSplitPaneDivider dividerContainer = (BasicSplitPaneDivider) outer
				.getComponent(2);
		dividerContainer.setBorder(null);
		dividerContainer.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
		outer.setContinuousLayout(true);

		setLayout(new BorderLayout());

		/*
		 * different panels to add: inner: graphcomponent + palette 
		 * panel: inner + upper icons
		 * outer: panel + contolPanel on the right
		 */
		add(panel, BorderLayout.CENTER);
		statusBar = new JLabel();
		statusBar.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

		// adds statusbar with pixel information
		// add(statusBar, BorderLayout.SOUTH);
		rubberband = new mxRubberband(graphComponent);
		keyboardHandler = new KeyboardHandler(graphComponent);
		installListeners();
		updateTitle();

		Palette shapesPalette = (Palette) libraryPane.add(new Palette());

		// shapesPalette.addTemplate(
		// "Container",
		// new ImageIcon(
		// PNMLEditor.class.getResource("/images/swimlane.png")),
		// "swimlane", 150, 150, new CellInfo("     Container"));
		// shapesPalette.getComponent(0).setEnabled(false);
		shapesPalette.addTemplate(
				"Transition",
				new ImageIcon(PTNEditor.class
						.getResource("/images/rectangle.png")), Constants.PNTransitionShape ,
				30, 30,  null);
		// shapesPalette.addTemplate(
		// "Immediate",
		// new ImageIcon(
		// PNMLEditor.class.getResource("/images/rectangle2.png")),
		// "immediate;fontSize=12", 20, 60, new CellInfo(0.0, 0.5));
		shapesPalette.addTemplate(
				"Place",
				new ImageIcon(PTNEditor.class
						.getResource("/images/ellipse.png")),
				Constants.PNPlaceShape   , 30, 30, null  );
	}
	public int getLowestIndex(SortedMap<Integer, String> a) {
		if(!a.isEmpty()){
		for(int i = 1;i<=a.lastKey();i++){
			if(a.get(i) == null)
			return i;
		}
		;
		return a.lastKey()+1;}
		else{return 1;}
	}

	
	public PTNEditor(AbstractGraphicalPN<?, ?, ?, ?, ?> netContainer2) {
		this("PetriNet Editor", new GraphComponent(new Graph()));
		
		netContainer = netContainer2;
		if(netContainer == null)
			netContainer = new GraphicalPTNet(new PTNet(), new PTGraphics());
		((Graph)graphComponent.getGraph()).setPN(netContainer);
		graphComponent = visualizeGraph(netContainer, graphComponent);
		graphComponent.getViewport().setOpaque(true);
		graphComponent.getViewport().setBackground(Constants.blueBG);

		Map<String, Object> style = graphComponent.getGraph().getStylesheet().getDefaultEdgeStyle();
		System.out.println(style);
		style.put("strokeWidth",2.0); 
		style.put("strokeColor",Integer.toHexString(Constants.bluelow.getRGB()));
	}

	private void setLayoutOrganic(final Graph graph) {
		mxOrganicLayout layout = new mxOrganicLayout(graph);
		// Execute layout
		// final mxGraph graph =graph;
		Object cell = graph.getSelectionCell();

		if (cell == null || graph.getModel().getChildCount(cell) == 0) {
			cell = graph.getDefaultParent();
		}

		graph.getModel().beginUpdate();
		try {
			long t0 = System.currentTimeMillis();
			layout.execute(cell);
			// status("Layout: " + (System.currentTimeMillis() - t0)
			// + " ms");
		} finally {
			mxMorphing morph = new mxMorphing(graphComponent, 20, 1.2, 20);

			morph.addListener(mxEvent.DONE, new mxIEventListener() {

				public void invoke(Object sender, mxEventObject evt) {
					graph.getModel().endUpdate();
				}

			});

			morph.startAnimation();
		}

	}

	protected static mxGraphComponent visualizeGraph(
			AbstractGraphicalPN<?, ?, ?, ?, ?> n,
			mxGraphComponent graphComponent2) {
		Graph graph = (Graph) graphComponent2.getGraph();
		if(n != null){
			System.out.println("here");
		pn = n.getPetriNet();
		AbstractPNGraphics<?, ?, ?, ?, ?> pnG = n.getPetriNetGraphics();
		
		graph.getModel().beginUpdate();
//		map2Str(pnG.getTokenGraphics(), graph);

		traverseFlowRelation(pn, graph, n);

		graph.getModel().endUpdate();
		}
		return graphComponent2;
	}

	private static void traverseFlowRelation(
			AbstractPetriNet<?, ?, ?, ?, ?> pn, Graph graph,
			AbstractGraphicalPN<?, ?, ?, ?, ?> n) {
		if (graph.getDefaultParent() instanceof mxCell)
			;
		((mxCell) graph.getDefaultParent()).setValue(n);
		for (Object oFlowRelation : pn.getFlowRelations()) {
			Object source = null;
			Object target = null;
			PTFlowRelation ptFlowRelation = null;
			if (oFlowRelation instanceof PTFlowRelation) {
				ptFlowRelation = (PTFlowRelation) oFlowRelation;
				AbstractPNNode<?> sourceNode = ptFlowRelation.getSource();

				source = getEndpoint(sourceNode, createdVertices, n, graph);
				AbstractPNNode<?> targetNode = ptFlowRelation.getTarget();
				target = getEndpoint(targetNode, createdVertices, n, graph);

				graph.insertEdge(null, ptFlowRelation.getName(),
						ptFlowRelation.getWeight(), source, target);

			}

//			CPNFlowRelation cpnFlowRelation = null;
//			if (oFlowRelation instanceof CPNFlowRelation) {
//				cpnFlowRelation = (CPNFlowRelation) oFlowRelation;
//
//				AbstractPNNode<?> sourceNode = cpnFlowRelation.getSource();
//
//				source = getEndpoint(sourceNode, createdVertices, n, graph);
//				AbstractPNNode<?> targetNode = cpnFlowRelation.getTarget();
//				target = getEndpoint(targetNode, createdVertices, n, graph);
//
//				graph.insertEdge(null, cpnFlowRelation.getName(), "test",
//						source, target);
//
//			}
		
		
//			CWNFlowRelation cwnFlowRelation = null;
//			if (oFlowRelation instanceof CWNFlowRelation) {
//				cwnFlowRelation = (CWNFlowRelation) oFlowRelation;
//
//				AbstractPNNode<?> sourceNode = cwnFlowRelation.getSource();
//
//				source = getEndpoint(sourceNode, createdVertices, n, graph);
//				AbstractPNNode<?> targetNode = cwnFlowRelation.getTarget();
//				target = getEndpoint(targetNode, createdVertices, n, graph);
//
//				graph.insertEdge(null, cwnFlowRelation.getName(), "test",
//						source, target);
//
//			}
	}

	}

	private static Object getEndpoint(AbstractPNNode<?> sourceNode,
			HashMap<Object, Object> createdVertices2,
			AbstractGraphicalPN<?, ?, ?, ?, ?> n, Graph graph) {
		Object endpoint = null;
		if (sourceNode instanceof PTPlace) {
			PTPlace place = (PTPlace) sourceNode;
			if (!createdVertices2.keySet().contains(place)) {

				NodeGraphics pG = (NodeGraphics) n.getPetriNetGraphics().getPlaceGraphics().get(
						place.getName());
				endpoint = addVertex(graph, place.getName() , n, pG,
						 Constants.PNPlaceShape, place.getName());
				createdVertices2.put(place, endpoint);
			} else
				endpoint = createdVertices2.get(place);
		}
		if (sourceNode instanceof PTTransition) {
			PTTransition transition = (PTTransition) sourceNode;
			if ((!createdVertices2.keySet().contains(transition))) {
				NodeGraphics pT = (NodeGraphics) n.getPetriNetGraphics().getTransitionGraphics()
						.get(transition.getName());

				endpoint = addVertex(graph, transition.getName(), n,  pT,
						Constants.PNTransitionShape, transition.getName());
				createdVertices2.put(transition, endpoint);
//				System.out.println(createdVertices2.size());
			} else
				endpoint = createdVertices2.get(transition);
		}
//		if (sourceNode instanceof CPNPlace) {
//			CPNPlace place = (CPNPlace) sourceNode;
//			if (!createdVertices2.keySet().contains(place)) {
//
//				NodeGraphics pG = (NodeGraphics) n.getPetriNetGraphics().getPlaceGraphics().get(
//						place.getName());
//				try {
//System.out.println(					pn.getMarking().get(place.getName())
//);				} catch (ParameterException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
////				System.out.println(pnG.getTokenGraphics().entrySet());
//				endpoint = addVertex(graph, null, n, pG,
//						//TODO: different styles to test
////						mxConstants.SHAPE_ELLIPSE+ ";image=http://www.jgraph.com/images/mxgraph.gif;"
////								+ "perimeter=rectanglePerimeter;imageVerticalAlign=top;fontStyle=1;align=center;"
////								+ "verticalAlign=top;spacing=2;spacingTop=40;imageAlign=center;"   
////								+ "imageWidth=40;imageHeight=40;rounded=1;"
////								+ "shadow=1;glass=1;");
//						Constants.PNPlaceShape, place.getName());
//				mxCell cell = (mxCell) endpoint;
//				createdVertices2.put(place, endpoint);
//			} else
//				endpoint = createdVertices2.get(place);
//		}
//		if (sourceNode instanceof CPNTransition) {
//			CPNTransition transition = (CPNTransition) sourceNode;
//			if ((!createdVertices2.keySet().contains(transition))) {
//				NodeGraphics pT = (NodeGraphics) n.getPetriNetGraphics().getTransitionGraphics()
//						.get(transition.getName());
//
//				endpoint = addVertex(graph, transition.getName(), n, pT,
//						mxConstants.SHAPE_RECTANGLE, transition.getName());
//				createdVertices2.put(transition, endpoint);
//			} else
//				endpoint = createdVertices2.get(transition);
//		}
//		if (sourceNode instanceof CWNPlace) {
//			CWNPlace place = (CWNPlace) sourceNode;
//			if (!createdVertices2.keySet().contains(place)) {
//
//				NodeGraphics pG = (NodeGraphics) n.getPetriNetGraphics().getPlaceGraphics().get(
//						place.getName());
//
//				endpoint = addVertex(graph, place.getName(), n, pG,
//						mxConstants.SHAPE_ELLIPSE, place.getName());
//				createdVertices2.put(place, endpoint);
//			} else
//				endpoint = createdVertices2.get(place);
//		}
//		if (sourceNode instanceof CWNTransition) {
//			CWNTransition transition = (CWNTransition) sourceNode;
//			if ((!createdVertices2.keySet().contains(transition))) {
//				NodeGraphics pT = (NodeGraphics) n.getPetriNetGraphics().getTransitionGraphics()
//						.get(transition.getName());
//
//				endpoint = addVertex(graph, transition.getName(), n, pT,
//						mxConstants.SHAPE_RECTANGLE, transition.getName());
//				createdVertices2.put(transition, endpoint);
//			} else
//				endpoint = createdVertices2.get(transition);
//		}


		return endpoint;
	}
	//Currently not in use, "how to read the tokens" in progress
//	protected static <A extends Object, B extends Object> mxGraph map2Str(
//			Map<A, B> m, mxGraph graph) {
//		// boolean empty = true;
//		// StringBuilder str = new StringBuilder();
//		for (Entry<A, B> pairs : m.entrySet()) {
//			System.out.println(pairs);
//			TokenGraphics tg = (TokenGraphics) pairs.getValue();
//			tg.getColorName();
////			if (pairs.getKey().getClass().toString().contains("Place"))
//////				graph.insertVertex(graph.getDefaultParent(), pairs.getKey()
//////						.toString(), new Object(), ((NodeGraphics) pairs
//////						.getValue()).getPosition().getX(),
//////						((NodeGraphics) pairs.getValue()).getPosition().getY(),
//////						size_x, size_y, mxConstants.SHAPE_ELLIPSE);
////			if (pairs.getKey().getClass().toString().contains("Transition"))
//////				graph.insertVertex(graph.getDefaultParent(), pairs.getKey()
//////						.toString(), new Object(), ((NodeGraphics) pairs
//////						.getValue()).getPosition().getX(),
//////						((NodeGraphics) pairs.getValue()).getPosition().getY(),
//////						size_x, size_y, mxConstants.SHAPE_RECTANGLE);
//			// if(pairs.getKey().getClass().toString().contains("Transition"))
//			// graph.insertEdge((graph.getDefaultParent(),
//			// pairs.getKey().toString(), , arg3, arg4)
//			// str.append("\n");
//			// str.append("                            " + pairs.getKey() + ": "
//			// + pairs.getValue());
//			// empty = false;
//			// graph.ins
//		}
//		return graph;
//	}
//	private static Object addVertex(mxGraph graph, String name,
//			Object transition, NodeGraphics pG, String shape) {
//		Object vertex = graph
//				.insertVertex(graph.getDefaultParent(), name, transition, pG.getPosition()
//						.getX(), pG.getPosition().getY(), pG
//						.getDimension().getX(), pG.getDimension()
//						.getY(), shape);
//		
//		return vertex;
//	}
	private static Object addVertex(Graph graph, String name,
			Object n, NodeGraphics pG, String shape, String id) {
		mxCell vertex = (mxCell) graph
				.insertVertexInPN((AbstractGraphicalPN<?, ?, ?, ?, ?>) n, graph.getDefaultParent(), name, null, pG.getPosition()
						.getX(), pG.getPosition().getY(), pG
						.getDimension().getX(), pG.getDimension()
						.getY(), shape);
		vertex.setId(id);
		
		return vertex;
	}

	protected void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0) {
			graphComponent.zoomIn();
		} else {
			graphComponent.zoomOut();
		}
		status("Scale: "
				+ (int) (100 * graphComponent.getGraph().getView().getScale())
				+ "%");
	}

	protected void showGraphPopupMenu(MouseEvent e) {
		Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(),
				graphComponent);
		PopupMenu menu = new PopupMenu(PTNEditor.this);
		menu.show(graphComponent, pt.x, pt.y);
		e.consume();
	}

	protected void mouseLocationChanged(MouseEvent e) {
		status(e.getX() + ", " + e.getY());
	}

	protected void installListeners() {
		graphComponent.addMouseWheelListener(new MouseWheelListener() {

			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getSource() instanceof mxGraphOutline
						|| e.isControlDown()) {
					PTNEditor.this.mouseWheelMoved(e);
				}
			}
		});

		graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showGraphPopupMenu(e);
				}
			}
		});

		graphComponent.getGraphControl().addMouseMotionListener(
				new MouseMotionListener() {

					public void mouseDragged(MouseEvent e) {
						mouseLocationChanged(e);
					}

					public void mouseMoved(MouseEvent e) {
						mouseDragged(e);
					}
				});
	}

	public void setCurrentFile(File file) {
		File oldValue = currentFile;
		currentFile = file;

		firePropertyChange("currentFile", oldValue, file);

		if (oldValue != file) {
			updateTitle();
		}
	}

	public File getCurrentFile() {
		return currentFile;
	}

	public void setModified(boolean modified) {
		boolean oldValue = this.modified;
		this.modified = modified;

		firePropertyChange("modified", oldValue, modified);

		if (oldValue != modified) {
			updateTitle();
		}
	}

	public boolean isModified() {
		return modified;
	}

	public mxGraphComponent getGraphComponent() {
		return graphComponent;
	}

	public mxUndoManager getUndoManager() {
		return undoManager;
	}

	public ControlPanel getControlPanel() {
		return controlPanel;
	}

	public Action bind(String name, final Action action) {
		return bind(name, action, null);
	}

	public Action bind(String name, final Action action, String iconUrl) {
		return new AbstractAction(name, (iconUrl != null) ? new ImageIcon(
				PTNEditor.class.getResource(iconUrl)) : null) {

			public void actionPerformed(ActionEvent e) {
				action.actionPerformed(new ActionEvent(getGraphComponent(), e
						.getID(), e.getActionCommand()));
			}
		};
	}

	public void status(String msg) {
		statusBar.setText(msg);
	}

	public void updateTitle() {
		JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);

		if (frame != null) {
			String title = (currentFile != null) ? currentFile
					.getAbsolutePath() : "New Diagram";
			if (modified) {
				title += "*";
			}
			frame.setTitle(title + " - " + appTitle);
		}
	}

	public void about() {
		JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);
		if (frame != null) {
			AboutFrame about = new AboutFrame(frame);
			about.setModal(true);
			int x = frame.getX() + (frame.getWidth() - about.getWidth()) / 2;
			int y = frame.getY() + (frame.getHeight() - about.getHeight()) / 2;
			about.setLocation(x, y);
			about.setVisible(true);
		}
	}

	public Action graphLayout(final String key, boolean animate) {
		final mxIGraphLayout layout = createLayout(key, animate);

		if (layout != null) {
			return new AbstractAction(mxResources.get(key)) {

				public void actionPerformed(ActionEvent e) {
					final Graph graph = (Graph) graphComponent.getGraph();
					Object cell = graph.getSelectionCell();

					if (cell == null
							|| graph.getModel().getChildCount(cell) == 0) {
						cell = graph.getDefaultParent();
					}

					graph.getModel().beginUpdate();
					try {
						long t0 = System.currentTimeMillis();
						layout.execute(cell);
						status("Layout: " + (System.currentTimeMillis() - t0)
								+ " ms");
					} finally {
						mxMorphing morph = new mxMorphing(graphComponent, 20,
								1.2, 20);

						morph.addListener(mxEvent.DONE, new mxIEventListener() {

							public void invoke(Object sender, mxEventObject evt) {
								graph.getModel().endUpdate();
							}
						});

						morph.startAnimation();
					}

				}
			};
		} else {
			return new AbstractAction(mxResources.get(key)) {

				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(graphComponent, "No Layout");
				}
			};
		}
	}

	protected mxIGraphLayout createLayout(String ident, boolean animate) {
		mxIGraphLayout layout = null;

		if (ident != null) {
			Graph graph = (Graph) graphComponent.getGraph();

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
			if (ident.equals("verticalStack")) {
				layout = new mxStackLayout(graph, false) {

					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
					@Override
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
					@Override
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

	public class KeyboardHandler extends mxKeyboardHandler {

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
				map.put(KeyStroke.getKeyStroke("control shift V"),
						"selectVertices");
				map.put(KeyStroke.getKeyStroke("control shift E"),
						"selectEdges");
				map.put(KeyStroke.getKeyStroke("control P"),
						"printNet");
				map.put(KeyStroke.getKeyStroke("DELETE"), "delete");
			}

			return map;
		}

		@Override
		protected ActionMap createActionMap() {
			ActionMap map = super.createActionMap();

			map.put("save", new SaveAction(false));
			map.put("saveAs", new SaveAction(true));
			map.put("new", new NewAction());
			map.put("open", new OpenAction());
			map.put("undo", new HistoryAction(true));
			map.put("redo", new HistoryAction(false));
			map.put("selectVertices", mxGraphActions.getSelectVerticesAction());
			map.put("selectEdges", mxGraphActions.getSelectEdgesAction());
			map.put("printNet", new PrintAction());
			map.put("delete", new DeleteAction("delete"));

			return map;
		}
	}

}
