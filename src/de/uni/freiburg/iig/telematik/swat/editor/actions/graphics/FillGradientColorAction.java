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
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.graph.Utils;
import de.uni.freiburg.iig.telematik.swat.editor.menu.toolbars.GraphicsToolBar.FillStyle;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class FillGradientColorAction extends AbstractPNEditorGraphicsAction {
	public static Color DEFAULT_FILL_COLOR = new Color(255, 255, 255);
	public static Color DEFAULT_GRADIENT_COLOR = new Color(0, 0, 0);
	private Color gradientColor = DEFAULT_GRADIENT_COLOR;
	private Color fillColor = DEFAULT_FILL_COLOR;

	public FillGradientColorAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "GradientColor", IconFactory.getIcon("fill"));
		setFillColor(DEFAULT_FILL_COLOR, DEFAULT_GRADIENT_COLOR);

	}

	public void setFillColor(Color fillColor, Color gradientColor) throws PropertyException, IOException {
		Image image = Utils.createIconImage(fillColor, gradientColor, GradientRotation.VERTICAL, SwatProperties.getInstance().getIconSize().getSize() / 3);
		this.fillColor = fillColor;
		this.gradientColor = gradientColor;
		setIconImage(image);
	}

	public void setIconImage(Image image) {
		getIcon().setImage(image);

	}

	@Override
	protected void performLabelAction() {
		NodeGraphics nodeGraphics = null;
		switch (getGraphSelectionCell().getType()) {
		case PLACE:
			nodeGraphics = getEditor().getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(getGraphSelectionCell().getId());
			break;
		case TRANSITION:
			nodeGraphics = getEditor().getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(getGraphSelectionCell().getId());
			break;
		case ARC:
			break;
		}
		Fill fill = null;
		if (nodeGraphics != null)
			fill = nodeGraphics.getFill();
		if (fill != null) {

			String colorString = fill.getColor();
			if (colorString.equals("transparent")) {
				getGraph().setCellStyles(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, mxUtils.hexString(fillColor));
			}

			String gradientColorString = fill.getGradientColor();
			if (gradientColorString == null) {
				gradientColorString = Utils.hexString(DEFAULT_GRADIENT_COLOR);

				getGraph().setCellStyles(MXConstants.LABEL_GRADIENTCOLOR, gradientColorString);

			}
			GradientRotation rotation = fill.getGradientRotation();
			if (rotation == null) {
				rotation = GradientRotation.VERTICAL;
				getGraph().setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, rotation.toString());
			}

		}
	}

	@Override
	protected void performNoLabelAction() {
		NodeGraphics nodeGraphics = null;
		switch (getGraphSelectionCell().getType()) {
		case PLACE:
			nodeGraphics = getEditor().getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(getGraphSelectionCell().getId());
			break;
		case TRANSITION:
			nodeGraphics = getEditor().getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(getGraphSelectionCell().getId());
			break;
		case ARC:
			break;
		}
		Fill fill = null;
		if (nodeGraphics != null)
			fill = nodeGraphics.getFill();
		if (fill != null) {

			String colorString = fill.getColor();
			if (colorString.equals("transparent")) {
				getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, mxUtils.hexString(fillColor));
			}

			String gradientColorString = fill.getGradientColor();
			if (gradientColorString == null) {
				gradientColorString = Utils.hexString(DEFAULT_GRADIENT_COLOR);
				getGraph().setCellStyles(mxConstants.STYLE_GRADIENTCOLOR, gradientColorString);
			}
			GradientRotation rotation = fill.getGradientRotation();
			if (rotation == null) {
				rotation = GradientRotation.VERTICAL;
				getGraph().setCellStyles(MXConstants.GRADIENT_ROTATION, rotation.toString());
			}

		}
	}

	@Override
	protected void doMoreFancyStuff(ActionEvent e) throws Exception {
		getEditor().getEditorToolbar().getGraphicsToolbar().setFillStyle(FillStyle.GRADIENT);

	}
}
