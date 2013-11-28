package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;

import com.mxgraph.swing.handler.mxGraphTransferHandler;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphComponent;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;

public class GraphTransferHandler extends mxGraphTransferHandler {

	@Override
	/**
	 * Checks if the mxGraphTransferable data flavour is supported and calls
	 * importGraphTransferable if possible.
	 */
	public boolean importData(JComponent c, Transferable t) {
		boolean result = false;

		if (isLocalDrag()) {
			// Enables visual feedback on the Mac
			result = true;
		} else {
			try {
				updateImportCount(t);

				if (c instanceof PNGraphComponent) {
					PNGraphComponent graphComponent = (PNGraphComponent) c;

					if (graphComponent.isEnabled() && t.isDataFlavorSupported(GraphTransferable.dataFlavor)) {
						GraphTransferable gt = (GraphTransferable) t.getTransferData(GraphTransferable.dataFlavor);

						if (gt.getCells() != null) {
							result = importGraphTransferable(graphComponent, gt);
						}

					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return result;
	}

	public GraphTransferHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Returns true if the cells have been imported using importCells.
	 */
	protected boolean importGraphTransferable(PNGraphComponent graphComponent, GraphTransferable gt) {
		boolean result = false;

		try {
			PNGraph graph = graphComponent.getGraph();
			double scale = graph.getView().getScale();
			mxRectangle bounds = gt.getBounds();
			double dx = 0, dy = 0;

			// Computes the offset for the placement of the imported cells
			if (location != null && bounds != null) {
				mxPoint translate = graph.getView().getTranslate();

				dx = location.getX() - (bounds.getX() + translate.getX()) * scale;
				dy = location.getY() - (bounds.getY() + translate.getY()) * scale;

				// Keeps the cells aligned to the grid
				dx = graph.snap(dx / scale);
				dy = graph.snap(dy / scale);
			} else {
				int gs = graph.getGridSize();

				dx = importCount * gs;
				dy = importCount * gs;
			}

			if (offset != null) {
				dx += offset.x;
				dy += offset.y;
			}

			importCells(graphComponent, gt, dx, dy);
			location = null;
			offset = null;
			result = true;

			// Requests the focus after an import
			graphComponent.requestFocus();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Gets a drop target using getDropTarget and imports the cells using
	 * mxGraph.splitEdge or PNGraphComponent.importCells depending on the drop
	 * target and the return values of mxGraph.isSplitEnabled and
	 * mxGraph.isSplitTarget. Selects and returns the cells that have been
	 * imported.
	 */
	protected Object[] importCells(PNGraphComponent graphComponent, GraphTransferable gt, double dx, double dy) {
		Object target = getDropTarget(graphComponent, gt);
		PNGraph graph = graphComponent.getGraph();
		Object[] cells = gt.getCells();

		for (Object object : cells) {
			PNGraphCell cell;
			if (object instanceof PNGraphCell) {
				cell = (PNGraphCell) object;
				switch (cell.getType()) {
				case PLACE:
					System.out.println("PLACE" + dx + "#" + dy);
					try {
						cells = createTargetPlace(graph, dx, dy);
					} catch (ParameterException e) {
						e.printStackTrace();
					}
					break;
				case TRANSITION:
					try {
						cells = createTargetPlace(graph, dx, dy);
					} catch (ParameterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		}

		// cells = graphComponent.getImportableCells(cells);

		if (graph.isSplitEnabled() && graph.isSplitTarget(target, cells)) {
			graph.splitEdge(target, cells, dx, dy);
		} else {
			// cells = graphComponent.importCells(cells, 0, 0, target,
			// location);
			graph.setSelectionCells(cells);
		}

		return cells;
	}

	private Object[] createTargetPlace(PNGraph graph, double dx, double dy) throws ParameterException {
		String prefix = MXConstants.PlaceNamePrefix;
		PNGraphCell newCell = null;
		Integer index = 0;
		while (graph.getNetContainer().getPetriNet().containsPlace(prefix + index)) {
			index++;
		}
		String nodeName = prefix + index;
		if (graph.getNetContainer().getPetriNet().addPlace(nodeName)) {
			AbstractPlace place = graph.getNetContainer().getPetriNet().getPlace(nodeName);
			newCell = graph.createPlaceCell(place.getName(), place.getLabel(), dx, dy, EditorProperties.getInstance().getDefaultPlaceSize(), EditorProperties.getInstance().getDefaultPlaceSize(),
					MXConstants.getStyle(PNComponent.PLACE, null, null));
			graph.addNodeReference(place, newCell);
		}

		setGraphicsOfNewCell(dx, dy, newCell, graph);
		graph.addCell(newCell, graph.getDefaultParent());
		newCell.setVertex(true);
		return new Object[] { newCell };
	}

	private Object createTargetTransition(PNGraph graph, double dx, double dy) throws ParameterException {
		String prefix = MXConstants.TransitionNamePrefix;
		PNGraphCell newCell = null;
		Integer index = 0;
		while (graph.getNetContainer().getPetriNet().containsTransition(prefix + index)) {
			index++;
		}
		String nodeName = prefix + index;
		if (graph.getNetContainer().getPetriNet().addTransition(nodeName)) {
			AbstractTransition transition = graph.getNetContainer().getPetriNet().getTransition(nodeName);
			newCell = graph.createTransitionCell(transition.getName(), transition.getLabel(), dx, dy, EditorProperties.getInstance().getDefaultTransitionWidth(), EditorProperties.getInstance()
					.getDefaultTransitionHeight(), MXConstants.getStyle(PNComponent.TRANSITION, null, null));
			graph.addNodeReference(transition, newCell);

		}
		setGraphicsOfNewCell(dx, dy, newCell, graph);
		graph.addCell(newCell, graph.getDefaultParent());
		newCell.setVertex(true);
		return newCell;
	}

	/**
	 * @param point
	 * @param dy
	 * @param newCell
	 * @param graph
	 * @throws ParameterException
	 */
	protected void setGraphicsOfNewCell(double dx, double dy, PNGraphCell newCell, PNGraph graph) throws ParameterException {
		mxCellState state = graph.getView().getState(newCell, true);
		state.setX(dx);
		state.setY(dy);
		state.setWidth(EditorProperties.getInstance().getDefaultTransitionWidth());
		state.setHeight(EditorProperties.getInstance().getDefaultTransitionHeight());
		graph.setGraphics(state);
	}

}
