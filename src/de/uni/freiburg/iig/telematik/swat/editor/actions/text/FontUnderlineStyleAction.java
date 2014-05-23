package de.uni.freiburg.iig.telematik.swat.editor.actions.text;

import java.awt.event.ActionEvent;
import java.io.IOException;

import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxConstants;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;

public class FontUnderlineStyleAction extends AbstractPNEditorAction {
	
	private static final long serialVersionUID = 7450908146578160638L;
	protected boolean underline = false;
	
	public FontUnderlineStyleAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Underline", IconFactory.getIcon("underline"));		
	}


	public void actionPerformed(ActionEvent e) {
				PNGraph graph = getEditor().getGraphComponent().getGraph();

				if(!underline){
				graph.setCellStyles((String) MXConstants.FONT_DECORATION, "underline");
				underline = true;
				}
				else {
				graph.setCellStyles((String) MXConstants.FONT_DECORATION, null);
				underline = false;
				}
		}

}