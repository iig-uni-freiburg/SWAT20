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
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;



	@SuppressWarnings("serial")
	public class FillGradientDirectionAction extends AbstractPNEditorAction {
		private Image diagonal;
		private Image vertical;
		private Image horizontal;
		boolean isDot = false;
		int iterator = 1;
		private Image gradientno; 

		public FillGradientDirectionAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
			super(editor, "gradient_horizontal", IconFactory.getIcon("gradient_horizontal"));
			horizontal = IconFactory.getIcon("gradient_horizontal").getImage();
			vertical = IconFactory.getIcon("gradient_vertical").getImage();
			diagonal = IconFactory.getIcon("gradient-diagonal").getImage();
			gradientno = IconFactory.getIcon("gradient_no").getImage();
		}

		public void actionPerformed(ActionEvent e) {
			PNGraph graph = getEditor().getGraphComponent().getGraph();

			Map<String, Object> style = graph.getCellStyle(graph.getSelectionCell());
			double width = mxUtils.getDouble(style, mxConstants.STYLE_STROKEWIDTH);

			iterator = iterator%4;
			if(iterator == 0){
				if(graph.isLabelSelected())
				graph.setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, GradientRotation.HORIZONTAL.toString());
				else
				graph.setCellStyles(MXConstants.GRADIENT_ROTATION, GradientRotation.HORIZONTAL.toString());	
				super.getIcon().setImage(this.horizontal);	
			}
	
			if(iterator == 1){
				if(graph.isLabelSelected())
					graph.setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, GradientRotation.VERTICAL.toString());	
					else
				graph.setCellStyles(MXConstants.GRADIENT_ROTATION, GradientRotation.VERTICAL.toString());	
				super.getIcon().setImage(this.vertical);	
				}

			if(iterator == 2){
				if(graph.isLabelSelected())
				graph.setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, GradientRotation.DIAGONAL.toString());	
				else
					graph.setCellStyles(MXConstants.GRADIENT_ROTATION, GradientRotation.DIAGONAL.toString());	
				super.getIcon().setImage(this.diagonal);	
				}
			if(iterator == 3){
				if(graph.isLabelSelected())
					graph.setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, null);	
				else
				graph.setCellStyles(MXConstants.GRADIENT_ROTATION, null);	
				super.getIcon().setImage(this.gradientno);	
				}
			iterator++;
			
			}

}