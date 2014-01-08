package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;

import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;

@SuppressWarnings("serial")
public class FontAlignRightAction extends AbstractPNEditorAction {
	
public FontAlignRightAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Right", IconFactory.getIcon("right"));
	}

	public void actionPerformed(ActionEvent e)
	{
		mxGraph graph = getEditor().getGraphComponent().getGraph();
		if (graph != null && !graph.isSelectionEmpty())
		{
			graph.setCellStyles(mxConstants.STYLE_ALIGN,mxConstants.ALIGN_RIGHT);
		}
	}
}