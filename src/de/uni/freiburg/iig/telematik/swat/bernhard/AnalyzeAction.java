package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;

public class AnalyzeAction extends AbstractPNEditorAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 891666558349291133L;
	private PatternWindow patternWindow;
	public AnalyzeAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Analyze", IconFactory.getIcon("spiderweb"));
		patternWindow=new PatternWindow();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		patternWindow.setVisible(editor);
	}
}
