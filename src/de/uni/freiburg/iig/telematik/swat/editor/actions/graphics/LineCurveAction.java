package de.uni.freiburg.iig.telematik.swat.editor.actions.graphics;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.Utils;
import de.uni.freiburg.iig.telematik.swat.editor.menu.toolbars.GraphicsToolBar.LineStyle;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class LineCurveAction extends AbstractPNEditorGraphicsAction {
	public static Color DEFAULT_FILL_COLOR = new Color(255, 255, 255);
	public static Color DEFAULT_GRADIENT_COLOR = new Color(0, 0, 0);
	private Color fillColor = DEFAULT_FILL_COLOR;

	public LineCurveAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Curve Color", IconFactory.getIcon("round"));

		setLineColor(DEFAULT_FILL_COLOR);

	}

	public void setLineColor(Color fillColor) throws PropertyException, IOException {
		Image image = Utils.createLIconImage(fillColor, SwatProperties.getInstance().getIconSize().getSize() / 3, 1, Line.Style.SOLID, true);
		this.fillColor = fillColor;
		setIconImage(image);
	}

	public void setIconImage(Image image) {
		getIcon().setImage(image);

	}

	@Override
	protected void performLabelAction() {
		getGraph().setCellStyles(mxConstants.STYLE_LABEL_BORDERCOLOR, mxUtils.hexString(fillColor));

	}

	@Override
	protected void performNoLabelAction() {
		getGraph().setCellStyles(mxConstants.STYLE_STROKECOLOR, mxUtils.hexString(fillColor));
		getGraph().setCellStyles(mxConstants.STYLE_ROUNDED, "true");
		getGraph().setCellStyles(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);
	}

	@Override
	protected void doMoreFancyStuff(ActionEvent e) throws Exception {
		getEditor().getEditorToolbar().getGraphicsToolbar().setLineStyle(LineStyle.NORMAL);

	}
}
