package de.uni.freiburg.iig.telematik.swat.editor.actions.graphics;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JColorChooser;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Style;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.graph.Utils;
import de.uni.freiburg.iig.telematik.swat.editor.menu.ToolBar.FillStyle;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class LineColorSelectionAction extends AbstractPNEditorAction {
	public static Color DEFAULT_FILL_COLOR = new Color(255, 255, 255);
	public static Color DEFAULT_GRADIENT_COLOR = new Color(0, 0, 0);
	private static final Double DEFAULT_STROKEWIDTH = 1.0;
	private static final Style DEFAULT_LINESTYLE = Style.SOLID;
	private Color fillColor;
//	private Color backgroundColor;
//	private Color gradientColor;
//	private GradientRotation gradientRotation;

	public LineColorSelectionAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "FillColor", IconFactory.getIcon("fill"));
		setFillColor(DEFAULT_FILL_COLOR, DEFAULT_STROKEWIDTH, DEFAULT_LINESTYLE, false);

	}
//
//	public void setLineColor(Color fillColor, boolean isCurve) {
//		Image image;
//		try {
//			image = Utils.createLIconImage(fillColor, SwatProperties.getInstance().getIconSize().getSize(), 1, Style.SOLID, isCurve );
//			setIconImage(image);
//		} catch (PropertyException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

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
	
	public void setFillColor(Color fillColor, Double defaultStrokewidth, Style defaultLinestyle, boolean isLineCurve) throws PropertyException, IOException {
		Image image = Utils.createLIconImage(fillColor, SwatProperties.getInstance().getIconSize().getSize(), defaultStrokewidth, defaultLinestyle, isLineCurve);
		this.fillColor= fillColor;
		setIconImage(image);
	}
}
