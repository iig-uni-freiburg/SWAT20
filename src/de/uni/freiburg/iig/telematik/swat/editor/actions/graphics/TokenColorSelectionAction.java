package de.uni.freiburg.iig.telematik.swat.editor.actions.graphics;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.graph.Utils;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.TokenChange;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.TokenColorChange;
import de.uni.freiburg.iig.telematik.swat.editor.menu.GraphicsToolBar.FillStyle;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class TokenColorSelectionAction extends AbstractPNEditorAction {
	public static Color DEFAULT_FILL_COLOR = new Color(255, 255, 255);
	public static Color DEFAULT_GRADIENT_COLOR = new Color(0, 0, 0);

	private Color tokenColor;
	private String tokenLabel;
	private JPanel parent;

	public TokenColorSelectionAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "FillColor", IconFactory.getIcon("fill"));
		setTokenColor(DEFAULT_FILL_COLOR);

	}

	public TokenColorSelectionAction(PNEditor editor, String tokenLabel) throws ParameterException, PropertyException, IOException {
		this(editor);
		this.tokenLabel = tokenLabel;
	}

	public void setTokenColor(Color fillColor) {
		this.tokenColor = fillColor;
		Image image;
		try {
			image = Utils.createIconImage(fillColor, fillColor, GradientRotation.VERTICAL, SwatProperties.getInstance().getIconSize().getSize());
			setIconImage(image);
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Color getTokenColor() {
		return tokenColor;
	}

	public void setIconImage(Image image) {
		getIcon().setImage(image);

	}

	public void setParent(JPanel parent) {
		this.parent = parent;
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		FillStyle fillStlye = getEditor().getEditorToolbar().getGraphicsToolbar().getFillStyle();
		Color backgroundColor = JColorChooser.showDialog(parent, "Token Color", null);
		if(backgroundColor != null){
		setTokenColor(backgroundColor);

		if (tokenLabel != null) {
			AbstractCPNGraphics cpnGraphics = (AbstractCPNGraphics) editor.getGraphComponent().getGraph().getNetContainer().getPetriNetGraphics();
			((mxGraphModel) getGraph().getModel()).execute(new TokenColorChange(editor,tokenLabel,getTokenColor()));
		}
		}		
	}

}
