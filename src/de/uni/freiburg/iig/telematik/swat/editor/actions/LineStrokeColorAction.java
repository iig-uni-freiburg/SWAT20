package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;

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

@SuppressWarnings("serial")
public class LineStrokeColorAction extends AbstractPNEditorAction {
	public LineStrokeColorAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "StokeColor", IconFactory.getIcon("border_color"));
	}

	public void actionPerformed(ActionEvent e) {
		PNGraph graph = getEditor().getGraphComponent().getGraph();

		if (!graph.isSelectionEmpty()) {
			Color newColor = JColorChooser.showDialog(getEditor().getGraphComponent(), "Stroke Color", null);
			if (newColor != null) {
				if (graph.isLabelSelected()) {
					graph.setCellStyles(mxConstants.STYLE_LABEL_BORDERCOLOR, mxUtils.hexString(newColor));
				} else {
					graph.setCellStyles(mxConstants.STYLE_STROKECOLOR, mxUtils.hexString(newColor));
				}

			}
		}
		//TODO: None Border Color not viewed correctly
		else{
			if (graph.isLabelSelected()) {
				graph.setCellStyles(mxConstants.STYLE_LABEL_BORDERCOLOR, "none");
			} else {
				graph.setCellStyles(mxConstants.STYLE_STROKECOLOR, "none");
			}
		}
	}

}