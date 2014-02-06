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

import javax.swing.ImageIcon;
import javax.swing.JColorChooser;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractObjectGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Style;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.ColorChooser;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.graph.Utils;
import de.uni.freiburg.iig.telematik.swat.editor.menu.ToolBar.FillStyle;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class LineColorSelectionAction extends AbstractPNEditorAction {
	public static Color DEFAULT_LINE_COLOR = new Color(255, 255, 255);
	public static Color DEFAULT_GRADIENT_COLOR = new Color(0, 0, 0);
//	private Color backgroundColor;
//	private Color gradientColor;
//	private GradientRotation gradientRotation;

	public LineColorSelectionAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "FillColor", IconFactory.getIcon("fill"));
		setLineColor(DEFAULT_LINE_COLOR, false);

	}

	public void setLineColor(Color fillColor, boolean isCurve) {
		Image image;
		try {
			image = Utils.createLIconImage(fillColor, SwatProperties.getInstance().getIconSize().getSize(), 1, Style.SOLID, isCurve );
			setIconImage(image);
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setIconImage(Image image) {
		getIcon().setImage(image);

	}

	public void actionPerformed(ActionEvent e) {
		PNGraph graph = getEditor().getGraphComponent().getGraph();
		FillStyle fillStlye = getEditor().getEditorToolbar().getFillStyle();
		Color backgroundColor;
		if (!graph.isSelectionEmpty()) {
			Color newColor = JColorChooser.showDialog(getEditor().getGraphComponent(), "Stroke Color", null);
			if (newColor != null) {
				if (graph.isLabelSelected()) {
					graph.setCellStyles(mxConstants.STYLE_LABEL_BORDERCOLOR, mxUtils.hexString(newColor));
				} else {
					graph.setCellStyles(mxConstants.STYLE_STROKECOLOR, mxUtils.hexString(newColor));
				}
				
//					setLineColor(newColor);
		

			}//TODO: None Border Color not viewed correctly
//		else{
//			if (graph.isLabelSelected()) {
//				graph.setCellStyles(mxConstants.STYLE_LABEL_BORDERCOLOR, "none");
//			} else {
//				graph.setCellStyles(mxConstants.STYLE_STROKECOLOR, "none");
//			}
//		}
		PNGraphCell selectedCell = (PNGraphCell) graph.getSelectionCell();
		if(selectedCell != null){
		Set<PNGraphCell> setWithOneCell = new HashSet<PNGraphCell>();
		setWithOneCell.add(selectedCell);
		getEditor().getEditorToolbar().updateView(setWithOneCell);
		}



		}}
	public void setNoFill() {
		ImageIcon noFill;
		try {
			noFill = IconFactory.getIcon("no_fill");
			java.awt.Image img = noFill.getImage();
			int size = getIcon().getIconWidth();
			java.awt.Image newimg = img.getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH);
			getIcon().setImage(newimg);
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
