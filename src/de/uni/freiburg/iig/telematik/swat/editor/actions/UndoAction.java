package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;

public class UndoAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 4315293729223367039L;

	public UndoAction(PNEditor pnEditor) throws ParameterException, PropertyException, IOException {
		super(pnEditor, "Undo", IconFactory.getIcon("left_circular"));
	}

	public void actionPerformed(ActionEvent e) {
		if (editor != null) {
			editor.getUndoManager().undo();
		}
	}
}