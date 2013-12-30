package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.Action;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;

public class PasteAction extends AbstractPNEditorAction {
	
	private static final long serialVersionUID = -7309816433378748227L;
	
	private Action transferAction = null;

	public PasteAction(PNEditor editor, Action transferAction) throws ParameterException, PropertyException, IOException {
		super(editor, "Paste", IconFactory.getIcon("down"));
		this.transferAction = transferAction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		transferAction.actionPerformed(new ActionEvent(this.getEditor().getGraphComponent(), e.getID(), e.getActionCommand()));
	}

}
