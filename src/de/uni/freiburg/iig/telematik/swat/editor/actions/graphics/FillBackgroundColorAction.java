package de.uni.freiburg.iig.telematik.swat.editor.actions.graphics;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.Utils;
import de.uni.freiburg.iig.telematik.swat.editor.menu.toolbars.GraphicsToolBar.FillStyle;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class FillBackgroundColorAction extends AbstractPNEditorGraphicsAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4112558885827445210L;
	public static Color DEFAULT_FILL_COLOR = new Color(255,255,255);
	private Color fillColor;
	public FillBackgroundColorAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "BackgroundColor", IconFactory.getIcon("fill"));

		setFillColor(DEFAULT_FILL_COLOR);
	}

	public void setFillColor(Color fillColor) throws PropertyException, IOException {
		Image image = Utils.createIconImage(fillColor,fillColor, GradientRotation.VERTICAL , SwatProperties.getInstance().getIconSize().getSize()/3);
		this.fillColor= fillColor;
		setIconImage(image);
	}
	public void setIconImage(Image image) throws PropertyException, IOException {
        getIcon().setImage(image);

	}

	@Override
	protected void performLabelAction() {
		getGraph().setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, null);
		getGraph().setCellStyles(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, mxUtils.hexString(fillColor));		
	}

	@Override
	protected void performNoLabelAction() {
		getGraph().setCellStyles(MXConstants.GRADIENT_ROTATION, null);
		getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, mxUtils.hexString(fillColor));		
	}

	@Override
	protected void doMoreFancyStuff(ActionEvent e) throws Exception {
		getEditor().getEditorToolbar().getGraphicsToolbar().setFillStyle(FillStyle.SOLID);		
	}


}


