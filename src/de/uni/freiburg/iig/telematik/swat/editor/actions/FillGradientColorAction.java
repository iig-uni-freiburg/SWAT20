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
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;

public class FillGradientColorAction extends AbstractPNEditorAction{
	public FillGradientColorAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "BackgroundColor", IconFactory.getIcon("bg_color"));
	}

	public void actionPerformed(ActionEvent e) {
		PNGraph graph = getEditor().getGraphComponent().getGraph();

		if (!graph.isSelectionEmpty()) {
			Color newColor = JColorChooser.showDialog(getEditor().getGraphComponent(), "Background Color", null);

			if (newColor != null) {
				if (graph.isLabelSelected()) {
					graph.setCellStyles(MXConstants.LABEL_GRADIENTCOLOR, mxUtils.hexString(newColor));

				} else {
					graph.setCellStyles(mxConstants.STYLE_GRADIENTCOLOR, mxUtils.hexString(newColor));
				}

			}
		}
	}
}


