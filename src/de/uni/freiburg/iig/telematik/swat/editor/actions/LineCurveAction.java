package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Map;

import javax.swing.JColorChooser;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;

@SuppressWarnings("serial")
public class LineCurveAction extends AbstractPNEditorAction {
	private Image curve;
	private Image line;
	int iterator = 1; 

	public LineCurveAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "LineCurve", IconFactory.getIcon("line"));
		line = IconFactory.getIcon("line").getImage();
		curve = IconFactory.getIcon("round").getImage();
	}

	public void actionPerformed(ActionEvent e) {
		PNGraph graph = getEditor().getGraphComponent().getGraph();

		Map<String, Object> style = graph.getCellStyle(graph.getSelectionCell());
		double width = mxUtils.getDouble(style, mxConstants.STYLE_STROKEWIDTH);

		iterator = iterator%2;
		System.out.println(iterator);
		if(iterator == 0){
			graph.setCellStyles(mxConstants.STYLE_ROUNDED,"true");
			graph.setCellStyles(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);
			super.getIcon().setImage(this.line);	
		}
			
			
		if(iterator == 1){
			graph.setCellStyles(mxConstants.STYLE_ROUNDED,"false");
			graph.setCellStyles(mxConstants.STYLE_EDGE,"direct");
			super.getIcon().setImage(this.curve);	
			}

			
		iterator++;
		
		}

}