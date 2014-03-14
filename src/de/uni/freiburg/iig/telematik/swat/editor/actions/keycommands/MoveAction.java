package de.uni.freiburg.iig.telematik.swat.editor.actions.keycommands;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;

public class MoveAction extends AbstractPNEditorAction {

	private int deltaX;
	private int deltaY;

	public MoveAction(PNEditor editor, int dx, int dy) throws ParameterException {
		super(editor);
		deltaX = dx;
		deltaY = dy;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1728027231812006823L;

	@Override
	public void actionPerformed(ActionEvent e) {
		PNGraph graph = getEditor().getGraphComponent().getGraph();
		 graph.moveCells(graph.getSelectionCells(), deltaX, deltaY);
		
	}


}
