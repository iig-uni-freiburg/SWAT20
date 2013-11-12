package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import org.w3c.dom.Document;

import com.mxgraph.io.mxCodec;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.util.mxUtils;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPNNode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;
import de.unifreiburg.iig.bpworkbench2.editor.EditorPopupMenu;
import de.unifreiburg.iig.bpworkbench2.editor.PNEditor;
import de.unifreiburg.iig.bpworkbench2.editor.PaletteIcon;
import de.unifreiburg.iig.bpworkbench2.editor.PaletteIconDataFlavor;

public abstract class PNGraphComponent extends mxGraphComponent {

	private static final long serialVersionUID = 1411737962538427287L;
	
	private EditorPopupMenu popupMenu = null;
	
	@SuppressWarnings("rawtypes")
	protected Map<AbstractPNNode, PNGraphCell> nodeReferences = new HashMap<AbstractPNNode, PNGraphCell>();

	public PNGraphComponent(PNGraph graph, PNEditor pnEditor) {
		super(graph);
		initialize(pnEditor);
	}
	
	@Override
	public PNGraph getGraph() {
		return (PNGraph) super.getGraph();
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
		setTransferHandler(new PaletteTransferHandler());
		this.popupMenu = pnEditor.getPopupMenu();
	}

	@Override
	protected ConnectionHandler createConnectionHandler() {
		return new ConnectionHandler(this);
	}

	public void setPopupMenu(EditorPopupMenu popupMenu){
		this.popupMenu = popupMenu;
	}
	
	@SuppressWarnings("rawtypes") 
	public void addExistingPlaceToGraph(AbstractPlace place, NodeGraphics nodeGraphics){
		PNGraphCell newCell = getGraph().insertPNPlace(place.getName(), place.getLabel(), nodeGraphics);
		nodeReferences.put(place, newCell);
		//TODO: Notify listeners
	}
	
	@SuppressWarnings("rawtypes") 
	public void addExistingTransitionToGraph(AbstractTransition transition, NodeGraphics nodeGraphics){
		PNGraphCell newCell = getGraph().insertPNTransition(transition.getName(), transition.getLabel(), nodeGraphics);
		nodeReferences.put(transition, newCell);
		//TODO: Notify listeners
	}
	
	@SuppressWarnings("rawtypes")
	public void addExistingRelation(AbstractFlowRelation relation, ArcGraphics arcGraphics){
		getGraph().insertPNRelation(relation.getName(), getArcConstraint(relation), nodeReferences.get(relation.getSource()), nodeReferences.get(relation.getTarget()), arcGraphics);
		//TODO: Notify listeners
	}
	
	@SuppressWarnings("rawtypes")
	protected abstract String getArcConstraint(AbstractFlowRelation relation);
	
	
	@SuppressWarnings("rawtypes")
	public void setLabelPositions(AbstractPNGraphics pnGraphics){
		
	}
	
	protected void addNewPlace(Point point){
//		String prefix = MXConstants.PlaceNamePrefix;
//		Integer index = 0;
//		while(getGraph().getNetContainer().getPetriNet().containsPlace(prefix+index)){
//			index++;
//		}
//		String nodeName = prefix+index;
//		
//		if(getGraph().getNetContainer().getPetriNet().addPlace(nodeName)){
//			AbstractPlace newPlace = 
//			nodeReferences.put(key, value);
//			
//		}
	}
	
	protected void addNewTransition(Point point){
		
	}
	
	
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
				refresh();
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
	        	addNewPlace(support.getDropLocation().getDropPoint());
	        } else if(icon.getType() == PNComponent.TRANSITION){
	        	addNewTransition(support.getDropLocation().getDropPoint());
	        }
	        
	        return true;
	    }
		
	}
	
}
