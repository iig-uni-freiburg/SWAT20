package de.uni.freiburg.iig.telematik.swat.editor.actions;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;

public abstract class AbstractPNEditorAction extends AbstractAction {

	private static final long serialVersionUID = -308657344513249134L;

	protected PNEditor editor = null;

	public AbstractPNEditorAction(PNEditor editor) throws ParameterException {
		super();
		setEditor(editor);
	}
	
	
	public AbstractPNEditorAction(PNEditor editor, String name) throws ParameterException {
		super(name);
		setEditor(editor);
	}
	
	public AbstractPNEditorAction(PNEditor editor, String name, Icon icon) throws ParameterException {
		super(name, icon);
		setEditor(editor);
	}
	
	private void setEditor(PNEditor editor) throws ParameterException{
		Validate.notNull(editor);
		this.editor = editor;
	}


	protected PNEditor getEditor() {
		return editor;
	}

}
