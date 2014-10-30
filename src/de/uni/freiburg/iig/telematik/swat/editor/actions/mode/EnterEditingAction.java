package de.uni.freiburg.iig.telematik.swat.editor.actions.mode;

import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;

public class EnterEditingAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 7716993627349722001L;

	protected boolean success = false;
	protected String errorMessage = null;

	
	public EnterEditingAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Edit", IconFactory.getIcon("edit"));
	}
	
	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		getEditor().getEditorToolbar().setEditingMode();
		
	}


}
