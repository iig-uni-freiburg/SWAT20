package de.uni.freiburg.iig.telematik.swat.editor.actions.fill;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.graph.Utils;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory.IconSize;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperty;

@SuppressWarnings("serial")
public class FillGradientDirectionAction extends AbstractPNEditorAction {
	private Image diagonal;
	private Image vertical;
	private Image horizontal;
	private Image gradientno;

	public FillGradientDirectionAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "gradient_horizontal", IconFactory.getIcon("gradient_horizontal"));
		horizontal = getIcon().getImage();
		vertical = getVerticalImage();
		diagonal = IconFactory.getIcon("gradient-diagonal").getImage();
		gradientno = IconFactory.getIcon("gradient_no").getImage();
		java.awt.Image img = getIcon().getImage();
		int size = getIcon().getIconWidth();
		java.awt.Image newimg = img.getScaledInstance(size/2, size/2,  java.awt.Image.SCALE_SMOOTH ) ;  
		getIcon().setImage(newimg);
	}

	private Image getVerticalImage() {
		int size = 0;
	try {
		IconSize iconSize = SwatProperties.getInstance().getIconSize();
		size = iconSize.getSize();
	} catch (PropertyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
        Image image = new BufferedImage (size, size, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics g = image.getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color color1 = new Color(255, 0, 0, 100);
        g2.setColor (color1);
        g2.fillOval (0, 0, 80, 80);
        g2.dispose ();
        return image;
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
			if (graph.isLabelSelected())
				graph.setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, GradientRotation.DIAGONAL.toString());
			else
				graph.setCellStyles(MXConstants.GRADIENT_ROTATION, GradientRotation.DIAGONAL.toString());
			getIcon().setImage(diagonal);
		} else if (getIcon().getImage() == diagonal) {
			if (graph.isLabelSelected())
				graph.setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, null);
			else
				graph.setCellStyles(MXConstants.GRADIENT_ROTATION, null);
			getIcon().setImage(gradientno);
		}
		
//		java.awt.Image img = getIcon().getImage();
//		int size = getIcon().getIconWidth();
//		java.awt.Image newimg = img.getScaledInstance(size/2, size/2,  java.awt.Image.SCALE_SMOOTH ) ;  
//		getIcon().setImage(newimg);

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