package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Map;

import javax.swing.JColorChooser;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;

@SuppressWarnings("serial")
public class ShowHideLabelsAction extends AbstractPNEditorAction {
	private Image visible;
	private Image invisible;
	int iterator = 0; 

	public ShowHideLabelsAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Visible", IconFactory.getIcon("visible"));
		invisible = IconFactory.getIcon("invisible").getImage();
		visible = IconFactory.getIcon("visible").getImage();
	}

	public void actionPerformed(ActionEvent e) {
		PNGraph graph = getEditor().getGraphComponent().getGraph();

		Map<String, Object> style = graph.getCellStyle(graph.getSelectionCell());
		double width = mxUtils.getDouble(style, mxConstants.STYLE_STROKEWIDTH);

		iterator = iterator%2;
		if(iterator == 0){
			graph.setCellStyles("noLabel", "1");
			super.getIcon().setImage(this.invisible);	
		}
			
			
		if(iterator == 1){
			graph.setCellStyles("noLabel", "0");
			super.getIcon().setImage(this.visible);	
			}

			
		iterator++;
		
		}

}