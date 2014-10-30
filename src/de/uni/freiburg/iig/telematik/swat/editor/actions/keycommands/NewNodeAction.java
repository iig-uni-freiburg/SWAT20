package de.uni.freiburg.iig.telematik.swat.editor.actions.keycommands;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import com.mxgraph.util.mxPoint;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;

public class NewNodeAction extends AbstractPNEditorAction {

	private int deltaX;
	private int deltaY;

	public NewNodeAction(PNEditor editor, int dx, int dy) throws ParameterException {
		super(editor);
		deltaX = dx;
		deltaY = dy;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7020268186996809614L;

	/**
	 * @param source
	 * @param pnGraph
	 * @param centerX
	 * @param centerY
	 * @return 
	 */
	protected PNGraphCell createNewNodeWithEdge(PNGraphCell source, PNGraph pnGraph, double centerX, double centerY) {
		PNGraphCell target = null;
		try {
			switch (source.getType()) {
			case PLACE:
				target = (PNGraphCell) pnGraph.addNewTransition(new mxPoint(centerX, centerY));
				break;
			case TRANSITION:
				target = (PNGraphCell) pnGraph.addNewPlace(new mxPoint(centerX, centerY));
				break;
			case ARC:
				break;
			}
			if(target != null)
			pnGraph.addNewFlowRelation(source, target);
		} catch (ParameterException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return target;
	}
	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		PNGraph graph = getEditor().getGraphComponent().getGraph();
		PNGraphCell source = (PNGraphCell) graph.getSelectionCell();
		PNGraphCell target = null;
		double centerX = source.getGeometry().getCenterX();
		double centerY = source.getGeometry().getCenterY();
		centerX += deltaX;
		centerY += deltaY;
		if(centerX >0 && centerY>0){
		target = createNewNodeWithEdge(source,getEditor().getGraphComponent().getGraph(),centerX,centerY);
		graph.setSelectionCell(target);}
				
	}
    

}
