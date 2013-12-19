package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;

import com.mxgraph.swing.handler.mxGraphTransferHandler;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;

import de.invation.code.toval.validate.ParameterException;

public class GraphTransferHandler extends mxGraphTransferHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8521142091923475876L;

	@Override
	/**
	 * Checks if the mxmxGraphTransferable data flavour is supported and calls
	 * importmxGraphTransferable if possible.
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

					if (graphComponent.isEnabled() && t.isDataFlavorSupported(mxGraphTransferable.dataFlavor)) {
						mxGraphTransferable gt = (mxGraphTransferable) t.getTransferData(mxGraphTransferable.dataFlavor);

						if (gt.getCells() != null) {
							result = importmxGraphTransferable(graphComponent, gt);
						}

					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * Returns true if the cells have been imported using importCells.
	 */
	protected boolean importmxGraphTransferable(PNGraphComponent graphComponent, mxGraphTransferable gt) {
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
	protected Object[] importCells(PNGraphComponent graphComponent, mxGraphTransferable gt, double dx, double dy) {
		Object target = getDropTarget(graphComponent, gt);
		PNGraph graph = graphComponent.getGraph();
		Object[] cells = gt.getCells();

		for (Object object : cells) {
			PNGraphCell cell;
			if (object instanceof PNGraphCell) {
				cell = (PNGraphCell) object;
				switch (cell.getType()) {
				case PLACE:
					try {
						cells = new Object[] { graph.addNewPlace(new mxPoint(dx, dy)) };
					} catch (ParameterException e) {
						e.printStackTrace();
					}
					break;
				case TRANSITION:
					try {
						cells = new Object[] { graph.addNewTransition(new mxPoint(dx, dy)) };
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

}
