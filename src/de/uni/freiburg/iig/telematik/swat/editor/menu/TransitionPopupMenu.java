package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.graphpopup.TransitionSilentAction;

public class TransitionPopupMenu extends JPopupMenu {
	
	private static final long serialVersionUID = -2983257974918330746L;

	public TransitionPopupMenu(PNEditor pnEditor) throws ParameterException, PropertyException, IOException {
		Validate.notNull(pnEditor);
		boolean selected = !pnEditor.getGraphComponent().getGraph().isSelectionEmpty();

		
		

		 JMenu submenu = (JMenu) add(new JMenu("Transition"));
			
		 submenu.add(new TransitionSilentAction(pnEditor, "silent",
		 true));
		 submenu.add(new TransitionSilentAction(pnEditor, "not silent",
		 false));		
		 
		 
	}
}



