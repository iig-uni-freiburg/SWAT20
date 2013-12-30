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
public class LineStyleAction extends AbstractPNEditorAction {
	private Image dot;
	private Image dash;
	private Image line;
	boolean isDot = false;
	int iterator = 1; 

	public LineStyleAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Line", IconFactory.getIcon("solid"));
		line = IconFactory.getIcon("solid").getImage();
		dash = IconFactory.getIcon("dash").getImage();
		dot = IconFactory.getIcon("dot").getImage();
	}

	public void actionPerformed(ActionEvent e) {
		PNGraph graph = getEditor().getGraphComponent().getGraph();

		Map<String, Object> style = graph.getCellStyle(graph.getSelectionCell());
		double width = mxUtils.getDouble(style, mxConstants.STYLE_STROKEWIDTH);

		iterator = iterator%3;
		System.out.println(iterator);
		if(iterator == 0){
			graph.setCellStyles(mxConstants.STYLE_DASHED, "0");	
			super.getIcon().setImage(this.line);	
		}
			
			
		if(iterator == 1){
			graph.setCellStyles(mxConstants.STYLE_DASHED, "1");
			graph.setCellStyles(mxConstants.STYLE_DASH_PATTERN, width*4 + " " + width*2);
			super.getIcon().setImage(this.dash);	
			}

			
		if(iterator == 2){
			graph.setCellStyles(mxConstants.STYLE_DASHED, "1");
			graph.setCellStyles(mxConstants.STYLE_DASH_PATTERN, width +" "+ width);
			super.getIcon().setImage(this.dot);	
			}
		iterator++;
		
		}

}