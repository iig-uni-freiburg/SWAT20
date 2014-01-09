package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Map;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;

@SuppressWarnings("serial")
public class FillGradientDirectionAction extends AbstractPNEditorAction {
	private Image diagonal;
	private Image vertical;
	private Image horizontal;
	private Image gradientno;

	public FillGradientDirectionAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "gradient_horizontal", IconFactory.getIcon("gradient_horizontal"));
		horizontal = getIcon().getImage();
		vertical = IconFactory.getIcon("gradient_vertical").getImage();
		diagonal = IconFactory.getIcon("gradient-diagonal").getImage();
		gradientno = IconFactory.getIcon("gradient_no").getImage();
	}

	public void actionPerformed(ActionEvent e) {
		PNGraph graph = getEditor().getGraphComponent().getGraph();
		if (getIcon().getImage() == gradientno) {
			if (graph.isLabelSelected())
				graph.setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, GradientRotation.HORIZONTAL.toString());
			else
				graph.setCellStyles(MXConstants.GRADIENT_ROTATION, GradientRotation.HORIZONTAL.toString());
			getIcon().setImage(horizontal);
		}

		else if (getIcon().getImage() == horizontal) {
			if (graph.isLabelSelected())
				graph.setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, GradientRotation.VERTICAL.toString());
			else
				graph.setCellStyles(MXConstants.GRADIENT_ROTATION, GradientRotation.VERTICAL.toString());
			getIcon().setImage(vertical);
		}

		else if (getIcon().getImage() == vertical) {
			System.out.println("verti");
			if (graph.isLabelSelected())
				graph.setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, GradientRotation.DIAGONAL.toString());
			else
				graph.setCellStyles(MXConstants.GRADIENT_ROTATION, GradientRotation.DIAGONAL.toString());
			getIcon().setImage(diagonal);
		} else if (getIcon().getImage() == diagonal) {
			System.out.println("dia");
			if (graph.isLabelSelected())
				graph.setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, null);
			else
				graph.setCellStyles(MXConstants.GRADIENT_ROTATION, null);
			getIcon().setImage(gradientno);
		}

	}

	public void setImageIcon(Image image) {
		getIcon().setImage(image);
	}

	public void setGradientnoIconImage() {
		getIcon().setImage(gradientno);
	}

	public void setVerticalIconImage() {
		getIcon().setImage(vertical);
	}

	public void setHorizontalIconImage() {
		getIcon().setImage(horizontal);
	}

	public void setDiagonalIconImage() {
		getIcon().setImage(diagonal);
	}
}