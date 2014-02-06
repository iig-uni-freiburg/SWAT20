package de.uni.freiburg.iig.telematik.swat.editor.actions.line;

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
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.graph.Utils;
import de.uni.freiburg.iig.telematik.swat.editor.menu.ToolBar.FillStyle;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class LineCurveAction extends AbstractPNEditorAction{
	public static Color DEFAULT_FILL_COLOR = new Color(255,255,255);
	public static Color DEFAULT_GRADIENT_COLOR = new Color(0,0,0);
	private Color gradientColor = DEFAULT_GRADIENT_COLOR;
	private Color fillColor = DEFAULT_FILL_COLOR ;
	public LineCurveAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "GradientColor", IconFactory.getIcon("round"));
		java.awt.Image img = getIcon().getImage();
		int size = getIcon().getIconWidth();
		java.awt.Image newimg = img.getScaledInstance(size /3, size /3 , java.awt.Image.SCALE_SMOOTH);
		getIcon().setImage(newimg);
		setLineColor(DEFAULT_FILL_COLOR, DEFAULT_GRADIENT_COLOR);
		
	}




	public void actionPerformed(ActionEvent e) {
		PNGraph graph = getEditor().getGraphComponent().getGraph();
		graph.setCellStyles(mxConstants.STYLE_ROUNDED, "true");
		graph.setCellStyles(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);
		PNGraphCell selectedCell = (PNGraphCell) graph.getSelectionCell();
		getEditor().getEditorToolbar().setFillStyle(FillStyle.GRADIENT);
		Set<PNGraphCell> setWithOneCell = new HashSet<PNGraphCell>();
		setWithOneCell.add(selectedCell);
		getEditor().getEditorToolbar().updateView(setWithOneCell);

	}

	public void setLineColor(Color fillColor, Color gradientColor) throws PropertyException, IOException {
		Image image = Utils.createLIconImage(fillColor, SwatProperties.getInstance().getIconSize().getSize()/3, 1, Line.Style.SOLID, true);
		this.fillColor = fillColor;
		this.gradientColor = gradientColor;
		setIconImage(image);
	}
	public void setIconImage(Image image){
        getIcon().setImage(image);

	}
}


