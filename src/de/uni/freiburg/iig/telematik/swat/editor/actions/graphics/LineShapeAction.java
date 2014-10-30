package de.uni.freiburg.iig.telematik.swat.editor.actions.graphics;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;

import com.mxgraph.util.mxConstants;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;

@SuppressWarnings("serial")
public class LineShapeAction extends AbstractPNEditorAction {
	private Image curve;
	private Image line;

	public LineShapeAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "LineCurve", IconFactory.getIcon("line"));
		line = getIcon().getImage();
		curve = IconFactory.getIcon("round").getImage();
	}

	public void setCurveIconImage() {
		getIcon().setImage(curve);

	}

	public void setLineIconImage() {
		getIcon().setImage(line);
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if (getIcon().getImage() == line) {
			getGraph().setCellStyles(mxConstants.STYLE_ROUNDED, "true");
			getGraph().setCellStyles(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);
			getIcon().setImage(curve);
		}
		else if (getIcon().getImage() == curve) {
			getGraph().setCellStyles(mxConstants.STYLE_ROUNDED, "false");
			getGraph().setCellStyles(mxConstants.STYLE_EDGE, "direct");
			getIcon().setImage(line);
		}		
	}

}