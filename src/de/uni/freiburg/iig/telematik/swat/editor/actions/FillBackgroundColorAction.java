package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JColorChooser;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;

public class FillBackgroundColorAction extends AbstractPNEditorAction{
	public FillBackgroundColorAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "BackgroundColor", IconFactory.getIcon("bg_color"));
	}

	public void actionPerformed(ActionEvent e) {
		PNGraph graph = getEditor().getGraphComponent().getGraph();

		if (!graph.isSelectionEmpty()) {
			Color newColor = ColorChooser.showDialog(getEditor().getGraphComponent(), "Background Color", null);

			if (newColor != null) {
				mxCell selectedCell = (mxCell) graph.getSelectionCell();
				if (graph.isLabelSelected()) {
					graph.setCellStyles(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, mxUtils.hexString(newColor));
				} else {
					graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, mxUtils.hexString(newColor));
				}

			}
			else {
				mxCell selectedCell = (mxCell) graph.getSelectionCell();
				if (graph.isLabelSelected()) {
					graph.setCellStyles(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "none");
				} else {
					graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "none");
				}
			}
		}
	}
}


