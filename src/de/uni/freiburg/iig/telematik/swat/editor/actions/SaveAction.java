package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNSerializationFormat;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;

public class SaveAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 7716993627349722001L;
	
	protected boolean success = false;
	protected String errorMessage = null;

    public SaveAction(PNEditor editor) throws ParameterException {
		super(editor);
	}

	public void actionPerformed(ActionEvent e) {
		success = true;
		try {
			String filename = editor.getFileReference().getAbsolutePath();
			PNSerialization.serialize(editor.getNetContainer(), PNSerializationFormat.PNML, filename);
			editor.setModified(false);
			editor.setFileReference(new File(filename));
		} catch (Exception ex) {
			success = false;
			errorMessage = ex.getMessage();
		}
	}

    public boolean isSuccess() {
        return success;
    }
    
    public String getErrorMessage(){
    	return errorMessage;
    }
}
