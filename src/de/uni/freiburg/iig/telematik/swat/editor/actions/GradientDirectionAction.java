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
	public class GradientDirectionAction extends AbstractPNEditorAction {
		private Image diagonal;
		private Image vertical;
		private Image horizontal;
		boolean isDot = false;
		int iterator = 1; 

		public GradientDirectionAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
			super(editor, "gradient_horizontal", IconFactory.getIcon("gradient_horizontal"));
			horizontal = IconFactory.getIcon("gradient_horizontal").getImage();
			vertical = IconFactory.getIcon("gradient_vertical").getImage();
			diagonal = IconFactory.getIcon("gradient-diagonal").getImage();
		}

		public void actionPerformed(ActionEvent e) {
			PNGraph graph = getEditor().getGraphComponent().getGraph();

			Map<String, Object> style = graph.getCellStyle(graph.getSelectionCell());
			double width = mxUtils.getDouble(style, mxConstants.STYLE_STROKEWIDTH);

			iterator = iterator%3;
			System.out.println(iterator);
			if(iterator == 0){
				graph.setCellStyles(mxConstants.STYLE_GRADIENT_DIRECTION, mxConstants.DIRECTION_SOUTH);	
				super.getIcon().setImage(this.horizontal);	
			}
				
				
			if(iterator == 1){
				graph.setCellStyles(mxConstants.STYLE_GRADIENT_DIRECTION, mxConstants.DIRECTION_EAST);	
				super.getIcon().setImage(this.vertical);	
				}

				
			if(iterator == 2){
				graph.setCellStyles(mxConstants.STYLE_GRADIENT_DIRECTION, "south_east");	
				super.getIcon().setImage(this.diagonal);	
				}
			iterator++;
			
			}

}