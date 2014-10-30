package de.uni.freiburg.iig.telematik.swat.editor.actions.zoom;

import java.awt.event.ActionEvent;
import java.io.IOException;

import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraphView;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.menu.EditorProperties;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;

public class ZoomOutAction extends AbstractPNEditorAction {
	
	private static final long serialVersionUID = 7450908146578160638L;
	private String 	degree = "90";
	private mxGraphView view;
	private double currentZoom;
	
	
	public ZoomOutAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Zoom Out", IconFactory.getIcon("zoom_out"));		
		view = getEditor().getGraphComponent().getGraph().getView();
		


	}



	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		currentZoom = view.getScale();
		if(currentZoom >0 +  EditorProperties.getInstance().getDefaultZoomStep() )
			getEditor().getGraphComponent().zoomTo(currentZoom - EditorProperties.getInstance().getDefaultZoomStep(), getEditor().getGraphComponent().isCenterZoom());
			}
}