package de.uni.freiburg.iig.telematik.swat.editor.actions;

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
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;

public class LineThroughStyleAction extends AbstractPNEditorAction {
	
	private static final long serialVersionUID = 7450908146578160638L;
	protected boolean bold = false;
	
	public LineThroughStyleAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Linethrough", IconFactory.getIcon("linethrough"));
		
	}

	public void actionPerformed(ActionEvent e) {
		mxIGraphModel model = getEditor().getGraphComponent().getGraph().getModel();
		model.beginUpdate();
		try {
			getEditor().getGraphComponent().stopEditing(false);
			getEditor().getGraphComponent().getGraph().toggleCellStyleFlags(mxConstants.STYLE_FONTSTYLE, 16);				
		} finally {
			model.endUpdate();
		}
}
}