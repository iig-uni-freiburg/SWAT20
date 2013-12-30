package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxConstants;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;

public class TextRotationAction extends AbstractPNEditorAction {
	
	private static final long serialVersionUID = 7450908146578160638L;
	private String 	degree = "90";
	
	
	public TextRotationAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Rotation", IconFactory.getIcon("rotate"));		
	}

	public void actionPerformed(ActionEvent e) {
	
				getEditor().getGraphComponent().getGraph().setCellStyles(MXConstants.TEXT_ROTATION_DEGREE, degree);
				if(degree.equals("0") || degree.equals("180") || degree.equals("360"))
				getEditor().getGraphComponent().getGraph().setCellStyles(mxConstants.STYLE_HORIZONTAL, "true");
				else
					getEditor().getGraphComponent().getGraph().setCellStyles(mxConstants.STYLE_HORIZONTAL, "false");
				degree = 	new Integer(degree)%360 + 90 + "";
		}

}