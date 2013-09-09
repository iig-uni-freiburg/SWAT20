package de.unifreiburg.iig.bpworkbench2.editor.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;

import com.mxgraph.layout.*;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.*;
import com.mxgraph.swing.handler.*;
import com.mxgraph.swing.util.*;
import com.mxgraph.util.*;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUndoableEdit.mxUndoableChange;
import com.mxgraph.view.mxGraph;

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



import de.unifreiburg.iig.bpworkbench2.editor.gui.actions.*;
import de.unifreiburg.iig.bpworkbench2.editor.soul.CellInfo;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Graph;

import javax.swing.plaf.basic.BasicSplitPaneDivider;

public final class PNMLEditor extends JPanel {

	protected mxGraphComponent graphComponent;
	protected JPanel libraryPane;
	private ControlPanel controlPanel;
	protected mxUndoManager undoManager;
	protected String appTitle;
	protected JLabel statusBar;
	protected File currentFile;
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

	public PNMLEditor() {
		this("PetriNet Editor", new GraphComponent(new Graph()));
	}

	/**
     *
     */
	public PNMLEditor(String appTitle, mxGraphComponent component) {
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
		graph.getDataHolder().setDataWatcher(controlPanel);
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
				new ImageIcon(PNMLEditor.class
						.getResource("/images/rectangle.png")), "fontSize=12",
				40, 40, new CellInfo(2.0, 0.5));
		// shapesPalette.addTemplate(
		// "Immediate",
		// new ImageIcon(
		// PNMLEditor.class.getResource("/images/rectangle2.png")),
		// "immediate;fontSize=12", 20, 60, new CellInfo(0.0, 0.5));
		shapesPalette.addTemplate(
				"Place",
				new ImageIcon(PNMLEditor.class
						.getResource("/images/ellipse.png")),
				"ellipse;fontSize=12", 40, 40, new CellInfo(0));
	}

	public PNMLEditor(File file) {

		this("PetriNet Editor", new GraphComponent(new Graph()));
		// if(file != null){
		// GraphicalPN net = null;
		// try {
		// net = new PNMLParser().parse(file);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (ParserException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (ParameterException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// visualizeGraph(net, this.getGraphComponent().getGraph());
		// System.out.println(net.getPetriNet().getClass());
		// }
		// set Layout
		// setLayoutOrganic(editor.getGraphComponent().getGraph());

		// try {
		// Document document = EditorUtils.parseXml(new FileInputStream(file));
		// if (document != null) {
		// String name = document.getDocumentElement().getNodeName();
		// if (name.endsWith("definitions")) {
		// BPMNEditorUtils.openBPMN(editor, fc.getSelectedFile());
		// // BPMNEditorUtils.addRecentFiletoList(editor, file);
		// // } else if (name.equals("VisioDocument")) {
		// // BPMNEditorUtils.openVdx(editor, fc.getSelectedFile(), document);
		// // } else if (name.equals("graphml")) {
		// // BPMNEditorUtils.openGraphML(editor, fc.getSelectedFile(),
		// document);
		// } else {
		// JOptionPane
		// .showMessageDialog(
		// editor.getGraphComponent(),
		// "The selected file is not recognized by Yaoqiang BPMN Editor. \nBecause the file format is unknown by the Editor,\nyou should be sure that the file comes from a trustworthy source.",
		// "Unsupported file format", JOptionPane.ERROR_MESSAGE);
		// }
		// }
		// } catch (Exception ex) {
		// editor.progress(false);
		// ex.printStackTrace();
		// JOptionPane.showMessageDialog(editor.getGraphComponent(),
		// ex.getStackTrace(),
		// "Please Capture This Error Screen Shots and Submit this BUG.",
		// JOptionPane.ERROR_MESSAGE);
		// }
		// }
		// petriNet.g
		// petriNet.getNodes().iterator().next().g

		// try {
		// while (netName == null ||
		// SimulationComponents.getInstance().getPetriNet(netName) != null) {
		// netName = JOptionPane.showInputDialog(Simulator.this,
		// "Name for the Petri net:", file.getName().substring(0,
		// file.getName().lastIndexOf(".")));
		// }
		// } catch (ParameterException e1) {
		// JOptionPane.showMessageDialog(Simulator.this,
		// "Cannot check if net name is already in use.\nReason: " +
		// e1.getMessage(), "Internal Exeption", JOptionPane.ERROR_MESSAGE);
		// return;
		// }
		// try {
		// if (!petriNet.getName().equals(netName))
		// petriNet.setName(netName);
		// } catch (ParameterException e2) {
		// JOptionPane.showMessageDialog(Simulator.this,
		// "Cannot change Petri net name to\"" + netName + "\".\nReason: " +
		// e2.getMessage(), "Internal Exeption", JOptionPane.ERROR_MESSAGE);
		// return;
		// }
		//
		// try {
		// SimulationComponents.getInstance().addPetriNet(petriNet);
		// } catch (Exception e1) {
		// JOptionPane.showMessageDialog(Simulator.this,
		// "Cannot add imported net to simulation components.\nReason: " +
		// e1.getMessage(), "Internal Exeption", JOptionPane.ERROR_MESSAGE);
		// return;
		// }

	}

	private void setLayoutOrganic(final mxGraph graph) {
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
//not in use because of missing parser
//	private void visualizeGraph(GraphicalPN n, mxGraph graph) {
//		PTNet pn = (PTNet) n.getPetriNet();
//		PNGraphics pnG = n.getPetriNetGraphics();
//		graph.getModel().beginUpdate();
//		HashMap<AbstractPNNode<?>, Object> createdVertices = new HashMap<AbstractPNNode<?>, Object>();
//		for (PTFlowRelation fr : pn.getFlowRelations()) {
//			System.out.println(fr);
//			// System.out.println(pnG.getPlaceGraphics().size());
//			// pnG.getPlaceGraphics()
//			Object source = null;
//			Object target = null;
//			AbstractPNNode<?> sourceNode = fr.getSource();
//			if (sourceNode instanceof PTPlace) {
//				PTPlace place = (PTPlace) sourceNode;
//				if (!createdVertices.keySet().contains(place)) {
//					NodeGraphics pG = (NodeGraphics) pnG.getPlaceGraphics()
//							.get(place);
//					CellInfo ci = new CellInfo(1);
//					ci.setName(place.getName());
//					source = graph.insertVertex(graph.getDefaultParent(), place
//							.getName(), ci, pG.getPosition().getX(), pG
//							.getPosition().getY(), pG.getDimension().getX(), pG
//							.getDimension().getY(), mxConstants.SHAPE_ELLIPSE);
//					System.out.println(pG);
//					createdVertices.put(place, source);
//				} else
//					source = createdVertices.get(place);
//			}
//			if (sourceNode instanceof PTTransition) {
//				PTTransition transition = (PTTransition) sourceNode;
//				if (!createdVertices.keySet().contains(transition)) {
//					NodeGraphics pT = (NodeGraphics) pnG
//							.getTransitionGraphics().get(transition);
//					CellInfo ci = new CellInfo(0, 0);
//					ci.setName(transition.getName());
//					source = graph.insertVertex(graph.getDefaultParent(),
//							transition.getName(), ci, pT.getPosition().getX(),
//							pT.getPosition().getY(), pT.getDimension().getX(),
//							pT.getDimension().getY(),
//							mxConstants.SHAPE_RECTANGLE);
//					System.out.println(pT);
//					createdVertices.put(transition, source);
//				} else
//					source = createdVertices.get(transition);
//			}
//
//			AbstractPNNode<?> targetNode = fr.getTarget();
//			if (targetNode instanceof PTPlace) {
//				PTPlace place = (PTPlace) targetNode;
//				if (!createdVertices.keySet().contains(place)) {
//					NodeGraphics pG = (NodeGraphics) pnG.getPlaceGraphics()
//							.get(place);
//					CellInfo ci = new CellInfo(1);
//					ci.setName(place.getName());
//					target = graph.insertVertex(graph.getDefaultParent(), place
//							.getName(), ci, pG.getPosition().getX(), pG
//							.getPosition().getY(), pG.getDimension().getX(), pG
//							.getDimension().getY(), mxConstants.SHAPE_ELLIPSE);
//					System.out.println(pG);
//					createdVertices.put(place, target);
//				} else
//					target = createdVertices.get(place);
//			}
//			if (targetNode instanceof PTTransition) {
//				PTTransition transition = (PTTransition) targetNode;
//				if (!createdVertices.keySet().contains(transition)) {
//					// target = graph.insertVertex(null, transition.getName(),
//					// transition.getLabel(), 20, 20, 50, 50);
//
//					NodeGraphics pT = (NodeGraphics) pnG
//							.getTransitionGraphics().get(transition);
//					CellInfo ci = new CellInfo(0, 0);
//					ci.setName(transition.getName());
//					target = graph.insertVertex(graph.getDefaultParent(),
//							transition.getName(), ci, pT.getPosition().getX(),
//							pT.getPosition().getY(), pT.getDimension().getX(),
//							pT.getDimension().getY(),
//							mxConstants.SHAPE_RECTANGLE);
//					System.out.println(pT);
//					createdVertices.put(transition, target);
//				} else
//					target = createdVertices.get(transition);
//
//			}
//
//			// NodeGraphics graphics = pnG.getPlaceGraphics().get(place);
//			// System.out.println(placegraphics);
//			System.out.println(source + "---->" + target);
//			System.out.println(createdVertices.get(sourceNode));
//			graph.insertEdge(null, fr.getName(), fr.getWeight(), source, target);
//			// graph.insertVertex(null, place.getName(), place.getLabel(), 20,
//			// 20, 20, 20);
//		}
//		graph.getModel().endUpdate();
//	}

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
		PopupMenu menu = new PopupMenu(PNMLEditor.this);
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
					PNMLEditor.this.mouseWheelMoved(e);
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
				PNMLEditor.class.getResource(iconUrl)) : null) {

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
					final mxGraph graph = graphComponent.getGraph();
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

			return map;
		}
	}
}
