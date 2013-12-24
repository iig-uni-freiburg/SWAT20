package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JColorChooser;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;

/**
*
*/
@SuppressWarnings("serial")
public class ColorAction extends AbstractAction {
	/**
	 * 
	 */
	protected String name, key;

	/**
	 * 
	 * @param key
	 */
	public ColorAction(String name, String key) {
		this.name = name;
		this.key = key;
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof mxGraphComponent) {
			mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
			PNGraph graph = (PNGraph) graphComponent.getGraph();

			if (!graph.isSelectionEmpty()) {
				Color newColor = JColorChooser.showDialog(graphComponent, name, null);

				if (newColor != null) {
					mxCell selectedCell = (mxCell) graph.getSelectionCell();
					mxCellState selectedCellState = graph.getView().getState(selectedCell);
					if(graph.isLabelSelected()){
						graph.getModel().beginUpdate();
						selectedCellState.getStyle().put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, mxUtils.hexString(newColor));
						graph.getModel().endUpdate();
					} else {
						graph.setCellStyles(key, mxUtils.hexString(newColor));
					}
					try {
						graph.setPNGraphics(selectedCellState);
					} catch (ParameterException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}
}