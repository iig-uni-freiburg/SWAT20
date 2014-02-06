package de.uni.freiburg.iig.telematik.swat.editor.actions.fill;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JColorChooser;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.graph.Utils;
import de.uni.freiburg.iig.telematik.swat.editor.menu.ToolBar.FillStyle;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory.IconSize;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class FillBackgroundColorAction extends AbstractPNEditorAction{
	public static Color DEFAULT_FILL_COLOR = new Color(255,255,255);
	private Color fillColor;
	public FillBackgroundColorAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "BackgroundColor", IconFactory.getIcon("fill"));

		setFillColor(DEFAULT_FILL_COLOR);
	}




	public void actionPerformed(ActionEvent e) {
		PNGraph graph = getEditor().getGraphComponent().getGraph();


				PNGraphCell selectedCell = (PNGraphCell) graph.getSelectionCell();
				if (graph.isLabelSelected()){
					graph.setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, null);
					graph.setCellStyles(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, mxUtils.hexString(fillColor));
				}
				else{
					graph.setCellStyles(MXConstants.GRADIENT_ROTATION, null);
					graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, mxUtils.hexString(fillColor));
					}
				getEditor().getEditorToolbar().setFillStyle(FillStyle.SOLID);
				Set<PNGraphCell> setWithOneCell = new HashSet<PNGraphCell>();
				setWithOneCell.add(selectedCell);
				getEditor().getEditorToolbar().updateView(setWithOneCell);
				
				
					

		
	}

	public void setFillColor(Color fillColor) throws PropertyException, IOException {
		Image image = Utils.createIconImage(fillColor,fillColor, GradientRotation.VERTICAL , SwatProperties.getInstance().getIconSize().getSize()/3);
		this.fillColor= fillColor;
		setIconImage(image);
	}
	public void setIconImage(Image image) throws PropertyException, IOException {
        getIcon().setImage(image);

	}


}


