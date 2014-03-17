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
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.graph.Utils;
import de.uni.freiburg.iig.telematik.swat.editor.menu.GraphicsToolBar.FillStyle;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class FillColorSelectionAction extends AbstractPNEditorAction {
	public static Color DEFAULT_FILL_COLOR = new Color(255, 255, 255);
	public static Color DEFAULT_GRADIENT_COLOR = new Color(0, 0, 0);
//	private Color backgroundColor;
//	private Color gradientColor;
//	private GradientRotation gradientRotation;

	public FillColorSelectionAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "FillColor", IconFactory.getIcon("fill"));
		setFillColor(DEFAULT_FILL_COLOR, DEFAULT_GRADIENT_COLOR, GradientRotation.VERTICAL);

	}

	public void setFillColor(Color fillColor, Color gradientColor, GradientRotation gradientRotation) {
		Image image;
		try {
			image = Utils.createIconImage(fillColor, gradientColor, gradientRotation, SwatProperties.getInstance().getIconSize().getSize());
			setIconImage(image);
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setIconImage(Image image) {
		getIcon().setImage(image);

	}

	public void actionPerformed(ActionEvent e) {
		PNGraph graph = getEditor().getGraphComponent().getGraph();
		FillStyle fillStlye = getEditor().getEditorToolbar().getGraphicsToolbar().getFillStyle();
		Color backgroundColor;
		switch (fillStlye) {
		case SOLID:
			if (!graph.isSelectionEmpty()) {
				backgroundColor = JColorChooser.showDialog(getEditor().getGraphComponent(), "Background Color", null);
if(backgroundColor != null){
				if (graph.isLabelSelected()) {
					graph.setCellStyles(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, mxUtils.hexString(backgroundColor));
				} else {
					graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, mxUtils.hexString(backgroundColor));
				}
				}



			}
			break;
		case GRADIENT:
			if (!graph.isSelectionEmpty()) {
				Color gradientColor = JColorChooser.showDialog(getEditor().getGraphComponent(), "Gradient Color", null);
				if(gradientColor != null){
				if (graph.isLabelSelected()) {
					graph.setCellStyles(MXConstants.LABEL_GRADIENTCOLOR, mxUtils.hexString(gradientColor));

				} else {
					graph.setCellStyles(mxConstants.STYLE_GRADIENTCOLOR, mxUtils.hexString(gradientColor));
				}
				}
				
			}
			break;
		case NOFILL:
			if (!graph.isSelectionEmpty()) {
				Color newColor = JColorChooser.showDialog(getEditor().getGraphComponent(), "Background Color", null);
				if(newColor != null){
				if (graph.isLabelSelected()){
					graph.setCellStyles(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, mxUtils.hexString(newColor));
					graph.setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, null);
					graph.setCellStyles(MXConstants.LABEL_GRADIENTCOLOR, mxUtils.hexString(FillGradientColorAction.DEFAULT_GRADIENT_COLOR));}

				else{
					graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, mxUtils.hexString(newColor));
					graph.setCellStyles(MXConstants.GRADIENT_ROTATION, null);
					graph.setCellStyles(mxConstants.STYLE_GRADIENTCOLOR, mxUtils.hexString(FillGradientColorAction.DEFAULT_GRADIENT_COLOR));
	
					}
				getEditor().getEditorToolbar().getGraphicsToolbar().setFillStyle(FillStyle.SOLID);
				}
			
			}
			break;
		default:
			break;

		}
		PNGraphCell selectedCell = (PNGraphCell) graph.getSelectionCell();
		if(selectedCell != null){
		Set<PNGraphCell> setWithOneCell = new HashSet<PNGraphCell>();
		setWithOneCell.add(selectedCell);
		getEditor().getEditorToolbar().updateView(setWithOneCell);
		}

//		try {
//			setFillColor(backgroundColor, gradientColor, this.gradientRotation);
//			getEditor().getEditorToolbar().setFillStyle(fillStlye, backgroundColor, gradientColor, this.gradientRotation);
//		} catch (PropertyException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

	}

	public void setNoFill() {
		ImageIcon noFill;
		try {
			noFill = IconFactory.getIcon("no_fill");
			java.awt.Image img = noFill.getImage();
			int size = getIcon().getIconWidth();
			java.awt.Image newimg = img.getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH);
			getIcon().setImage(newimg);
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
