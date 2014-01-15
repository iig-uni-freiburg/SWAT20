package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import org.w3c.dom.Document;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.io.mxCodec;
import com.mxgraph.shape.mxConnectorShape;
import com.mxgraph.shape.mxDefaultTextShape;
import com.mxgraph.shape.mxEllipseShape;
import com.mxgraph.shape.mxHtmlTextShape;
import com.mxgraph.shape.mxRectangleShape;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.handler.ConnectionHandler;
import de.uni.freiburg.iig.telematik.swat.editor.graph.handler.GraphTransferHandler;
import de.uni.freiburg.iig.telematik.swat.editor.graph.shape.ConnectorShape;
import de.uni.freiburg.iig.telematik.swat.editor.graph.shape.DefaultTextShape;
import de.uni.freiburg.iig.telematik.swat.editor.graph.shape.EllipseShape;
import de.uni.freiburg.iig.telematik.swat.editor.graph.shape.HtmlTextShape;
import de.uni.freiburg.iig.telematik.swat.editor.graph.shape.RectangleShape;
import de.uni.freiburg.iig.telematik.swat.editor.menu.EditorPopupMenu;

public abstract class PNGraphComponent extends mxGraphComponent {

	private static final long serialVersionUID = 1411737962538427287L;

	private EditorPopupMenu popupMenu = null;

	public PNGraphComponent(PNGraph graph) {
		super(graph);
		initialize();
		getCanvas().putShape(mxConstants.SHAPE_RECTANGLE, new RectangleShape());
		getCanvas().putShape(mxConstants.SHAPE_ELLIPSE, new EllipseShape());
		getCanvas().putTextShape(mxGraphics2DCanvas.TEXT_SHAPE_DEFAULT, new DefaultTextShape());
		getCanvas().putTextShape(mxGraphics2DCanvas.TEXT_SHAPE_HTML, new HtmlTextShape());
		getCanvas().putShape(mxConstants.SHAPE_CONNECTOR, new ConnectorShape());

	}

	@Override
	public PNGraph getGraph() {
		return (PNGraph) super.getGraph();
	}

	@Override
	/**
	 * 
	 */
	protected TransferHandler createTransferHandler() {
		return new GraphTransferHandler();
	}

//	@Override
//	/**
//	 * 
//	 * @param state
//	 *            Cell state for which a handler should be created.
//	 * @return Returns the handler to be used for the given cell state.
//	 */
//	public mxCellHandler createHandler(mxCellState state) {
//		if (graph.getModel().isVertex(state.getCell())) {
//			if (state.getCell() instanceof PNGraphCell) {
//				PNGraphCell cell = (PNGraphCell) state.getCell();
//				switch (cell.getType()) {
//				case PLACE:
//					return new PlaceVertexHandler(this, state);
//				case TRANSITION:
//					return new mxVertexHandler(this, state);
//				default:
//					break;
//
//				}
//			}
//		} else if (graph.getModel().isEdge(state.getCell())) {
//			mxEdgeStyleFunction style = graph.getView().getEdgeStyle(state, null, null, null);
//
//			if (graph.isLoop(state) || style == mxEdgeStyle.ElbowConnector || style == mxEdgeStyle.SideToSide || style == mxEdgeStyle.TopToBottom) {
//				return new mxElbowEdgeHandler(this, state);
//			}
//
//			return new mxEdgeHandler(this, state);
//		}
//
//		return new mxCellHandler(this, state);
//	}

	private void initialize() {
		setGridStyle(mxGraphComponent.GRID_STYLE_LINE);
		setGridColor(MXConstants.bluehigh);
		setBackground(MXConstants.blueBG);
		setGridVisible(true);
		setToolTips(true);
		getGraphControl().addMouseListener(new GCMouseAdapter());
		addMouseWheelListener(new GCMouseWheelListener());
//		addKeyListener(new GCKeyListener());
		getConnectionHandler().setCreateTarget(true);
		mxCodec codec = new mxCodec();
		Document doc = mxUtils.loadDocument(PNEditor.class.getResource("/default-style.xml").toString());
		codec.decode(doc.getDocumentElement(), graph.getStylesheet());
	}

	@Override
	protected ConnectionHandler createConnectionHandler() {
		return new ConnectionHandler(this);
	}
	

	public void setPopupMenu(EditorPopupMenu popupMenu) {
		this.popupMenu = popupMenu;
	}

	// ------- MouseListener support
	// ------------------------------------------------------------------

