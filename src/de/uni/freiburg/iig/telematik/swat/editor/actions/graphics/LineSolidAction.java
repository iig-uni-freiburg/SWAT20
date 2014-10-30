package de.uni.freiburg.iig.telematik.swat.editor.actions.graphics;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;

@SuppressWarnings("serial")
public class LineSolidAction extends AbstractPNEditorGraphicsAction {

	public LineSolidAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "gradient_vertical", IconFactory.getIcon("solid"));
		java.awt.Image img = getIcon().getImage();
		int size = getIcon().getIconWidth();
		java.awt.Image newimg = img.getScaledInstance(size / 2, size / 3, java.awt.Image.SCALE_SMOOTH);
		getIcon().setImage(newimg);
	}

	@Override
	protected void performLabelAction() {
		getGraph().setCellStyles(MXConstants.LABEL_LINE_STYLE, "solid");		
	}

	@Override
	protected void performNoLabelAction() {
		getGraph().setCellStyles(MXConstants.LINE_STYLE, "solid");
	}

	@Override
	protected void doMoreFancyStuff(ActionEvent e) throws Exception {
		// TODO Auto-generated method stub
		
	}

}