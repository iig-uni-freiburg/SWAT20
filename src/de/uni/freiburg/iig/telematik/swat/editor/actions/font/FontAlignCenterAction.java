package de.uni.freiburg.iig.telematik.swat.editor.actions.font;

import java.awt.event.ActionEvent;
import java.io.IOException;

import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;

@SuppressWarnings("serial")
public class FontAlignCenterAction extends AbstractPNEditorAction
{
public FontAlignCenterAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Center", IconFactory.getIcon("center"));
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e)
	{
		mxGraph graph = getEditor().getGraphComponent().getGraph();
		if (graph != null && !graph.isSelectionEmpty())
		{
			graph.setCellStyles(mxConstants.STYLE_ALIGN,mxConstants.ALIGN_CENTER);
		}
	}
}