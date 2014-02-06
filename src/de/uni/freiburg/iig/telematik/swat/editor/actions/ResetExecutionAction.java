package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNSerializationFormat;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;

public class ResetExecutionAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 7716993627349722001L;

	protected boolean success = false;
	protected String errorMessage = null;

	
	public ResetExecutionAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Exeuctin", IconFactory.getIcon("reload"));
	}
	
	public void actionPerformed(ActionEvent e) {
		getEditor().getEditorToolbar().setExecutionMode();
		getEditor().getGraphComponent().getGraph().setExecution(true);
		getEditor().getGraphComponent().highlightEnabledTransitions();
	}

//	public boolean isSuccess() {
//		return success;
//	}
//
//	public String getErrorMessage() {
//		return errorMessage;
//	}
}
