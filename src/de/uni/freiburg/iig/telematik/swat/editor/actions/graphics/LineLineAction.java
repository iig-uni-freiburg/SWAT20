package de.uni.freiburg.iig.telematik.swat.editor.actions.graphics;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Style;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.graph.Utils;
import de.uni.freiburg.iig.telematik.swat.editor.menu.GraphicsToolBar.LineStyle;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
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
		setFillColor(DEFAULT_FILL_COLOR, DEFAULT_STROKEWIDTH);
	}




	public void actionPerformed(ActionEvent e) {
		PNGraph graph = getEditor().getGraphComponent().getGraph();

		if (graph.isLabelSelected()) {
			graph.setCellStyles(mxConstants.STYLE_LABEL_BORDERCOLOR, mxUtils.hexString(fillColor));
		} else {
			graph.setCellStyles(mxConstants.STYLE_STROKECOLOR, mxUtils.hexString(fillColor));
		}
				PNGraphCell selectedCell = (PNGraphCell) graph.getSelectionCell();
				graph.setCellStyles(mxConstants.STYLE_ROUNDED, "false");
				graph.setCellStyles(mxConstants.STYLE_EDGE, "direct");
				getEditor().getEditorToolbar().getGraphicsToolbar().setLineStyle(LineStyle.NORMAL);
				Set<PNGraphCell> setWithOneCell = new HashSet<PNGraphCell>();
				setWithOneCell.add(selectedCell);
				getEditor().getEditorToolbar().updateView(setWithOneCell);
				
				
					

		
	}

	public void setFillColor(Color fillColor, Double defaultStrokewidth) throws PropertyException, IOException {
		Image image = Utils.createLIconImage(fillColor, SwatProperties.getInstance().getIconSize().getSize()/3, defaultStrokewidth, Line.Style.SOLID, false);
		this.fillColor= fillColor;
		setIconImage(image);
	}
	public void setIconImage(Image image) throws PropertyException, IOException {
        getIcon().setImage(image);

	}


}


