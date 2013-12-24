package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;

public class RedoAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 4315293729223367039L;
	
	public RedoAction(PNEditor pnEditor) throws ParameterException, PropertyException, IOException {
		super(pnEditor, "Redo", IconFactory.getIcon("right_circular"));
	}

	public void actionPerformed(ActionEvent e) {
		if (editor != null) {
			editor.getUndoManager().redo();
		}
	}
}
