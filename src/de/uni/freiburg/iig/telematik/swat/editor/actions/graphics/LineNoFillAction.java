package de.uni.freiburg.iig.telematik.swat.editor.actions.graphics;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.mxgraph.util.mxConstants;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.menu.GraphicsToolBar.LineStyle;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class LineNoFillAction extends AbstractPNEditorAction{
	public LineNoFillAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "NoFill", IconFactory.getIcon("no_fill"));
		java.awt.Image img = getIcon().getImage();
		int size = SwatProperties.getInstance().getIconSize().getSize()/3;
		java.awt.Image newimg = img.getScaledInstance(size, size,  java.awt.Image.SCALE_SMOOTH ) ;  
		getIcon().setImage(newimg);
		
	}

	public void actionPerformed(ActionEvent e) {
		PNGraph graph = getEditor().getGraphComponent().getGraph();


		PNGraphCell selectedCell = (PNGraphCell) graph.getSelectionCell();
				if (graph.isLabelSelected()) {
					graph.setCellStyles(mxConstants.STYLE_LABEL_BORDERCOLOR, "none");
				} else {
					graph.setCellStyles(mxConstants.STYLE_STROKECOLOR, "none");
				}
				getEditor().getEditorToolbar().getGraphicsToolbar().setLineStyle(LineStyle.NOFILL);
				Set<PNGraphCell> setWithOneCell = new HashSet<PNGraphCell>();
				setWithOneCell.add(selectedCell);
				getEditor().getEditorToolbar().updateView(setWithOneCell);

			}
	
}


