package de.uni.freiburg.iig.telematik.swat.workbench;

import java.awt.Window;

import de.invation.code.toval.graphic.dialog.FileNameDialog;

public class PNNameDialog extends FileNameDialog {

	public PNNameDialog(Window parent, String message, String title, boolean allowSpaces) {
		super(parent, message, title, allowSpaces);
	}

	@Override
	protected boolean isValid(String input) {
		if(!super.isValid(input))
			return false;
		
		return !SwatComponents.getInstance().containsPetriNetWithID(input);
	}

	

}
