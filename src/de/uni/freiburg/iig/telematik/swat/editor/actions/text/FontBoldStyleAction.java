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

public class FontBoldStyleAction extends AbstractPNEditorAction {
	
	private static final long serialVersionUID = 7450908146578160638L;
	protected boolean bold = false;
	
	public FontBoldStyleAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Bold", IconFactory.getIcon("bold"));		
	}


	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if(!bold){
		getGraph().setCellStyles((String) MXConstants.FONT_WEIGHT, "bold");
		bold = true;
		}
		else {
		getGraph().setCellStyles((String) MXConstants.FONT_WEIGHT, "normal");
		bold = false;
		}		
	}

}