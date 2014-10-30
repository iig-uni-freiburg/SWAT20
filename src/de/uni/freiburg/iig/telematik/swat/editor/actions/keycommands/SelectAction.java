package de.uni.freiburg.iig.telematik.swat.editor.actions.keycommands;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;

public class SelectAction extends AbstractPNEditorAction {

	private int deltaX;
	private int deltaY;
	private PNComponent type;

	public SelectAction(PNEditor editor, PNComponent type) throws ParameterException {
		super(editor);
		this.type =type;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1728027231812006823L;

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		PNGraph graph = getEditor().getGraphComponent().getGraph();
		 graph.selectPNGraphCells(type);		
	}


}
