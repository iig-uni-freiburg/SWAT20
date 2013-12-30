package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.mxgraph.util.mxConstants;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;

public class RemoveImageAction extends AbstractPNEditorAction{
	public RemoveImageAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Image Remove", IconFactory.getIcon("remove_image"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		String key = mxConstants.STYLE_IMAGE;
		String message = "Image";

			PNGraph graph = getEditor().getGraphComponent().getGraph();

			if (graph != null && !graph.isSelectionEmpty())
			{
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Choose existing working directory");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileFilter filter;
				fileChooser.setFileFilter(new ImageFileFilter("png"));
				fileChooser.setFileFilter(new ImageFileFilter("jpeg"));
				int returnVal = fileChooser.showOpenDialog(getEditor().getGraphComponent());

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fileChooser.getSelectedFile();
		        	getEditor().getGraphComponent().getGraph().setCellStyles(mxConstants.STYLE_IMAGE, "");
		            getEditor().getGraphComponent().getGraph().setCellStyles("shape",mxConstants.SHAPE_IMAGE);

			}}

	}
	


}


