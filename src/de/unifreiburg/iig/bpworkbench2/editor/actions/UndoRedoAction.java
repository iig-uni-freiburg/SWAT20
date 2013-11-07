package de.unifreiburg.iig.bpworkbench2.editor.actions;

import java.awt.event.ActionEvent;

import de.invation.code.toval.validate.ParameterException;
import de.unifreiburg.iig.bpworkbench2.editor.PNEditor;

public class UndoRedoAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 4315293729223367039L;
	
	protected boolean undo;

    public UndoRedoAction(PNEditor pnEditor, boolean undo) throws ParameterException {
    	super(pnEditor);
        this.undo = undo;
    }

    public void actionPerformed(ActionEvent e) {
        if (editor != null) {
            if (undo) {
                editor.getUndoManager().undo();
            } else {
                editor.getUndoManager().redo();
            }
        }
    }
}
