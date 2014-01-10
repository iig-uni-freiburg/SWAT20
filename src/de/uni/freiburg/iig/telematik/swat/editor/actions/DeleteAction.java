package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.JMenuItem;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;

public class DeleteAction extends AbstractPNEditorAction {
	
	private static final long serialVersionUID = -7309816433378748227L;
	
	private Action transferAction = null;

	public DeleteAction(PNEditor editor, Action transferAction) throws ParameterException, PropertyException, IOException {
		super(editor, "Delete", IconFactory.getIcon("delete"));
		this.transferAction = transferAction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		transferAction.actionPerformed(new ActionEvent(this.getEditor().getGraphComponent(), e.getID(), e.getActionCommand()));
	}

}
