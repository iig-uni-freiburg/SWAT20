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
import de.uni.freiburg.iig.telematik.swat.editor.menu.GraphicsToolBar.FillStyle;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class LineColorSelectionAction extends AbstractPNEditorGraphicsAction {
	public static Color DEFAULT_FILL_COLOR = new Color(255, 255, 255);
	public static Color DEFAULT_GRADIENT_COLOR = new Color(0, 0, 0);
	private static final Double DEFAULT_STROKEWIDTH = 1.0;
	private static final Style DEFAULT_LINESTYLE = Style.SOLID;
	private Color fillColor;

	public LineColorSelectionAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "FillColor", IconFactory.getIcon("fill"));
		setFillColor(DEFAULT_FILL_COLOR, DEFAULT_STROKEWIDTH, DEFAULT_LINESTYLE, false);

	}

	public void setIconImage(Image image) {
		getIcon().setImage(image);

	}

	public void setNoFill() throws ParameterException, PropertyException, IOException {
		ImageIcon noFill;

		noFill = IconFactory.getIcon("no_fill");
		java.awt.Image img = noFill.getImage();
		int size = getIcon().getIconWidth();
		java.awt.Image newimg = img.getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH);
		getIcon().setImage(newimg);

	}

	public void setFillColor(Color fillColor, Double defaultStrokewidth, Style defaultLinestyle, boolean isLineCurve) throws PropertyException, IOException {
		Image image = Utils.createLIconImage(fillColor, SwatProperties.getInstance().getIconSize().getSize(), defaultStrokewidth, defaultLinestyle, isLineCurve);
		this.fillColor = fillColor;
		setIconImage(image);
	}

	@Override
	protected void performLabelAction() {
		Color newColor = JColorChooser.showDialog(getEditor().getGraphComponent(), "Stroke Color", null);
		if (newColor != null) {
			getGraph().setCellStyles(mxConstants.STYLE_LABEL_BORDERCOLOR, mxUtils.hexString(newColor));
		}
	}

	@Override
	protected void performNoLabelAction() {
		Color newColor = JColorChooser.showDialog(getEditor().getGraphComponent(), "Stroke Color", null);
		if (newColor != null) {
			getGraph().setCellStyles(mxConstants.STYLE_STROKECOLOR, mxUtils.hexString(newColor));
		}
	}

	@Override
	protected void doMoreFancyStuff(ActionEvent e) throws Exception {
		// TODO Auto-generated method stub

	}
}
