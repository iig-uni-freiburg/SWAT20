package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.TooManyListenersException;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import org.w3c.dom.Document;

import com.mxgraph.io.mxCodec;
import com.mxgraph.shape.mxActorShape;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.swing.handler.mxCellHandler;
import com.mxgraph.swing.handler.mxEdgeHandler;
import com.mxgraph.swing.handler.mxElbowEdgeHandler;
import com.mxgraph.swing.handler.mxGraphTransferHandler;
import com.mxgraph.swing.handler.mxVertexHandler;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxEdgeStyle.mxEdgeStyleFunction;

import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.menu.EditorPopupMenu;
import de.uni.freiburg.iig.telematik.swat.editor.menu.GraphTransferHandler;
import de.uni.freiburg.iig.telematik.swat.editor.menu.PaletteIcon;
import de.uni.freiburg.iig.telematik.swat.editor.menu.PaletteIconDataFlavor;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;

public abstract class PNGraphComponent extends mxGraphComponent {

	private static final long serialVersionUID = 1411737962538427287L;
	
	private EditorPopupMenu popupMenu = null;

	public PNGraphComponent(PNGraph graph, PNEditor pnEditor) {
		super(graph);
		initialize(pnEditor);
	}
	
	@Override
	public PNGraph getGraph() {
		return (PNGraph) super.getGraph();
	}
	
	@Override
	/**
	 * 
	 */
	protected TransferHandler createTransferHandler()
	{
		return new GraphTransferHandler();
	}
	
	@Override
	/**
	 * 
	 * @param state
	 *            Cell state for which a handler should be created.
	 * @return Returns the handler to be used for the given cell state.
	 */
	public mxCellHandler createHandler(mxCellState state)
	{
		if (graph.getModel().isVertex(state.getCell()))
		{
			if(state.getCell() instanceof PNGraphCell){
			PNGraphCell cell = (PNGraphCell) state.getCell();
			switch(cell.getType()){
			case PLACE:
				return	new VertexHandler(this, state);
			case TRANSITION:
			return	new mxVertexHandler(this, state);
			default:
				break;
			
			}
			}
		}
		else if (graph.getModel().isEdge(state.getCell()))
		{
			mxEdgeStyleFunction style = graph.getView().getEdgeStyle(state,
					null, null, null);

			if (graph.isLoop(state) || style == mxEdgeStyle.ElbowConnector
					|| style == mxEdgeStyle.SideToSide
					|| style == mxEdgeStyle.TopToBottom)
			{
				return new mxElbowEdgeHandler(this, state);
			}

			return new mxEdgeHandler(this, state);
		}

		return new mxCellHandler(this, state);
	}

	private void initialize(PNEditor pnEditor){
		setGridStyle(mxGraphComponent.GRID_STYLE_LINE);
		setGridColor(MXConstants.bluehigh);
		setBackground(MXConstants.blueBG);
		setGridVisible(true);
		getGraphControl().addMouseListener(new GCMouseAdapter());
		getConnectionHandler().setCreateTarget(true);
		mxCodec codec = new mxCodec();
		Document doc = mxUtils.loadDocument(PNEditor.class.getResource("/default-style.xml").toString());
		codec.decode(doc.getDocumentElement(), graph.getStylesheet());
		
	}

	@Override
	protected ConnectionHandler createConnectionHandler() {
		return new ConnectionHandler(this);
	}

	public void setPopupMenu(EditorPopupMenu popupMenu){
		this.popupMenu = popupMenu;
	}
	
//	@SuppressWarnings("rawtypes") 
//	public void addExistingPlaceToGraph(AbstractPlace place, NodeGraphics nodeGraphics){
//		getGraph().insertPNPlace(place, nodeGraphics);
//	}
//	
//	@SuppressWarnings("rawtypes") 
//	public void addExistingTransitionToGraph(AbstractTransition transition, NodeGraphics nodeGraphics){
//		getGraph().insertPNTransition(transition, nodeGraphics);
//	}
//	
//	@SuppressWarnings("rawtypes")
//	public void addExistingRelation(AbstractFlowRelation relation, ArcGraphics arcGraphics){
//		getGraph().insertPNRelation(relation, getArcConstraint(relation), arcGraphics);
//	}
//	
	

	
	
	//------- MouseListener support ------------------------------------------------------------------

	protected boolean rightClickOnCanvas(MouseEvent e){
		Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), this);
		popupMenu.show(PNGraphComponent.this, pt.x, pt.y);
		return false;
	}
	
	protected boolean rightClickOnPlace(PNGraphCell cell, MouseEvent e){
		return false;
	}
	
	protected boolean rightClickOnTransition(PNGraphCell cell, MouseEvent e){
		return false;
	}
	
	protected boolean rightClickOnArc(PNGraphCell cell, MouseEvent e){
		return false;
	}
	
	protected boolean doubleClickOnCanvas(MouseEvent e){
		return false;
	}
	
	protected boolean doubleClickOnPlace(PNGraphCell cell, MouseEvent e){
		return false;
	}
	
	protected boolean doubleClickOnTransition(PNGraphCell cell, MouseEvent e){
		return false;
	}
	
	protected boolean doubleClickOnArc(PNGraphCell cell, MouseEvent e){
		return false;
	}
	
	private class GCMouseAdapter extends MouseAdapter {
		
		

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.getSource() instanceof mxGraphOutline || e.isControlDown()) {
				if (e.getWheelRotation() < 0) {
					zoomIn();
				} else {
					zoomOut();
				}
//				displayStatusMessage(String.format(scaleMessageFormat, (int) (100 * getGraph().getView().getScale())));
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			
			Object object = getCellAt(e.getX(), e.getY());
			PNGraphCell cell = null;
			if(object != null){
				cell = (PNGraphCell) object;
			}
			boolean refresh = false;
			
			if(e.getClickCount()==1){
				if(e.isPopupTrigger()){
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
				}
			} else if(e.getClickCount() == 2 && !e.isPopupTrigger()){
				// Double click on graph component.
				if(object == null){
					refresh = doubleClickOnCanvas(e);
				} else {
					switch(cell.getType()){
					case PLACE:
						refresh = doubleClickOnPlace(cell,e);
						break;
					case TRANSITION:
						refresh = doubleClickOnTransition(cell,e);
						break;
					case ARC:
						refresh = doubleClickOnArc(cell,e);
						break;
					}
				}
			}
			if(refresh){
				mxCellState state = getGraph().getView().getState(cell);
				redraw(state);
			}
		}
		
	}
	
	public class PaletteTransferHandler extends TransferHandler {



	
		private static final long serialVersionUID = -6764630859491349189L;
		
		
		public boolean canImport(TransferSupport support) {
	        if (!support.isDrop()) {
	            return false;
	        }
	        return support.isDataFlavorSupported(new PaletteIconDataFlavor());
	    }

	    public boolean importData(TransferSupport support) {
	    	
	        if (!canImport(support)) {
	          return false;
	        }

	        Transferable transferable = support.getTransferable();
	        PaletteIcon icon;
	        try {
	        	icon = (PaletteIcon) transferable.getTransferData(new PaletteIconDataFlavor());
	        } catch (Exception e) {
	        	return false;
	        }
	        
	        if(icon.getType() == PNComponent.PLACE){
//	        	addNewPlace(support.getDropLocation().getDropPoint());
	        } else if(icon.getType() == PNComponent.TRANSITION){
//	        	addNewTransition(support.getDropLocation().getDropPoint());
	        }
	        
	        return true;
	    }
		
	}
	
}
