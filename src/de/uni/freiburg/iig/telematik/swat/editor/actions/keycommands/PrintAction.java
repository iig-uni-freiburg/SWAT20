package de.uni.freiburg.iig.telematik.swat.editor.actions.keycommands;

import java.awt.event.ActionEvent;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;

public class PrintAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 8493344159268498517L;

	protected boolean showDialog, success;

	public PrintAction(PNEditor pnEditor) throws ParameterException {
		super(pnEditor);
	}

	public void actionPerformed(ActionEvent e) {
		success = false;		
		System.out.println("\n//PRINT NET INFORMATION//\n");
		System.out.println(editor.getNetContainer().getPetriNet());
		System.out.println(editor.getNetContainer().getPetriNetGraphics());
		System.out.println("////////////////////////");
	}

	public boolean isSuccess() {
		return success;
	}
}
