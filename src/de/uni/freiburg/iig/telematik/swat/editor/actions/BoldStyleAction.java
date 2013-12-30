package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxConstants;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;

public class BoldStyleAction extends AbstractPNEditorAction {
	
	private static final long serialVersionUID = 7450908146578160638L;
	protected boolean bold = false;
	
	public BoldStyleAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Bold", IconFactory.getIcon("bold"));		
	}

	public void actionPerformed(ActionEvent e) {
				mxIGraphModel model = getEditor().getGraphComponent().getGraph().getModel();
				model.beginUpdate();
				try {
					getEditor().getGraphComponent().stopEditing(false);
					getEditor().getGraphComponent().getGraph().toggleCellStyleFlags(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);				
				} finally {
					model.endUpdate();
				}
		}

}