package de.uni.freiburg.iig.telematik.swat.editor.actions.font;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxCellEditor;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;

public class FontLineThroughStyleAction extends AbstractPNEditorAction {
	
	private static final long serialVersionUID = 7450908146578160638L;
	protected boolean linethrough = false;
	
	public FontLineThroughStyleAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Linethrough", IconFactory.getIcon("linethrough"));
		
	}

	public void actionPerformed(ActionEvent e) {
		PNGraph graph = getEditor().getGraphComponent().getGraph();

		if(!linethrough){
		graph.setCellStyles((String) MXConstants.FONT_DECORATION, "line-through");
		linethrough = true;
		}
		else {
		graph.setCellStyles((String) MXConstants.FONT_DECORATION, null);
		linethrough = false;
		}
}
}