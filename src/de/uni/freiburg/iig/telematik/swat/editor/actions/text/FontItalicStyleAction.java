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

public class FontItalicStyleAction extends AbstractPNEditorAction {
	
	private static final long serialVersionUID = 7450908146578160638L;
	protected boolean italic = false;
	
	public FontItalicStyleAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Italic", IconFactory.getIcon("italic"));		
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if(!italic){
		getGraph().setCellStyles((String) MXConstants.FONT_STYLE, "italic");
		italic = true;
		}
		else {
			getGraph().setCellStyles((String) MXConstants.FONT_STYLE, "normal");
		italic = false;
		}		
	}

}