	protected boolean rightClickOnCanvas(MouseEvent e) {
		Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), this);
		popupMenu.show(PNGraphComponent.this, pt.x, pt.y);
		return false;
	}

	protected boolean rightClickOnPlace(PNGraphCell cell, MouseEvent e) {
		return false;
	}

	protected boolean rightClickOnTransition(PNGraphCell cell, MouseEvent e) {
		return false;
	}

	protected boolean rightClickOnArc(PNGraphCell cell, MouseEvent e) {
		return false;
	}

	protected boolean doubleClickOnCanvas(MouseEvent e) {
		return false;
	}

	protected boolean mouseWheelOnCanvas(MouseEvent e) {
		return false;
	}

	protected boolean doubleClickOnPlace(PNGraphCell cell, MouseEvent e) {
		return false;
	}

	protected boolean mouseWheelOnPlace(PNGraphCell cell, MouseWheelEvent e) {
		return false;
	}

	protected boolean doubleClickOnTransition(PNGraphCell cell, MouseEvent e) {
		return false;
	}

	protected boolean doubleClickOnArc(PNGraphCell cell, MouseEvent e) {
		return false;
	}

	private class GCMouseWheelListener implements MouseWheelListener {

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			Object object = getGraph().getSelectionCell();
			PNGraphCell cell = null;
			if (object != null) {
				cell = (PNGraphCell) object;
			}
			boolean refresh = false;

			// Double click on graph component.
			if (object == null) {
				refresh = mouseWheelOnCanvas(e);
			} else {
				switch (cell.getType()) {
				case PLACE:
					refresh = mouseWheelOnPlace(cell, e);
					break;
				}
			}

			if (refresh) {
				mxCellState state = getGraph().getView().getState(cell);
				redraw(state);
			}

		}

	}
	
	private class GCKeyListener extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			
		}

		@Override
		public void keyReleased(KeyEvent e) {

		}

		@Override
		public void keyTyped(KeyEvent event) {
			System.out.println("blub");
			int dx = 0;
			int dy = 0;
		    if (event.getKeyChar() == KeyEvent.VK_LEFT) {
		        dx--;
		    }
		    if (event.getKeyChar() == KeyEvent.VK_DOWN) {
		        dy++;
		    }
		    if (event.getKeyChar() == KeyEvent.VK_RIGHT) {
		        dx++;
		    }
		    if (event.getKeyChar() == KeyEvent.VK_UP) {
		        dy--;
		    }
		    graph.moveCells(graph.getSelectionCells(), dx, dy);
		}


	}

	private class GCMouseAdapter extends MouseAdapter {

		@Override
		/**
		 * 
		 */
		public void mousePressed(MouseEvent e) {

			// Handles context menu on the Mac where the trigger is on mousepressed
//			mouseClicked(e.getModifiers());

		}

		@Override
		/**
		 * 
		 */
		public void mouseReleased(MouseEvent e) {
			// Handles context menu on Windows where the trigger is on mousereleased
			//TODO also working on Linux?
//			mouseClicked(e);
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.getSource() instanceof mxGraphOutline || e.isControlDown()) {
				if (e.getWheelRotation() < 0) {
					zoomIn();
				} else {
					zoomOut();
				}
				// displayStatusMessage(String.format(scaleMessageFormat, (int)
				// (100 * getGraph().getView().getScale())));
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			Object object = getCellAt(e.getX(), e.getY());
			PNGraphCell cell = null;
			if (object != null) {
				cell = (PNGraphCell) object;
			}
			boolean refresh = false;
			if (e.getClickCount() == 1) {
				if (e.getModifiers() == 4) {
					// Right click on graph component.
					if (object == null) {
						refresh = rightClickOnCanvas(e);
					} else {
						switch (cell.getType()) {
						case PLACE:
							refresh = rightClickOnPlace(cell, e);
							break;
						case TRANSITION:
							refresh = rightClickOnTransition(cell, e);
							break;
						case ARC:
							refresh = rightClickOnArc(cell, e);
							break;
						}
					}
				} else {
					// Left click on graph component.
					mxCellState cellState = getGraph().getView().getState(cell);
					if(cellState != null){
						getGraph().setLabelSelected(cellState.getLabelBounds().contains(e.getX(), e.getY()));
					} else {
						getGraph().setLabelSelected(false);
					}
					getGraph().invoke(PNGraphComponent.this, new mxEventObject(mxEvent.CHANGE));
				}
			} else if (e.getClickCount() == 2 && !(e.getModifiers() == 4)) {
				// Double click on graph component.
				if (object == null) {
					refresh = doubleClickOnCanvas(e);
				} else {
					switch (cell.getType()) {
					case PLACE:
						refresh = doubleClickOnPlace(cell, e);
						break;
					case TRANSITION:
						refresh = doubleClickOnTransition(cell, e);
						break;
					case ARC:
						refresh = doubleClickOnArc(cell, e);
						break;
					}
				}
			}
			if (refresh) {
				mxCellState state = getGraph().getView().getState(cell);
				redraw(state);
			}
		}

	}

	// public class PaletteTransferHandler extends TransferHandler {
	//
	// private static final long serialVersionUID = -6764630859491349189L;
	//
	// public boolean canImport(TransferSupport support) {
	// if (!support.isDrop()) {
	// return false;
	// }
	// return support.isDataFlavorSupported(new
	// NotInUsePaletteIconDataFlavor());
	// }
	//
	// public boolean importData(TransferSupport support) {
	//
	// if (!canImport(support)) {
	// return false;
	// }
	//
	// Transferable transferable = support.getTransferable();
	// NotInUsePaletteIcon icon;
	// try {
	// icon = (NotInUsePaletteIcon) transferable.getTransferData(new
	// NotInUsePaletteIconDataFlavor());
	// } catch (Exception e) {
	// return false;
	// }
	//
	// if (icon.getType() == PNComponent.PLACE) {
	// // addNewPlace(support.getDropLocation().getDropPoint());
	// } else if (icon.getType() == PNComponent.TRANSITION) {
	// // addNewTransition(support.getDropLocation().getDropPoint());
	// }
	//
	// return true;
	// }
	//
	// }

}
