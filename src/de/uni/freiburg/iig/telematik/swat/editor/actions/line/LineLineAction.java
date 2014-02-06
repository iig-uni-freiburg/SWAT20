package de.uni.freiburg.iig.telematik.swat.editor.actions.line;

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
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Style;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.graph.Utils;
import de.uni.freiburg.iig.telematik.swat.editor.menu.ToolBar.FillStyle;
import de.uni.freiburg.iig.telematik.swat.editor.menu.ToolBar.LineStyle;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory.IconSize;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class LineLineAction extends AbstractPNEditorAction{
	private static final Double DEFAULT_STROKEWIDTH = 1.0;
	private static final Style DEFAULT_LINESTYLE = Style.SOLID;
	public static Color DEFAULT_FILL_COLOR = new Color(255,255,255);
	private Color fillColor;
	public LineLineAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "LineColor", IconFactory.getIcon("line"));
		java.awt.Image img = getIcon().getImage();
		int size = getIcon().getIconWidth();
		java.awt.Image newimg = img.getScaledInstance(size /3, size /3 , java.awt.Image.SCALE_SMOOTH);
		getIcon().setImage(newimg);
		setFillColor(DEFAULT_FILL_COLOR, DEFAULT_STROKEWIDTH, DEFAULT_LINESTYLE);
	}




	public void actionPerformed(ActionEvent e) {
		PNGraph graph = getEditor().getGraphComponent().getGraph();


				PNGraphCell selectedCell = (PNGraphCell) graph.getSelectionCell();
				graph.setCellStyles(mxConstants.STYLE_ROUNDED, "false");
				graph.setCellStyles(mxConstants.STYLE_EDGE, "direct");
				getEditor().getEditorToolbar().setFillStyle(FillStyle.SOLID);
				Set<PNGraphCell> setWithOneCell = new HashSet<PNGraphCell>();
				setWithOneCell.add(selectedCell);
				getEditor().getEditorToolbar().updateView(setWithOneCell);
				
				
					

		
	}

	public void setFillColor(Color fillColor, Double defaultStrokewidth, Style defaultLinestyle) throws PropertyException, IOException {
		Image image = Utils.createLIconImage(fillColor, SwatProperties.getInstance().getIconSize().getSize()/3, defaultStrokewidth, defaultLinestyle, false);
		this.fillColor= fillColor;
		setIconImage(image);
	}
	public void setIconImage(Image image) throws PropertyException, IOException {
        getIcon().setImage(image);

	}


}


