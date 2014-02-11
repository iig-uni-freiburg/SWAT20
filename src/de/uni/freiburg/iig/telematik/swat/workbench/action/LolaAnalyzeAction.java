package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.lola.LolaTransformator;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;

public class LolaAnalyzeAction extends AbstractAction {

	private static final long serialVersionUID = 9111775745565090191L;
	private SwatTabView tabView;

	public LolaAnalyzeAction(SwatTabView tabView) {
		this.tabView = tabView;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			LolaTransformator lola = new LolaTransformator((PNEditor) tabView.getSelectedComponent());
			//run LoLA

			//present results
		} catch (ClassCastException e) {
			MessageDialog.getInstance().addMessage("This is not a Petri Net");
		}


	}

}
