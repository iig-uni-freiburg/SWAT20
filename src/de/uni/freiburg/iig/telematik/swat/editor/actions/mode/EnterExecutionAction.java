package de.uni.freiburg.iig.telematik.swat.editor.actions.mode;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNSerializationFormat;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;

public class EnterExecutionAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 7716993627349722001L;

	protected boolean success = false;
	protected String errorMessage = null;

	private Image play;

	private Image reset;


	
	public EnterExecutionAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Exeuctin", IconFactory.getIcon("play"));
//		play = getIcon().getImage();
//		reset = IconFactory.getIcon("restart").getImage();
		
	}
	
	public void actionPerformed(ActionEvent e) {
		getEditor().getEditorToolbar().setExecutionMode();

		
	}


}
