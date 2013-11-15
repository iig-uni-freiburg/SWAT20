package de.uni.freiburg.iig.telematik.swat.editor.actions;

import javax.swing.AbstractAction;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;

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
