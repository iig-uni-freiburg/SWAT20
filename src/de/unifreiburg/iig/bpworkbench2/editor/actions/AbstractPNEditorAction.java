package de.unifreiburg.iig.bpworkbench2.editor.actions;

import javax.swing.AbstractAction;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.unifreiburg.iig.bpworkbench2.editor.gui.PNEditor;

public abstract class AbstractPNEditorAction extends AbstractAction {

	private static final long serialVersionUID = -308657344513249134L;
	
	protected PNEditor editor = null;
	
	public AbstractPNEditorAction(PNEditor editor) throws ParameterException {
		super();
		Validate.notNull(editor);
		this.editor = editor;
	}
	
	protected PNEditor getEditor(){
		return editor;
	}

}
