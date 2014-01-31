package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JColorChooser;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.graph.Utils;
import de.uni.freiburg.iig.telematik.swat.editor.menu.ToolBar.FillStyle;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class FillGradientColorAction extends AbstractPNEditorAction{
	public static Color DEFAULT_FILL_COLOR = new Color(255,255,255);
	public static Color DEFAULT_GRADIENT_COLOR = new Color(0,0,0);
	public FillGradientColorAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "GradientColor", IconFactory.getIcon("fill"));
		setFillColor(DEFAULT_FILL_COLOR, DEFAULT_GRADIENT_COLOR);
		
	}




	public void actionPerformed(ActionEvent e) {
		PNGraph graph = getEditor().getGraphComponent().getGraph();
		PNGraphCell selectedCell = (PNGraphCell) graph.getSelectionCell();

		NodeGraphics nodeGraphics = null;
		switch (selectedCell.getType()) {
		case PLACE:
			nodeGraphics = getEditor().getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(selectedCell.getId());
			break;
		case TRANSITION:
			nodeGraphics = getEditor().getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(selectedCell.getId());
			break;
		case ARC:
			break;
		}
		
		Fill fill = nodeGraphics.getFill();
		if(fill != null){
			
			String gradientColorString = fill.getGradientColor();
			if(gradientColorString == null){
				gradientColorString = Utils.hexString(DEFAULT_GRADIENT_COLOR);
				
			if (graph.isLabelSelected()) {
				graph.setCellStyles(MXConstants.LABEL_GRADIENTCOLOR, gradientColorString);

			} else {
				graph.setCellStyles(mxConstants.STYLE_GRADIENTCOLOR, gradientColorString);
			}
			}
			GradientRotation rotation = fill.getGradientRotation();
			if(rotation == null){
				rotation = GradientRotation.VERTICAL;
			
			if (graph.isLabelSelected()){
				graph.setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, rotation.toString());
				}
			else{
				graph.setCellStyles(MXConstants.GRADIENT_ROTATION, rotation.toString());
				}
			}

		}
		getEditor().getEditorToolbar().setFillStyle(FillStyle.GRADIENT);
		Set<PNGraphCell> setWithOneCell = new HashSet<PNGraphCell>();
		setWithOneCell.add(selectedCell);
		getEditor().getEditorToolbar().updateView(setWithOneCell);

	}

	public void setFillColor(Color fillColor, Color gradientColor) throws PropertyException, IOException {
		Image image = Utils.createIconImage(fillColor,gradientColor, GradientRotation.VERTICAL , SwatProperties.getInstance().getIconSize().getSize()/3);
		setIconImage(image);
	}
	public void setIconImage(Image image){
        getIcon().setImage(image);

	}
}


